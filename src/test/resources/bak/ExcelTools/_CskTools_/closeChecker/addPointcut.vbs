Option Explicit

Const TAG_PTN_FULL = "FULL"
Const TAG_PTN_ARGTAG = "ARGTAG"
Const TAG_PTN_JVMOPT = "JVMOPT"

Dim CHECKER_BASE_DIR
Dim PRODUCT_ID


CHECKER_BASE_DIR = WScript.Arguments.Item(0)
PRODUCT_ID = Lcase(WScript.Arguments.Item(1))

' pom追記処理実行
Call modifyPomXML

' pointcut追記処理実行
'Call modifyAppProductTestXML

Sub modifyPomXML()
    Const PATH_POM_XML = "pom.xml"

'	Dim Stream
'    Dim buf, bufList
'    Dim outBuf
'    Dim isBuild, isAddJavaassist
'    Dim cnt
'    Dim i

'    ' 読み込み
'    Set Stream = CreateObject("ADODB.Stream")
'    Stream.Open
'    Stream.Type = 2
'    Stream.Charset = "utf-8"
'    Stream.LoadFromFile = PATH_POM_XML
'    buf = Stream.readText()
'    Stream.Close
'
'    bufList = Split(buf, vbLf)
'    
'    isBuild = False
'    isAddJavaassist = False
'    cnt = UBound(bufList)
'    For i = 0 To cnt
'        
'        ' TODO surefire-pluginの中にarglineを追記する
'        ' <build>タグが存在しない場合は丸ごと追記
'        ' TODO <build>タグが存在した場合は種々判定が必要
'        If isBuild = False And InStr(bufList(i), "<build>") > 0 Then
'            isBuild = True
'        End If
'        If isAddJavaassist = False And isBuild = True And InStr(bufList(i), "</build>") > 0 Then
'            outBuf = outBuf & getJavaassistTag(TAG_PTN_FULL)
'            isAddJavaassist = True
'        End If
'        If isAddJavaassist = False And isBuild = False And InStr(bufList(i), "</project>") > 0 Then
'            outBuf = outBuf & getJavaassistTag(TAG_PTN_FULL)
'            isAddJavaassist = True
'            isBuild = True
'        End If
'        
'        outBuf = outBuf & bufList(i) & vbLf
'    Next
'    
'    ' 書き込み
'    ' ・ADODB.Streamで"utf-8"読込→"utf-8"書出しを実行した場合、ファイル冒頭に"\ufffd"が
'    '   挿入され、mavenでparseエラーが発生したのでpom.xmlの書出しは"shift_jis"にしている
'    Set Stream = CreateObject("ADODB.Stream")
'    Stream.Open
'    Stream.Type = 2
'    Stream.Charset = "shift-jis"
'    Stream.WriteText (outBuf)
'    Stream.SaveToFile PATH_POM_XML, 2
'    Stream.Close
'    
'    Set Stream = Nothing

Dim objDOM, rtResult, nodeList, argLineTag, obj
Dim projectTag, buildTag, pluginsTag, pluginTag, groupTag, artifactTag, configurationTag
Dim isBuildTag, isSurefireOption, isArgLine
Dim idxPlugin, strArgLine
Set objDOM = WScript.CreateObject("MSXML2.DOMDocument") 
rtResult = objDOM.load("pom.xml") 
If rtResult = True Then
    isSurefireOption = False
    idxPlugin = 0
    
    ' buildタグ存在チェック
    isBuildTag = False
    Set nodeList = objDOM.documentElement.selectNodes("/project/build") 
    For Each obj In nodeList
        isBuildTag = True
    Next

    ' 「maven-surefire-plugin」オプション設定存在チェック
    Set nodeList = objDOM.documentElement.selectNodes("/project/build/plugins/plugin/artifactId") 
    For Each obj In nodeList
        If Trim(obj.text) = "maven-surefire-plugin" Then
            isSurefireOption = True
            Exit For
           'MsgBox obj.nodeName & " : " & obj.text
        End If
        idxPlugin = idxPlugin + 1
    Next
    'MsgBox isSurefireOption & ":" & idxPlugin
    
    
    isArgLine = False
    If isSurefireOption = True Then
        ' 「argLine」存在チェック
        Set nodeList = objDOM.documentElement.selectNodes("/project/build/plugins/plugin[" & idxPlugin & "]/configuration/argLine")
        For Each obj In nodeList
            'MsgBox obj.nodeName & " : " & obj.text 
            If obj.text <> "" Then
                isArgLine = True
                obj.text = obj.text & " " & getJavaassistTag(TAG_PTN_JVMOPT)
                Exit For
               
            End If
        Next
        If isArgLine = False Then
             Set obj = objDOM.documentElement.selectSingleNode("/project/build/plugins/plugin[" & idxPlugin & "]/configuration[0]")
             Set argLineTag = objDOM.createElement("argLine")
             argLineTag.text = getJavaassistTag(TAG_PTN_JVMOPT)
             obj.appendChild(argLineTag)
        End if
    Else

        Set pluginsTag = objDOM.createElement("plugins")
        Set pluginTag = objDOM.createElement("plugin")
        Set groupTag = objDOM.createElement("groupId")
        groupTag.text = "org.apache.maven.plugins"
        Set artifactTag = objDOM.createElement("artifactId")
        artifactTag.text = "maven-surefire-plugin"
        Set configurationTag = objDOM.createElement("configuration")
        Set argLineTag = objDOM.createElement("argLine")
        argLineTag.text = getJavaassistTag(TAG_PTN_JVMOPT)
        
        configurationTag.appendChild(argLineTag)
        pluginTag.appendChild(groupTag)
        pluginTag.appendChild(artifactTag)
        pluginTag.appendChild(configurationTag)
        pluginsTag.appendChild(pluginTag)

        If isBuildTag = False Then
            Set projectTag = objDOM.documentElement.selectSingleNode("/project")
            Set buildTag = objDOM.createElement("build")
            buildTag.appendChild(pluginsTag)
            projectTag.appendChild(buildTag)
        Else
            Set buildTag = objDOM.documentElement.selectSingleNode("/project/build")
            buildTag.appendChild(pluginsTag)
        End If
    End If
    
End If 
objDOM.save("pom.xml") 
Set objDOM = Nothing 

    
End Sub

Function getJavaassistTag(ptn)

    If ptn = TAG_PTN_FULL Then
    getJavaassistTag = "" & vbCrLf & _
"    <build>" & vbCrLf & _
"        <plugins>" & vbCrLf & _
"            <plugin>" & vbCrLf & _
"                <artifactId>maven-surefire-plugin</artifactId>" & vbCrLf & _
"                <configuration>" & vbCrLf & _
"                    " & getJavaassistTag(TAG_PTN_ARGTAG) & vbCrLf & _
"                </configuration>" & vbCrLf & _
"            </plugin>" & vbCrLf & _
"        </plugins>" & vbCrLf & _
"    </build>" & vbCrLf
    ElseIf ptn = TAG_PTN_ARGTAG Then
    getJavaassistTag = "<argLine>" & getJavaassistTag(TAG_PTN_JVMOPT) & "</argLine>"
    ElseIf ptn = TAG_PTN_JVMOPT Then
    getJavaassistTag = "-javaagent:" & CHECKER_BASE_DIR & "\closecheck-1.0.0.jar=""targetClass=" & getTargetClass() & """"
    End If
    
End Function

Function getTargetClass()
    If InStr(PRODUCT_ID, "-bsp") > 0 Or InStr(PRODUCT_ID, "-bmc") > 0 Then
        getTargetClass = Replace(PRODUCT_ID, "-", ".") & ".*Impl"
    Else
        getTargetClass = "murata.*." & Mid(PRODUCT_ID, 1, 2) & "." & PRODUCT_ID & ".*"
    End If
End Function


Sub modifyAppProductTestXML()
    Const PATH_APP_PRODUCT_TEST_XML = "src\test\resources\springconf\applicationContext-product-test.xml"

	Dim Stream
    Dim buf, bufList
    Dim outBuf
    Dim isAddAspect
    Dim cnt
    Dim i

    ' 読み込み
    Set Stream = CreateObject("ADODB.Stream")
    Stream.Open
    Stream.Type = 2
    Stream.Charset = "utf-8"
    Stream.LoadFromFile = PATH_APP_PRODUCT_TEST_XML
    buf = Stream.readText()
    Stream.Close

    bufList = Split(buf, vbLf)
    
    isAddAspect = False
    cnt = UBound(bufList)
    For i = 0 To cnt
        outBuf = outBuf & bufList(i) & vbLf
        
        ' <beans>タグ直後にpointcutを追加する
        If isAddAspect = False And InStr(Trim(bufList(i)), "<beans>") > 0 Then
            outBuf = outBuf & getAspectTag
            isAddAspect = True
        End If
    Next
    
    ' 書き込み
    Set Stream = CreateObject("ADODB.Stream")
    Stream.Open
    Stream.Type = 2
    Stream.Charset = "utf-8"
    Stream.WriteText (outBuf)
    Stream.SaveToFile PATH_APP_PRODUCT_TEST_XML, 2
    Stream.Close
    
    Set Stream = Nothing
    
End Sub

Function getAspectTag()
    getAspectTag = "" & vbCrLf & _
"    <bean class=""org.springframework.aop.support.DefaultPointcutAdvisor"">" & vbCrLf & _
"        <property name=""advice"">" & vbCrLf & _
"            <bean class=""CloseCheckAspect""/>" & vbCrLf & _
"        </property>" & vbCrLf & _
"        <property name=""pointcut"">" & vbCrLf & _
"            <bean class=""CloseCheckAspect""/>" & vbCrLf & _
"        </property>" & vbCrLf & _
"    </bean>" & vbCrLf & vbCrLf
End Function
