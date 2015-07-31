package jp.gr.java_conf.boj.app.regex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jp.gr.java_conf.boj.app.regex.Example.Command;
import jp.gr.java_conf.boj.app.regex.ReplacementArea.ReplacementAreaInfo;
import jp.gr.java_conf.boj.app.regex.ReplacementField.IllegalInputException;
import jp.gr.java_conf.boj.app.regex.WhitespaceVisibleArea.WhitespaceVisibleAreaInfo;
import jp.gr.java_conf.boj.app.regex.WhitespaceVisibleField.WhitespaceVisibleFieldInfo;
import jp.gr.java_conf.boj.lib.gui.CoveredPane;

public class RegexTester {

    // コンストラクタで生成
    private final JFrame frame;

    // PatternクラスのメソッドのMethodInfoを格納した読み取り専用マップ
    private final Map<String, MethodInfo> patternMap;

    // MatcherクラスのメソッドのMethodInfoを格納した読み取り専用マップ
    private final Map<String, MethodInfo> matcherMap;

    // Example名をキーにExampleオブジェクトを格納したマップ
    private final Map<String, Example> exampleMap;

    // チュートリアルモードか否か
    private boolean tutorialMode;

    private List<CoveredPane> coverList = new ArrayList<CoveredPane>();

    ////////// 以下イベント処理の為に必要なコンポーネントの参照 //////////

    // centerPanel
    private WhitespaceVisibleArea sourceArea;
    private ReplacementArea replacementArea;
    private ExplanationArea explanationArea;
    private WhitespaceVisibleField regexField;
    private ReplacementField replacementField;
    private JTextField findTF = new JTextField(3);

    private JComboBox newlineModeComboBox = new JComboBox(
                                                new Object[]{LF, CRLF});

    private JButton resetButton = new JButton("reset");
    private JButton findButton = new JButton(FIND_METHOD);
    private JButton findIntButton = new JButton(FIND_INT_METHOD);
    private JButton lookingAtButton = new JButton(LOOKING_AT_METHOD);
    private JButton matchesButton = new JButton(MATCHES_METHOD);
    private JButton appendReplacementButton = new JButton(
                                                  "appendReplacement");
    private JButton appendTailButton = new JButton("appendTail");
    private JButton replaceAllButton = new JButton("replaceAll");
    private JButton replaceFirstButton = new JButton("replaceFirst");
    private JButton sourceClearButton =
        new JButton(Main.getStringProperty(
                        "component.string.clear.button.label"));
    private JButton replacedBufClearButton =
        new JButton(Main.getStringProperty(
                        "component.string.clear.button.label"));
    private JButton backButton;
    private JButton nextButton;
    private JButton clearTutorialButton =
        new JButton(Main.getStringProperty(
                        "component.string.cleartutorial.button.label"));

    // modeComp
    private Map<Integer, JCheckBox> modeCBMap =
                                new LinkedHashMap<Integer, JCheckBox>();
    private JPanel modeCompPanel = new JPanel();
    private TitledBorder modeCompBorder =
        BorderFactory.createTitledBorder(
            Main.getStringProperty("component.string.mode.border.title"));


    // regionComp
    private JButton regionButton = new JButton("region");
    private JTextField regionStartTF = new JTextField(3);
    private JTextField regionEndTF = new JTextField(3);
    private JLabel regionStartLabel = new JLabel();
    private JLabel regionEndLabel = new JLabel();
    private JLabel inputLengthLabel = new JLabel();
    private JRadioButton anchoringRB = new JRadioButton("true");
    private JRadioButton nonAnchoringRB = new JRadioButton("false");
    private JRadioButton tranparentRB = new JRadioButton("true");
    private JRadioButton opaqueRB = new JRadioButton("false");

    // stateComp
    private JLabel groupCountLabel = new JLabel();
    private JLabel hitEndLabel = new JLabel();
    private JLabel requireEndLabel = new JLabel();
    private JLabel startLabel = new JLabel();
    private JLabel endLabel = new JLabel();
    private JComboBox groupComboBox = new JComboBox(new Integer[]{0});

    // leftPanel
    private JList matcherList;
    private JList patternList;

    /**
     * GUIの生成を行う。<br>
     * このコンストラクタ実行後にgetFrameメソッドでパック済みの
     * JFrameオブジェクトを取得して表示する事で
     * アプリケーションが起動する。<br>
     * このコンストラクタは設定ファイルに問題がある場合などには
     * アプリケーションは実行不能なので実行時例外をスローする。
     *
     * @throws RuntimeException getPatternMapメソッドや
     *                          getMatcherMapメソッドで供給するマップを
     *                          作成できない場合
     */
    public RegexTester() {
        Map<String, Map<String, MethodInfo>> map = Main.getClassMap();
        patternMap = Collections.unmodifiableMap(map.get("Pattern"));
        matcherMap = Collections.unmodifiableMap(map.get("Matcher"));
        exampleMap = Main.getExampleMap();
        frame = new FrameBuilder().build();
    }

    /**
     * コンストラクタで生成したフレームを返す。<br>
     * フレームのサイズはpack()メソッドで決定されている。
     *
     * @return アプリケーションのGUIであるJFrameオブジェクト
     */
    public JFrame getFrame() {
        return frame;
    }

    // ExplanationAreaの内容をクリアする。
    private void clearExplanation() {
        explanationArea.clear();
        updateNavigateButton();
        setTutorialMode(false);
    }

    // チュートリアルモードではない場合のみ
    // setExplanationメソッドを実行する。
    private void checkModeAndSetExplanation(String html,
                                            boolean isTutorial) {
        if (isTutorialMode()) {
            return;
        }
        setExplanation(html, isTutorial);
    }

    // ExplanationAreaの現在の説明を破棄して現在の説明を引数のhtmlにする。
    // チュートリアルモードで表示するExampleのhtmlの場合には
    // isTutorialをtrueにする。
    private void setExplanation(String html, boolean isTutorial) {
        explanationArea.set(html, isTutorial);
        updateNavigateButton();
        setTutorialMode(isTutorial);
    }

    // ExplanationAreaの現在の説明を戻るボタンで戻れるようにして
    // 現在の説明を引数のhtmlにする。
    // チュートリアルモードで表示するExampleのhtmlの場合には
    // isTutorialをtrueにする。
    // Exampleから取り出した説明の場合にはisExampleをtrueにする。
    private void shiftExplanation(String html, boolean isTutorial) {
        explanationArea.shift(html, isTutorial);
        updateNavigateButton();
        setTutorialMode(isTutorial);
    }

    // ExplanationAreaにExampleから取り出した説明を設定する。
    // 引数はnullではない事は呼び出しもとでチェックする。
    // ExplanationAreaの説明を置き換えるだけで戻る、進むボタンは
    // そのままとなる。
    // ExampleからExampleへの遷移は必ずこのメソッドで行う
    private void replaceExplanation(String html) {
        explanationArea.replace(html);
        //updateNavigateButton();
        setTutorialMode(true);
    }

    // ExplanationAreaにあれば次の説明を表示
    private void nextExplanation() {
        if (!explanationArea.hasNext()) {
            return;
        }
        explanationArea.next();
        updateNavigateButton();
        // 戻る進むボタンではサンプルモードにはならず、
        // チュートリアルモードの場合も解除される。
        setTutorialMode(false);
    }

    // ExplanationAreaにあれば前の説明を表示
    private void previousExplanation() {
        if (!explanationArea.hasPrevious()) {
            return;
        }
        explanationArea.previous();
        updateNavigateButton();
        // 戻る進むボタンではチュートリアルにはならず、
        // 現在チュートリアルモードの場合も解除される。
        setTutorialMode(false);
    }

    /*
     * チュートリアルモードの設定と解除
     * チュートリアルモードの場合はGUIの戻るボタン、進むボタン、
     * チュートリアルクリアボタン以外のコンポーネントを使用不可にする。
     * 左タブペインのコンポーネントは常に使用可能
     */
    private void setTutorialMode(boolean tutorialMode) {
        if (this.tutorialMode == tutorialMode) {
            return;
        }

        if (tutorialMode) {
            for (CoveredPane cover : coverList) {
                cover.cover();
            }
        } else {
            for (CoveredPane cover : coverList) {
                cover.uncover();
            }
        }
        clearTutorialButton.setEnabled(tutorialMode);
        this.tutorialMode = tutorialMode;
    }

    private boolean isTutorialMode() {
        return tutorialMode;
    }

    // 戻る、進むボタンの状態をExplanationAreaに問い合わせて更新する。
    private void updateNavigateButton() {
        boolean nextButtonEnabled = nextButton.isEnabled();
        if (nextButtonEnabled != explanationArea.hasNext()) {
            nextButton.setEnabled(!nextButtonEnabled);
        }
        boolean backButtonEnabled = backButton.isEnabled();
        if (backButtonEnabled != explanationArea.hasPrevious()) {
            backButton.setEnabled(!backButtonEnabled);
        }
    }

    private void processExample(Example example, boolean isSet) {
        boolean isTutorial = example.isTutorial;
        if (!example.html.equals("KEEP_NOW")) {
            String html = (isTutorial) 
                          ? Main.toHTML(example.html, EXPLAIN_RRGGBB)
                          : Main.toHTML(example.html);
            if (isSet) {
                setExplanation(html, isTutorial);
            } else {
                if (isTutorial && isTutorialMode()) {
                    replaceExplanation(html);
                } else {
                    shiftExplanation(html, isTutorial);
                }
            }
        }

        if (isTutorial) {
            AppState state = example.state;
            if (state != null) {
                setAppState(state);
            }
            Command[] commands = example.commands;
            if ((commands != null) && (commands.length > 0)) {
                executeCommand(commands);
            }
        }
    }

    // 引数のExample.Commandが表す処理を実行
    private void executeCommand(Command[] commands) {
        for (Command command : commands) {
            JButton button = null;
            int groupIndex = -1;
            if (command == Command.RESET) {
                button = resetButton;
            } else if (command == Command.CLEAR_SOURCE) {
                button = sourceClearButton;
            } else if (command == Command.CLEAR_REPLACEMENT_BUF) {
                button = replacedBufClearButton;
            } else if (command == Command.FIND) {
                button = findButton;
            } else if (command == Command.MATCHES) {
                button =matchesButton;
            } else if (command == Command.LOOKING_AT) {
                button = lookingAtButton;
            } else if (command == Command.FIND_INT) {
                button = findIntButton;
            } else if (command == Command.REPLACE_ALL) {
                button = replaceAllButton;
            } else if (command == Command.APPEND_REPLACEMENT) {
                button = appendReplacementButton;
            } else if (command == Command.REPLACE_FIRST) {
                button = replaceFirstButton;
            } else if (command == Command.APPEND_TAIL) {
                button = appendTailButton;
            } else if (command == Command.REGION) {
                button = regionButton;
            } else if (command == Command.GROUP0) {
                groupIndex = 0;
            } else if (command == Command.GROUP1) {
                groupIndex = 1;
            } else if (command == Command.GROUP2) {
                groupIndex = 2;
            } else if (command == Command.GROUP3) {
                groupIndex = 3;
            } else if (command == Command.GROUP4) {
                groupIndex = 4;
            } else if (command == Command.GROUP5) {
                groupIndex = 5;
            }

            if (button != null) {
                final JButton clickedButton = button;
                EventQueue.invokeLater(
                    new Runnable() {
                        public void run() {
                            clickedButton.doClick();
                        }
                    }
                );
            } else if (groupIndex >= 0) {
                final int selectedIndex = groupIndex;
                EventQueue.invokeLater(
                    new Runnable() {
                        public void run() {
                            groupComboBox.setSelectedIndex(selectedIndex);
                        }
                    }
                );
            }

        }
    }

    // AppStateの内容をコンポーネントに反映させる。
    private void setAppState(AppState state) {
        int regexLength = state.regex.length();
        if (regexLength == 0) {
            if (regexField.getText().length() > 0) {
                regexField.setText("");
            }
        } else if (regexLength > 0) {
            regexField.setText(state.regex);
        }


        int replacementLength = state.replacement.length();
        if (replacementLength == 0) {
            if (replacementField.getText().length() > 0) {
                replacementField.setText("");
            }
        } else if (replacementLength > 0) {
            replacementField.setText(state.replacement);
        }

        int inputLength = state.inputChars.length();
        if (inputLength == 0) {
            if (sourceArea.getText().length() > 0) {
                sourceArea.setText("");
            }
        } else if (inputLength > 0) {
            sourceArea.setText(state.inputChars);
        }

        int findIndex = state.findIndex;
        if (findIndex >= 0) {
            String text = (findIndex == 0) ? ""
                                           : String.valueOf(findIndex);
            findTF.setText(text);
        }

        int regionStart = state.regionStart;
        if (regionStart >= 0) {
            String text = (regionStart == 0) ? ""
                                             : String.valueOf(regionStart);
            regionStartTF.setText(text);
        }

        int regionEnd = state.regionEnd;
        if (regionEnd >= 0) {
            String text = (regionEnd == 0) ? ""
                                           : String.valueOf(regionEnd);
            regionEndTF.setText(text);
        }

        if (state.isAnchoringBounds) {
            anchoringRB.setSelected(true);
        } else {
            nonAnchoringRB.setSelected(true);
        }

        if (state.isTransparentBounds) {
            tranparentRB.setSelected(true);
        } else {
            opaqueRB.setSelected(true);
        }

        if (state.isCRLF) {
            if (newlineModeComboBox.getSelectedItem() == LF) {
                newlineModeComboBox.setSelectedItem(CRLF);
            }
        } else {
            if (newlineModeComboBox.getSelectedItem() == CRLF) {
                newlineModeComboBox.setSelectedItem(LF);
            }
        }

        if (state.modemusk >= 0) {
            Set<Integer> keys = modeCBMap.keySet();
            for (Integer i : keys) {
                JCheckBox cb = modeCBMap.get(i);
                cb.setSelected(state.containsMode(i));
            }
        }
    }

    /*
     * 左タブペインのMatcherやPatternのメソッドリストを選択した際の
     * イベントを処理するリスナクラス
     */
    private class ListSelectionEventHandler implements ListSelectionListener {
        /**
         * ListSelectionListener#valueChangedの実装<br>
         * 左タブペインのリストを選択した際のイベントを実装
         */
        public void valueChanged(ListSelectionEvent e) {
            //リスト選択により発生する一連のイベントの最後だけを処理する。
            if (e.getValueIsAdjusting()) {
                return;
            }
            JList source = (JList)e.getSource();
            int index = e.getFirstIndex();
            if (!source.isSelectedIndex(index)) {
                index = e.getLastIndex();
                if (!source.isSelectedIndex(index)) {
                    return;
                }
            }

            if (source == patternList) {
                Object value = patternList.getModel().getElementAt(index);
                MethodInfo info = patternMap.get(value.toString());
                setExplanation(Main.toHTML(info), false);
                if (!matcherList.isSelectionEmpty()) {
                    matcherList.clearSelection();
                }
            } else if (source == matcherList) {
                Object value = matcherList.getModel().getElementAt(index);
                MethodInfo info = matcherMap.get(value.toString());
                setExplanation(Main.toHTML(info), false);
                if (!patternList.isSelectionEmpty()) {
                    patternList.clearSelection();
                }
            }

        }
    }

    /*
     * JEditorPaneのHTMLのリンクをクリックした際のイベントを
     * 処理するリスナクラス。
     */
    private class HyperlinkEventHandler implements HyperlinkListener {

        /**
         * HyperlinkListener#hyperlinkUpdate の実装
         * ExplanationAreaでのリンクのクリックを処理
         */
        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED) {
                return;
            }

            // e.getDescription()はMalformedURLExceptionを伴うので
            // getURL()を使う。(URLは実際には存在しない)
            String url = e.getURL().toString();
            try {
                if (!url.startsWith(LINKED_URL_PRE)) {
                    throw new AssertionError();
                }
                String[] words =
                    url.substring(LINKED_URL_PRE.length()).split("/");

                if (words[0].equals("Matcher")) {
                	MethodInfo info = matcherMap.get(words[1]);
                    shift(info);
                    
                } else if (words[0].equals("Pattern")) {
                	MethodInfo info = patternMap.get(words[1]);
                    shift(info);
                    
                } else if (words[0].equals("Example")) {
                    Example example = exampleMap.get(words[1]);

                    // explanationAreaのリンクはshiftでそれ以外はset
                    if (e.getSource() ==
                            explanationArea.getViewport().getView()) {
                        processExample(example, false);
                    } else {
                        processExample(example, true);
                    }

                }
                else {
                	throw new AssertionError();
                }
                if (!matcherList.isSelectionEmpty()) {
                    matcherList.clearSelection();
                }
                if (!patternList.isSelectionEmpty()) {
                    patternList.clearSelection();
                }

            } catch (Throwable error) {
                JOptionPane.showMessageDialog(getFrame(),
                        ILLEGAL_LINKED_URL_ERROR_MESSAGE + url,
                        "",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        // nullチェック
        private void shift(MethodInfo info) {
            shiftExplanation(Main.toHTML(info), false);

            setNaviButtonEnabled(true, false, false);
            //戻るボタンを有効にし、進むボタンを無効にする。
            if (!backButton.isEnabled()) {
                backButton.setEnabled(true);
            }
            if (nextButton.isEnabled()) {
                nextButton.setEnabled(false);
            }
        }

        private void setNaviButtonEnabled(boolean enabledBackButton,
                                          boolean enabledNextButton,
                                          boolean enabledClearSampleButton) {
            if (enabledBackButton != backButton.isEnabled()) {
                backButton.setEnabled(enabledBackButton);
            }
            if (enabledNextButton != nextButton.isEnabled()) {
                nextButton.setEnabled(enabledNextButton);
            }
            if (enabledClearSampleButton != clearTutorialButton.isEnabled()) {
                clearTutorialButton.setEnabled(enabledClearSampleButton);
            }
        }



    }

    /*
     * イベント処理をまとめたクラス
     */
    private class ActionEventHandler implements ActionListener {

        // Matcherを管理するオブジェクト
        private final MatcherWrapper matcherObj = new MatcherWrapper();

        // 最後にテキストコンポーネントに入力されていた正規表現
        private String regex;

        // 最後にテキストコンポーネントに入力されていたマッチ対象文字列
        private String source;

        /**
         * ActionListener#actionPerformedの実装
         */
        public void actionPerformed(ActionEvent e) {
            Object comp = e.getSource();

            if (comp == newlineModeComboBox) {
                doNewlineModeComboBoxAction();
            } else if (comp == groupComboBox) {
                doGroupComboBoxAction();
            } else if (comp == resetButton) {
                doResetButtonAction();
            } else if (comp == findButton) {
                doFindButtonAction();
            } else if (comp == findIntButton) {
                doFindIntButtonAction();
            } else if (comp == lookingAtButton) {
                doLookingAtButtonAction();
            } else if (comp ==matchesButton) {
                doMatchesButtonAction();
            } else if (comp == appendReplacementButton) {
                doAppendReplacementButtonAction();
            } else if (comp == appendTailButton) {
                doAppendTailButtonAction();
            } else if (comp == replaceAllButton) {
                doReplaceAllButtonAction();
            } else if (comp == replaceFirstButton) {
                doReplaceFirstButtonAction();
            } else if (comp == sourceClearButton) {
                doSourceClearButtonAction();
            } else if (comp == replacedBufClearButton) {
                doReplacedBufClearButtonAction();
            } else if (comp == regionButton) {
                doRegionButtonAction();
            } else if (comp == clearTutorialButton) {
                doClearTutorialButtonAction();
            } else if (comp == nextButton) {
                doNextButtonAction();
            } else if (comp == backButton) {
                doBackButtonAction();
            }

            if (!matcherList.isSelectionEmpty()) {
                matcherList.clearSelection();
            }
            if (!patternList.isSelectionEmpty()) {
                patternList.clearSelection();
            }
        }



        // resetボタンアクション
        private void doResetButtonAction() {
            matcherObj.reset();
            checkModeAndSetExplanation(RESET_MESSAGE, false);
        }

        // findボタンアクション
        private void doFindButtonAction() {
            if (!checkInput()) {
                return;
            }
            showMatchingResult(matcherObj.find(), FIND_METHOD);
        }

        // find(int)ボタンアクション
        private void doFindIntButtonAction() {
            if (!checkInput()) {
                return;
            }

            try {
                int index = Integer.parseInt(findTF.getText());
                showMatchingResult(matcherObj.find(index), FIND_INT_METHOD);
            } catch (Exception e) {
                setExplanation(Main.toHTML(FIND_ERROR_MESSAGE,
                                           ERROR_RRGGBB), false);
            }
        }

        // matchesボタンアクション
        private void doMatchesButtonAction() {
            if (!checkInput()) {
                return;
            }
            showMatchingResult(matcherObj.matches(), MATCHES_METHOD);
        }

        // lookingAtボタンアクション
        private void doLookingAtButtonAction() {
            if (!checkInput()) {
                return;
            }
            showMatchingResult(matcherObj.lookingAt(), LOOKING_AT_METHOD);
        }

        // newlineModeComboBoxボタンアクション
        private void doNewlineModeComboBoxAction() {
            Object item = newlineModeComboBox.getSelectedItem();
            if (item == CRLF) {
                if (sourceArea.isEditable() && !sourceArea.isCRLF()) {
                    sourceArea.setCRLF(true);
                }
            } else {
                if (sourceArea.isEditable() && sourceArea.isCRLF()) {
                    sourceArea.setCRLF(false);
                }
            }
        }

        // regionボタンアクション
        private void doRegionButtonAction() {
            if (!readRegexAndSource()) {
                return;
            }

            int inputLength = source.length();
            int start = -1;
            int end = -1;
            try {
                start = Integer.parseInt(regionStartTF.getText());
                end = Integer.parseInt(regionEndTF.getText());

            } catch (NumberFormatException e) {}

            // 範囲が不正ならメッセージを表示してリターン
            if ((start < 0) || (start > inputLength) ||
                    (end < start) || (end > inputLength)) {
                setExplanation(Main.toHTML(REGION_ERROR_MESSAGE,
                                           ERROR_RRGGBB), false);
                return;
            }

            matcherObj.reset();
            if (!checkAndCreateMatcher()) {
                return;
            }
            matcherObj.region(start, end);
            regionStartTF.setText("");
            regionEndTF.setText("");
            checkModeAndSetExplanation(SET_REGION_MESSAGE, false);
        }

        // クリアボタンアクション(SourceArea)
        private void doSourceClearButtonAction() {
            matcherObj.clearSource();
            checkModeAndSetExplanation(CLEAR_MESSAGE, false);
        }

        // groupコンボボックスアクション
        private void doGroupComboBoxAction() {
            int groupIndex = matcherObj.updateGroup();
            if (groupIndex == 0) {
                checkModeAndSetExplanation(
                    Main.toHTML(GROUP_0_MESSAGE, EXPLAIN_RRGGBB),
                    false);
            } else if (groupIndex > 0) {
                String message = groupIndex + GROUP_MESSAGE;
                checkModeAndSetExplanation(
                    Main.toHTML(message, EXPLAIN_RRGGBB), false);
            }
        }

        // appendTailButtonアクション
        private void doAppendTailButtonAction() {
            if (!checkInput()) {
                return;
            }
            matcherObj.appendTail();
            checkModeAndSetExplanation(
                Main.toHTML(null,
                            null,
                            APPENDTAIL_MESSAGE,
                            EXPLAIN_RRGGBB,
                            matcherMap.get(APPENDTAIL_METHOD)), false);
        }

        // replaceAllButtonアクション
        private void doReplaceAllButtonAction() {
            if (!checkInput()) {
                return;
            }
            String replacement = getReplacement();
            if (replacement == null) {
                return;
            }
            try {
                matcherObj.replaceAll(replacement);
                checkModeAndSetExplanation(
                    Main.toHTML(null,
                                null,
                                REPLACEALL_MESSAGE,
                                EXPLAIN_RRGGBB,
                                matcherMap.get(REPLACEALL_METHOD)), false);

            } catch (Exception e) {
                setExplanation(
                    Main.toHTML(e, REPLACEMENT_ERROR_MESSAGE), false);
            }
        }



        // replaceFirstButtonアクション
        private void doReplaceFirstButtonAction() {
            if (!checkInput()) {
                return;
            }
            String replacement = getReplacement();
            if (replacement == null) {
                return;
            }
            try {
                matcherObj.replaceFirst(replacement);
                checkModeAndSetExplanation(
                    Main.toHTML(null,
                                null,
                                REPLACEFIRST_MESSAGE,
                                EXPLAIN_RRGGBB,
                                matcherMap.get(REPLACEFIRST_METHOD)), false);

            } catch (Exception e) {
                setExplanation(
                    Main.toHTML(e, REPLACEMENT_ERROR_MESSAGE), false);
            }
        }

        // appendReplacementButtonアクション
        private void doAppendReplacementButtonAction() {
            if (!matcherObj.isMatched()) {
                checkModeAndSetExplanation(
                    Main.toHTML(APPENDREPLACEMENT_NO_MATCHED_ERROR_MESSAGE,
                                ERROR_RRGGBB), false);
                return;
            }
            String replacement = getReplacement();
            if (replacement == null) {
                return;
            }
            try {
                matcherObj.appendReplacement(replacement);
                checkModeAndSetExplanation(
                    Main.toHTML(null,
                                null,
                                APPENDREPLACEMENT_MESSAGE,
                                EXPLAIN_RRGGBB,
                                matcherMap.get(
                                    APPENDREPLACEMENT_METHOD)), false);


            } catch (StringIndexOutOfBoundsException e) {
                setExplanation(
                    Main.toHTML(e, ILLEGAL_APPEND_POSITION_ERROR_MESSAGE),
                    false);
            } catch (Exception e) {
                setExplanation(
                    Main.toHTML(e, REPLACEMENT_ERROR_MESSAGE), false);
            }
        }

        // replacedBufClearButtonアクション
        private void doReplacedBufClearButtonAction() {
            replacementArea.setText("");
        }

        // nextButtonアクション
        private void doNextButtonAction() {
            allReset();
            nextExplanation();
        }

        // backButtonアクション
        private void doBackButtonAction() {
            allReset();
            previousExplanation();
        }

        // clearTutorialButtonアクション
        private void doClearTutorialButtonAction() {
            allReset();
            clearExplanation();
        }

        private void allReset() {
            regexField.setText("");
            replacementField.setText("");
            replacementArea.setText("");
            findTF.setText("");
            regionStartTF.setText("");
            regionEndTF.setText("");
            Collection<JCheckBox> modeCBs = modeCBMap.values();
            for (JCheckBox cb : modeCBs) {
                cb.setSelected(false);
            }
            matcherObj.clearSource();
        }

        // 入力された置換構文をExplanationAreaから取得して返す。
        // 置換構文はJavaソースにおける文字列リテラルとして正しく
        // なければならない。
        // 正しくエスケープされていない文字がある場合にはReplacementFieldが
        // IllegalInputExceptionで知らせてくれるのでエラーメッセージを
        // ExplanationAreaに表示してnullを返す。
        private String getReplacement() {
            String replacement = null;
            try {
                replacement = replacementField.getTextAsEscapedChars();
            } catch (IllegalInputException e) {
                StringBuilder buf = new StringBuilder();
                if (e.isQuotCaused()) {
                    buf.append(REPLACEMENT_QUOT_ERROR_MESSAGE);
                } else {
                    buf.append(REPLACEMENT_ESCAPE_ERROR_MESSAGE);
                }
                String text = e.getText();
                int position = e.getPosition();
                int length = text.length();
                buf.append("<br>");
                buf.append("<font color=\"");
                buf.append(CODE_RRGGBB);
                buf.append("\">");
                buf.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                for (int i = 0; i < length; i++) {
                    char c = text.charAt(i);
                    if (i == position) {
                        buf.append("</font>");
                        buf.append("<font color=\"");
                        buf.append(HIGHLIGHT_RRGGBB_1);
                        buf.append("\">");
                        buf.append(c);
                        buf.append("</font>");
                        buf.append("<font color=\"");
                        buf.append(CODE_RRGGBB);
                        buf.append("\">");
                    } else {
                        buf.append(c);
                    }
                }
                buf.append("</font><br>");
                setExplanation(
                    Main.toHTML(buf.toString(), ERROR_RRGGBB), false);
            }
            return replacement;
        }

        // マッチングの結果をExplanationAreaに表示
        // チュートリアルモードの場合は何もしない。
        private void showMatchingResult(boolean isMatched,
                                        String methodKey) {
            if (isTutorialMode()) {
                return;
            }
            String message = (isMatched) ? MATCHED_MESSAGE
                                         : NO_MATCHED_MESSAGE;

            setExplanation(Main.toHTML(String.valueOf(isMatched),
                                       CODE_RRGGBB,
                                       message,
                                       EXPLAIN_RRGGBB,
                                       matcherMap.get(methodKey)), false);
        }

        // readRegexAndSourceとreadRegexAndSourceを行う。
        private boolean checkInput() {
            return (readRegexAndSource() && checkAndCreateMatcher());
            /*
            boolean noproblem = readRegexAndSource();
            if (noproblem) {
                noproblem = checkAndCreateMatcher();
            }
            return noproblem;
            */
        }

        // matcherObj が Matcherオブジェクトを生成していなければ
        // 生成させてtrueを返す。
        // 入力エラーなどでMatcherオブジェクトを生成できない場合は
        // エラーメッセージを表示してfalseを返す。
        // すでに生成済みなら何もせずにtrueを返す。
        private boolean checkAndCreateMatcher() {
            boolean result = true;
            if (!matcherObj.isCreated()) {
                try {
                    matcherObj.create();
                } catch (PatternSyntaxException e) {
                    showRegexError(e);
                    result = false;
                }
            }
            return result;
        }

        // 正規表現とマッチ対象文字列が入力されていなければ
        // メッセージを表示してfalseを返す。
        private boolean readRegexAndSource() {
            regex = regexField.getText();
            source = sourceArea.getText();
            if ((regex == null) || (regex.length() == 0) ||
                    (source == null) || (source.length() == 0)) {

                setExplanation(Main.toHTML(NO_INPUT_ERROR_MESSAGE,
                                           ERROR_RRGGBB), false);
                return false;
            }
            return true;
        }

        // PatternSyntaxExceptionに関するエラーメッセージを表示
        private void showRegexError(PatternSyntaxException e) {
            setExplanation(Main.toHTML(e, PATTERN_SYNTAX_ERROR_MESSAGE),
                           false);
        }


        /*
         * Matcherオブジェクトを管理するクラス。
         * Mathcerに対する操作とその結果必要となる
         * コンポーネントの状態の更新はこのクラスが行う。
         * ただしExplanationAreaにはさわらない。
         */
        private class MatcherWrapper {

            private final Color disabledBorderTitleColor =
                          UIManager.getColor("Label.disabledForeground");

            private Matcher matcher;
            private boolean matched;
            private int lastSelectedGruop;

            // リセットしてSourceAreaをクリアする。
            void clearSource() {
                reset();
                sourceArea.setText("");
            }

            // リセットする。
            // matcherをnullにしてstart end group region を消す
            void reset() {
                if (matcher == null) {
                    return;
                }

                matcher = null;
                matched = false;
                regexField.setEditable(true);
                sourceArea.setEditable(true);
                setModeCompsEnabled(true);
                newlineModeComboBox.setEnabled(true);
                regionStartLabel.setText("");
                regionEndLabel.setText("");
                inputLengthLabel.setText("");
                sourceArea.clearRegion();
                if (groupComboBox.getItemCount() > 1) {
                    groupComboBox.removeAllItems();
                    groupComboBox.addItem(new Integer(0));
                    lastSelectedGruop = 0;
                }
                update();
            }

            // Matcher#findを実行。 start end group を更新する。
            boolean find() {
                return match(FIND_METHOD);
            }

            // リセットしてMatcher#findを実行。start end group を更新する。
            // indexが不正な場合はIndexOutOfBoundsExceptionをスローする。
            boolean find(int index) {
                reset();
                create();
                return match(FIND_INT_METHOD, index);
            }

            // 設定されている領域の先頭部分からマッチングして領域全体が
            // マッチしたらtrueを返す。
            // start end group を更新する。
            boolean matches() {
                return match(MATCHES_METHOD);
            }

            // 設定されている領域の先頭部分がマッチすればtrueを返す。
            // start end group を更新する。
            boolean lookingAt() {
                return match(LOOKING_AT_METHOD);
            }

            // 必要ならグループに関する状態を更新する。
            int updateGroup() {
                int result = -1;
                if (!isMatched()) {
                    return result;
                }
                if (groupComboBox.getItemCount() > 0) {
                    int groupIndex = groupComboBox.getSelectedIndex();
                    if (groupIndex != lastSelectedGruop) {
                        lastSelectedGruop = groupIndex;
                        update();
                    }
                    result = groupIndex;
                }
                return result;
            }

            // リセット後に先頭からマッチングを行い、すべてのマッチした
            // 部分をreplacementに置換した文字列をReplacementAreaに表示
            // replacementが不正な場合は例外をスローする。
            void replaceAll(String replacement) throws RuntimeException {
                checkMatcher();
                setRegionBoundsMode();
                String text = matcher.replaceAll(
                              (replacement == null) ? "" : replacement);
                replacementArea.setText(text);
            }

            // リセット後に先頭からマッチングを行い、最初にマッチした
            // 部分だけreplacementに置換した文字列をReplacementAreaに表示
            // replacementが不正な場合は例外をスローする。
            void replaceFirst(String replacement) {
                checkMatcher();
                setRegionBoundsMode();
                String text = matcher.replaceFirst(
                          (replacement == null) ? "" : replacement);
                replacementArea.setText(text);
            }

            // アペンドポジション以降を引数のbufに追加して返す。
            void appendTail() {
                checkMatcher();
                String text = replacementArea.getText();
                if (text == null) {
                    text = "";
                }
                StringBuffer buf = new StringBuffer(text);
                matcher.appendTail(buf);
                if (text.length() != buf.length()) {
                    replacementArea.setText(buf.toString());
                }
            }

            // ReplacementAreaへの置換操作を行う。
            // 成功したマッチングが行われている事とreplacementの
            // チェックは呼び出しもとで行う。
            // replacementが不正な場合は例外をスローする。
            // マッチ後にappendReplacementを２回以上続けて
            // 呼び出した場合には illegal_append_position を
            // メッセージとして IllegalStateException をスローする。
            void appendReplacement(String replacement) {
                checkMatching();
                StringBuffer buf = new StringBuffer(
                                           replacementArea.getText());
                matcher.appendReplacement(buf, replacement);
                replacementArea.setText(buf.toString());
            }

            // 新たにMatcherオブジェクトを作成する。
            // regexとsourceは呼び出しもとで設定しておく
            // 正規表現パターンが不正な場合はPatternSyntaxExceptionをスロー
            void create() throws PatternSyntaxException {
                int flags = getCompileFlags();
                Pattern pattern = Pattern.compile(regex, flags);
                matcher = pattern.matcher(source);
                inputLengthLabel.setText(String.valueOf(source.length()));

                if (!isTutorialMode()) {
                    regexField.setEditable(false);
                    sourceArea.setEditable(false);
                    setModeCompsEnabled(false);
                    newlineModeComboBox.setEnabled(false);
                }
                regionStartLabel.setText(String.valueOf(
                                             matcher.regionStart()));
                regionEndLabel.setText(String.valueOf(
                                           matcher.regionEnd()));
            }

            // startから(end - 1)までを領域に設定する。
            // 文字シーケンス全体を指定した場合には設定している
            // 領域があればそれを消す。
            // リセットを行い新たなMatcherを生成する。
            // 入力チェック(readRegexAndSource)は呼び出しもとで行う
            // startやendが不正な場合はIllegalArgumentExceptionをスローする
            void region(int start, int end) {
                matcher.region(start, end);
                regionStartLabel.setText(String.valueOf(start));
                regionEndLabel.setText(String.valueOf(end));
                if ((start == 0) && (end == source.length())) {
                    sourceArea.clearRegion();
                } else {
                    sourceArea.region(start, end, OUT_OF_REGION_COLOR);
                }
            }

            // matcherがnullでなければtrue
            boolean isCreated() {
                return (matcher != null);
            }

            // 最後のマッチングでマッチしていたらtrue
            boolean isMatched() {
                return matched;
            }

            // ハイライトとマッチに関する情報を更新
            private void update() {
                sourceArea.clearAllHighlights();

                if ((!isCreated()) || (!isMatched())) {
                    groupCountLabel.setText("");
                    requireEndLabel.setText("");
                    hitEndLabel.setText("");
                    startLabel.setText("");
                    endLabel.setText("");
                } else {
                    // 選択されたグループに関する情報を表示
                    // 0以外の場合はhitEnd, requireEndは何も表示しない
                    int groupCount = matcher.groupCount();
                    groupCountLabel.setText(String.valueOf(groupCount));

                    int groupIndex = groupComboBox.getSelectedIndex();
                    if (groupIndex == 0) {
                        hitEndLabel.setText(String.valueOf(matcher.hitEnd()));
                        requireEndLabel.setText(String.valueOf(
                                                    matcher.requireEnd()));
                    } else {
                        hitEndLabel.setText("");
                        requireEndLabel.setText("");
                    }
                    int groupStart = matcher.start(groupIndex);
                    int groupEnd = matcher.end(groupIndex);
                    startLabel.setText(String.valueOf(groupStart));
                    endLabel.setText(String.valueOf(groupEnd));
                    
                    // 全体にはマッチしたが指定したキャプチャグループには
                    // マッチしなかった場合はgroupStart groupEndともに -1
                    if (groupStart >= 0) {
                    	sourceArea.highlight(groupStart,
                                             groupEnd,
                                             HIGHLIGHT_COLOR);
                    }
                }
            }

            // anchoringとtransparencyをmatcherに設定
            private void setRegionBoundsMode() {
                if (matcher == null) {
                    return;
                }
                matcher.useAnchoringBounds(anchoringRB.isSelected());
                matcher.useTransparentBounds(tranparentRB.isSelected());
            }

            // 設定されているモードに対応する定数の論理和を返す。
            private int getCompileFlags() {
                int flags = 0;
                Set<Integer> keys = modeCBMap.keySet();
                for (Integer mode : keys) {
                    JCheckBox cb = modeCBMap.get(mode);
                    if (cb.isSelected()) {
                        flags |= mode;
                    }
                }
                return flags;
            }

            // モード変更の可、不可を設定
            private void setModeCompsEnabled(boolean enabled) {
                Collection<JCheckBox> modeCBs = modeCBMap.values();
                for (JCheckBox cb : modeCBs) {
                    cb.setEnabled(enabled);
                }

                if (enabled) {
                    modeCompBorder.setTitleColor(null);
                } else {
                    modeCompBorder.setTitleColor(disabledBorderTitleColor);
                }
                modeCompPanel.repaint();
            }

            // find matches lookingAt の実行と結果をGUIに更新
            private boolean match(String methodName) {
                return match(methodName, Integer.MIN_VALUE);
            }

            // find matches lookingAt の実行と結果をGUIに更新
            // param はfind(int)の場合のみ使う引数
            private boolean match(String methodName, int param) {
                checkMatcher();
                setRegionBoundsMode();
                if (methodName.equals(FIND_METHOD)) {
                    matched = matcher.find();
                } else if (methodName.equals(FIND_INT_METHOD)) {
                    matched = matcher.find(param);
                } else if (methodName.equals(MATCHES_METHOD)) {
                    matched = matcher.matches();
                } else if (methodName.equals(LOOKING_AT_METHOD)) {
                    matched = matcher.lookingAt();
                } else {
                    throw new AssertionError();
                }
                if (matched) {
                    int allGroup = matcher.groupCount() + 1;
                    if (allGroup != groupComboBox.getItemCount()) {
                        groupComboBox.removeAllItems();
                        for (int i = 0; i < allGroup; i++) {
                            groupComboBox.addItem(new Integer(i));
                        }
                    }
                    groupComboBox.setSelectedIndex(0);

                } else {
                    if (groupComboBox.getItemCount() > 1) {
                        groupComboBox.removeAllItems();
                        groupComboBox.addItem(new Integer(0));
                    }
                }
                lastSelectedGruop = 0;
                update();
                return matched;
            }

            // Matcher オブジェクトが生成されていない場合は例外をスローする
            private void checkMatcher() {
                if (matcher == null) {
                    throw new IllegalStateException(
                        "program error : matcher is null !");
                }
            }

            // Matcher オブジェクトが生成されていない場合や
            // マッチしていない場合は例外をスローする
            private void checkMatching() {
                checkMatcher();
                if (!isMatched()) {
                    throw new IllegalStateException(
                        "program error : not matching !");
                }
            }
        }



    }

    /*
     * フレーム生成クラス
     */
    private class FrameBuilder {
        private static final int BUTTON_MARGIN = 5;

        //private JPanel leftPanel;
        
        /*
        private JScrollPane patternSP;
        private JScrollPane matcherSP;
        private JScrollPane metaCharSP;
        private JScrollPane charclassesSP;
        private JScrollPane tutorialSP;
        */
        HyperlinkListener hyperlinkListener = new HyperlinkEventHandler();

        final JFrame build() {
            if (frame != null) {
                throw new IllegalStateException("RegexTester.frame != null");
            }
            JFrame frame = new JFrame("Regex Tester");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            createComponent();
            JTabbedPane leftTab = new JTabbedPane(
                                          JTabbedPane.TOP,
                                          JTabbedPane.WRAP_TAB_LAYOUT );
            addListener();

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            //mainPanel.add(createToolBar(), BorderLayout.NORTH);
            mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
            
            JComponent rightPanel = addCover(createRightPanel());
            mainPanel.add(rightPanel, BorderLayout.EAST);

            JScrollPane sp = new JScrollPane(mainPanel);
            frame.getContentPane().add(sp);
            
            // 右パネルの高さを基準にパックしてその時点の
            // センターパネルの３つのテキストエリアのサイズを
            // それぞれの推奨サイズに設定しておく
            frame.pack();
            adjustCenterCompsAfterPacking();
            int rightPanelHeight = rightPanel.getHeight();
            
            // 左パネルを加える。この時点で左パネルの高さは
            // 2000を超えている
            JComponent leftPanel = addCover(createLeftPanel(leftTab));
            mainPanel.add(leftPanel, BorderLayout.WEST);
            
            // 左タブの幅を確定する為のパック
            // パック後の幅を推奨サイズに設定する(高さは
            // 0でうまくいかないので1で試してみる)
            frame.pack();
            adjustLeftPanelAfterPacking(leftTab);
            
            // 最後のパック
            // 左パネルの高さはフレームの高さに合わせられるはず
            // またこれがないと最大化して表示されているフレームをもとの
            // サイズに戻した際のサイズがおかしくなる。
            frame.pack();
            
            // まれに左パネルがうまくスクロールペインに収まらずに
            // 高さが2000ぐらいになってしまう現象が発生した為。
            // Main#createAndShowGUI でmainに記述していた起動部分を
            // イベントディスパッチスレッドで処理するように変更後した後は
            // 発生するかどうか不明
            int leftPanelHeight = leftPanel.getHeight();
            if (leftPanelHeight > rightPanelHeight) {
            	JOptionPane.showMessageDialog(null, 
            		"まだフレームは表示していませんがコンポーネントの高さを" +
            		"うまく調整できませんでした。\n" +
            		"これはまれに発生する不具合ですがフレーム内のレイアウトが" +
            		"不格好なだけで" +
            		"OSに影響のあるようなエラーではありません。\n" +
            		"お手数ですがフレームが表示されたら一度フレームを" +
        			"閉じてからもう一度起動してみてください。");
            }
            
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            
            // Exampleの内容でテキストを変更する際に文字列の表示に
            // 必要な分だけ横幅が大きくなってしまう為、最大サイズを
            // 教えておく。
            regexField.setMaxWidth(regexField.getWidth());
            replacementField.setMaxWidth(replacementField.getWidth());
            
            return frame; 
        }



        // 宣言時に生成されていないコンポーネントをすべて作成
        private void createComponent() {
            Color textBG = Main.getColorProperty("text.color.bg");
            Color textFG = Main.getColorProperty("text.color.fg");
            Color caretColor = Main.getColorProperty("text.color.caret");
            Color endLineColor = Main.getColorProperty("text.color.endLine");
            Color spaceColor = Main.getColorProperty("text.color.whitespace");
            Font textareaFont = Main.getFontProperty("textarea.font");
            Font textfieldFont = Main.getFontProperty("textfield.font");
            int tabCount = Main.getIntProperty("text.int.tab.count");

            WhitespaceVisibleAreaInfo sourceAreaInfo =
                new WhitespaceVisibleAreaInfo(textBG, textFG, caretColor, endLineColor,
                                   spaceColor, textareaFont, tabCount);

            sourceArea = new WhitespaceVisibleArea(sourceAreaInfo);

            ReplacementAreaInfo replacementAreaInfo =
                new ReplacementAreaInfo(textBG, textFG, endLineColor,
                                        spaceColor, textareaFont, tabCount);

            replacementArea = new ReplacementArea(replacementAreaInfo);

            WhitespaceVisibleFieldInfo regexFieldInfo =
                new WhitespaceVisibleFieldInfo(textBG, textFG, caretColor,
                                   spaceColor, textfieldFont);
            regexField = new WhitespaceVisibleField(regexFieldInfo);
            replacementField = new ReplacementField(regexFieldInfo);

            explanationArea = new ExplanationArea(hyperlinkListener);

            backButton = new JButton(Main.getStringProperty(
                                "component.string.back.button.label"),
                                Main.getImageIcon("triangle_left.gif"));
            nextButton = new JButton(Main.getStringProperty(
                                "component.string.next.button.label"),
                                Main.getImageIcon("triangle_right.gif"));
            backButton.setEnabled(false);
            nextButton.setEnabled(false);
            clearTutorialButton.setEnabled(false);

            // PatternのメソッドのJList
            Set<String> patternKeys = patternMap.keySet();
            String[] patternMethods = patternKeys.toArray(
                                          new String[patternKeys.size()]);
            patternList = new JList(patternMethods);
            patternList.setSelectionMode(
                                     ListSelectionModel.SINGLE_SELECTION);

            // MatcherのメソッドのJList
            Set<String> matcherKeys = matcherMap.keySet();
            String[] matcherMethods = matcherKeys.toArray(
                                          new String[matcherKeys.size()]);
            matcherList = new JList(matcherMethods);
            matcherList.setSelectionMode(
                                     ListSelectionModel.SINGLE_SELECTION);

            // モードのチェックボックスを作成してmodeCBMapに格納
            String[] labels = new String[] {
                "MULTILINE", "DOTALL", "COMMENTS",
                "LITERAL", "UNIX_LINES","UNICODE_CASE",
                "CASE_INSENSITIVE", "CANON_EQ"
            };
            int[] flagValues = new int[] {
                Pattern.MULTILINE, Pattern.DOTALL, Pattern.COMMENTS,
                Pattern.LITERAL, Pattern.UNIX_LINES, Pattern.UNICODE_CASE,
                Pattern.CASE_INSENSITIVE, Pattern.CANON_EQ
            };
            for (int i = 0; i < labels.length; i++) {
                modeCBMap.put(flagValues[i], new JCheckBox(labels[i]));
            }

            // sourceAreaとreplacementAreaとexplanationArea の高さを調整
            adjustCenterComps();
        }

        // sourceAreaとreplacementAreaとexplanationAreaの初期状態の
        // preferredSizeを同じにする。
        // explanationAreaの初期サイズが他の２つとは違う為
        private void adjustCenterComps() {
            Dimension sourceD = sourceArea.getPreferredSize();
            Dimension replacementD = replacementArea.getPreferredSize();
            Dimension explanationD = explanationArea.getPreferredSize();
            int maxHeight = Math.max(sourceD.height, replacementD.height);
            maxHeight = Math.max(maxHeight, explanationD.height);
            sourceD.height = maxHeight;
            replacementD.height = maxHeight;
            explanationD.height = maxHeight;
            sourceArea.setPreferredSize(sourceD);
            replacementArea.setPreferredSize(replacementD);
            explanationArea.setPreferredSize(explanationD);
        }

        // pack後のsourceAreaとreplacementAreaとexplanationAreaの
        // のサイズをpreferredSizeに設定しておく。
        // この処理は画面を最大化した後にもとのサイズに戻す際に
        // pack時のサイズに正しく戻す為に必要。
        // またexplanationAreaのJEditorPaneの返す推奨サイズは
        // 通常のコンポーネントとは違う為、レイアウトを
        // 再構築する際にexplanationAreaのサイズがJEditorPaneの
        // サイズに合わせて変更される場合があるので
        // コンテナであるJScrollPaneのpreferredSizeを設定しておく事で
        // この現象を避けられる。
        private void adjustCenterCompsAfterPacking() {
            Dimension SourceD = new Dimension(sourceArea.getWidth(),
                                              sourceArea.getHeight());
            sourceArea.setMinimumSize(SourceD);
            sourceArea.setPreferredSize(SourceD);

            Dimension replacementD = new Dimension(
                                         replacementArea.getWidth(),
                                         replacementArea.getHeight());
            replacementArea.setMinimumSize(replacementD);
            replacementArea.setPreferredSize(replacementD);

            Dimension explanationD = new Dimension(
                                         explanationArea.getWidth(),
                                         explanationArea.getHeight());
            explanationArea.setMinimumSize(explanationD);
            explanationArea.setPreferredSize(explanationD);
        }
        
        // 横幅を必要な分確保し、高さは実際には2000以上必要になるが
        // 最後の pack でそんなに大きくなっては困るので0に設定しておく
        // 無意味だと思うが推奨の高さを 0 ではなく 1 にしてみる。
        private void adjustLeftPanelAfterPacking(JTabbedPane tabPane) {
        	
        	tabPane.setPreferredSize(new Dimension(tabPane.getWidth(), 1));
        	
        	// 上の設定だけではまれにフレームの高さが2000以上に
        	// なる事があるのでタブペイン内のスクロールペインの
            // 推奨サイズも設定しておく
        	int count = tabPane.getTabCount();
        	int leftWidth = 0;
        	for (int i = 0; i < count; i++) {
        		JComponent comp = (JComponent)tabPane.getComponentAt(i);
        		leftWidth = Math.max(leftWidth, 
        		                     comp.getPreferredSize().width);
        	}
        	for (int i = 0; i < count; i++) {
        		JComponent comp = (JComponent)tabPane.getComponentAt(i);
        		comp.setPreferredSize(new Dimension(leftWidth, 1));
        	}
        	
        }
        
        // まとめてリスナ登録
        private void addListener() {
            ActionListener actionEventHandler = new ActionEventHandler();
            newlineModeComboBox.addActionListener(actionEventHandler);
            groupComboBox.addActionListener(actionEventHandler);
            resetButton.addActionListener(actionEventHandler);
            findButton.addActionListener(actionEventHandler);
            findIntButton.addActionListener(actionEventHandler);
            lookingAtButton.addActionListener(actionEventHandler);
            matchesButton.addActionListener(actionEventHandler);
            appendReplacementButton.addActionListener(actionEventHandler);
            appendTailButton.addActionListener(actionEventHandler);
            replaceAllButton.addActionListener(actionEventHandler);
            replaceFirstButton.addActionListener(actionEventHandler);
            sourceClearButton.addActionListener(actionEventHandler);
            replacedBufClearButton.addActionListener(actionEventHandler);
            regionButton.addActionListener(actionEventHandler);
            anchoringRB.addActionListener(actionEventHandler);
            nonAnchoringRB.addActionListener(actionEventHandler);
            tranparentRB.addActionListener(actionEventHandler);
            opaqueRB.addActionListener(actionEventHandler);
            backButton.addActionListener(actionEventHandler);
            nextButton.addActionListener(actionEventHandler);
            clearTutorialButton.addActionListener(actionEventHandler);

            ListSelectionListener l = new ListSelectionEventHandler();
            patternList.addListSelectionListener(l);
            matcherList.addListSelectionListener(l);
        }

        private JComponent addCover(JComponent comp) {
            CoveredPane coveredPane = new CoveredPane();
            coveredPane.setTarget(comp);
            coverList.add(coveredPane);
            return coveredPane;
        }


        private JPanel createLeftPanel(JTabbedPane leftTab) {

            JScrollPane patternSP = new JScrollPane(patternList,
                                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            leftTab.add(Main.getStringProperty(
                        "component.string.tabpane.pattern.label"), 
                        patternSP);

            JScrollPane matcherSP = new JScrollPane(matcherList,
                                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            leftTab.add(Main.getStringProperty(
                        "component.string.tabpane.matcher.label"), 
                        matcherSP);

            StringBuilder sb = new StringBuilder(64);
            sb.append("bgcolor=\"");
            sb.append(Main.color2HexString(patternList.getBackground()));
            sb.append("\" text=\"");
            sb.append(Main.color2HexString(patternList.getForeground()));
            sb.append("\" ");
            sb.append("style=\"font-family: ");
            sb.append(Main.getStringProperty(
                         "lefteditorpane.string.html.font.family"));
            sb.append("; font-size: ");
            sb.append(Main.getStringProperty(
                         "lefteditorpane.string.html.font.size"));
            sb.append(";\"");
            String styleAttr = sb.toString();
            
            JScrollPane metaCharSP = new JScrollPane(
                                 createMetaCharList(styleAttr),
                                 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            leftTab.add(Main.getStringProperty(
                        "component.string.tabpane.metachar.label"), 
                        metaCharSP);

            JScrollPane charclassesSP = new JScrollPane(
                                createCharClassesList(styleAttr),
                                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            leftTab.add(Main.getStringProperty(
                        "component.string.tabpane.charclass.label"), 
                        charclassesSP);

            JScrollPane tutorialSP = new JScrollPane(
                             createTutorialList(styleAttr),
                             JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                             JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            leftTab.add(Main.getStringProperty(
                        "component.string.tabpane.tutorial.label"), 
                        tutorialSP);

            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(5, 5, 5, 5);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1; c.weighty = 1;
            leftPanel.add(leftTab, c);

            return leftPanel;
        }

        private JPanel createCenterPanel() {

            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            int horMargin = 10;

            c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(15, horMargin + 2, 3, 0);
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.EAST;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel(
                Main.getStringProperty(
                    "component.string.regex.field.label")), c);

            c.gridx = 1; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(15, 0, 3, 5);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.WEST;
            c.weightx = 0.1; c.weighty = 0;
            panel.add(addCover(regexField), c);

            Box box = new Box(BoxLayout.X_AXIS);
            box.add(sourceClearButton);
            box.add(Box.createHorizontalStrut(BUTTON_MARGIN));
            box.add(newlineModeComboBox);

            c.gridx = 2; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(15, 5, 3, horMargin + 1);
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.EAST;
            c.weightx = 0; c.weighty = 0;
            panel.add(addCover(box), c);

            c.gridx = 0; c.gridy = 1; c.gridwidth = 3; c.gridheight = 1;
            c.insets = new Insets(0, horMargin, 3, horMargin);
            c.fill = GridBagConstraints.BOTH;
            c.anchor = GridBagConstraints.CENTER;
            c.weightx = 0.1; c.weighty = 0.1;
            panel.add(addCover(sourceArea), c);

            c.gridx = 0; c.gridy = 2; c.gridwidth = 3; c.gridheight = 1;
            c.insets = new Insets(0, horMargin, 5, horMargin);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.1; c.weighty = 0;
            panel.add(addCover(createMatchButtonsComp()), c);

            c.gridx = 0; c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(15, horMargin + 2, 3, 0);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel(
                      Main.getStringProperty(
                          "component.string.replacement.field.label")), c);

            c.gridx = 1;c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(15, 0, 3, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.1; c.weighty = 0;
            panel.add(addCover(replacementField), c);

            c.gridx = 2; c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(15, 5, 3, horMargin + 1);
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.WEST;
            c.weightx = 0; c.weighty = 0;
            panel.add(addCover(replacedBufClearButton), c);

            c.gridx = 0; c.gridy = 4; c.gridwidth = 3; c.gridheight = 1;
            c.insets = new Insets(0, horMargin, 3, horMargin);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 0.1; c.weighty = 0.1;
            panel.add(addCover(replacementArea), c);

            c.gridx = 0; c.gridy = 5; c.gridwidth = 3; c.gridheight = 1;
            c.insets = new Insets(0, horMargin, 5, horMargin);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.1; c.weighty = 0;
            panel.add(addCover(createReplacedButtonsComp()), c);

            c.gridx = 0; c.gridy = 6; c.gridwidth = 3; c.gridheight = 1;
            c.insets = new Insets(15, horMargin, 3, horMargin);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 0.1; c.weighty = 0.2;
            panel.add(explanationArea, c);

            c.gridx = 0; c.gridy = 7; c.gridwidth = 3; c.gridheight = 1;
            c.insets = new Insets(0, horMargin, 5, horMargin);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.1; c.weighty = 0;
            panel.add(createExplanationButtonsComp(), c);

            return panel;
        }

        private JComponent createMatchButtonsComp() {

            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            int margin = 5;

            c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, margin);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0.1; c.weighty = 0;
            panel.add(resetButton, c);

            c.gridx = 1; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, margin);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(lookingAtButton, c);

            c.gridx = 2; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, margin);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(matchesButton, c);

            c.gridx = 3; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, margin);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(findButton, c);

            c.gridx = 4;c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 0);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(findIntButton, c);

            c.gridx = 5; c.gridy = 0; c.gridwidth = 1;  c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 0);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel(" ( "), c);

            c.gridx = 6; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 0);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.VERTICAL;
            c.weightx = 0; c.weighty = 0;
            panel.add(findTF, c);

            c.gridx = 7; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 1);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel(" )"), c);

            return panel;
        }

        private JComponent createReplacedButtonsComp() {
            int margin = 5;

            Box box = new Box(BoxLayout.X_AXIS);
            box.add(Box.createHorizontalGlue());
            box.add(appendReplacementButton);
            box.add(Box.createHorizontalStrut(margin));
            box.add(appendTailButton);
            box.add(Box.createHorizontalStrut(margin));
            box.add(replaceFirstButton);
            box.add(Box.createHorizontalStrut(margin));
            box.add(replaceAllButton);
            box.add(Box.createHorizontalStrut(1));

            return box;
        }

        private JComponent createExplanationButtonsComp() {
            int margin = 5;

            Box box = new Box(BoxLayout.X_AXIS);

            /* CoveredPaneのテスト
            JButton coverButton = new JButton("cover");
            coverButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    setClearSampleButtonEnabled(true);
                }
            });
            JButton uncoverButton = new JButton("uncover");
            uncoverButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    setClearSampleButtonEnabled(false);
                }
            });
            box.add(coverButton);
            box.add(uncoverButton);
            */

            box.add(Box.createHorizontalGlue());
            box.add(backButton);
            box.add(Box.createHorizontalStrut(margin));
            box.add(nextButton);
            box.add(Box.createHorizontalStrut(margin));
            box.add(clearTutorialButton);
            box.add(Box.createHorizontalStrut(1));

            return box;
        }

        private JPanel createRightPanel() {
        	JPanel rightPanel = new JPanel();
        	rightPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(5, 5, 5, 5);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 0; c.weighty = 0;
            rightPanel.add(createStateComp(), c);

            c.gridx = 0; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 5, 5, 5);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 0; c.weighty = 0;
            rightPanel.add(createModeComp(), c);

            c.gridx = 0; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 5, 5, 5);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 0; c.weighty = 0;
            rightPanel.add(createRegionComp(), c);

            //余白を埋めるように TrademarkPanel を貼り付ける。
            String imageIconFile = Main.getStringProperty(
                                   "trademarkpanel.string.imagefile");
            TrademarkPanel trademarkPanel = new TrademarkPanel(
            		Main.getImageIcon(imageIconFile),
            		Main.getStringProperty("trademarkpanel.string.message"), 
            		10, 20, 180000);
            c.gridx = 0; c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 0);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 0; c.weighty = 0.1;
            rightPanel.add(trademarkPanel, c);

            return rightPanel;
        }

        //境界の設定
        private JComponent createRegionComp() {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            Box regionBox = new Box(BoxLayout.X_AXIS);
            regionBox.add(regionButton);
            regionBox.add(new JLabel(" ( "));
            regionBox.add(regionStartTF);
            regionBox.add(new JLabel(", "));
            regionBox.add(regionEndTF);
            regionBox.add(new JLabel(" )"));

            c.gridx = 0; c.gridy = 0; c.gridwidth = 5; c.gridheight = 1;
            c.insets = new Insets(5, 5, 0, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(regionBox, c);

            c.gridx = 0; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 0);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(Box.createHorizontalStrut(30), c);

            c.gridx = 1; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 0);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel("regionStart"), c);

            c.gridx = 2; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 0);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel(" :　"), c);

            c.gridx = 3; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 0);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(regionStartLabel, c);

            c.gridx = 1; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 0);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel("regionEnd"), c);

            c.gridx = 2; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 0);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel(" :　"), c);

            c.gridx = 3; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 0);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(regionEndLabel, c);

            ButtonGroup buttonGroup1 = new ButtonGroup();
            buttonGroup1.add(anchoringRB);
            buttonGroup1.add(nonAnchoringRB);
            anchoringRB.setSelected(true);

            c.gridx = 0; c.gridy = 3; c.gridwidth = 5; c.gridheight = 1;
            c.insets = new Insets(5, 5, 0, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel("anchoringBounds"), c);

            c.gridx = 1; c.gridy = 4; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 0);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(anchoringRB, c);

            c.gridx = 2; c.gridy = 4; c.gridwidth = 3; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            c.gridwidth = 2;
            panel.add(nonAnchoringRB, c);

            ButtonGroup buttonGroup2 = new ButtonGroup();
            buttonGroup2.add(tranparentRB);
            buttonGroup2.add(opaqueRB);
            opaqueRB.setSelected(true);

            c.gridx = 0; c.gridy = 5; c.gridwidth = 5; c.gridheight = 1;
            c.insets = new Insets(5, 5, 0, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel("transparentBounds"), c);

            c.gridx = 1; c.gridy = 6; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 0);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(tranparentRB, c);

            c.gridx = 2; c.gridy = 6; c.gridwidth = 3; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(opaqueRB, c);

            //余計なスペースは右寄せ
            c.gridx = 50; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 0);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 0.1; c.weighty = 0;
            panel.add(Box.createHorizontalStrut(0), c);

            panel.setBorder(BorderFactory.createTitledBorder(
                Main.getStringProperty(
                    "component.string.region.and.bounds.border.title")));
            return panel;
        }

        //Pattern#compile の引数となるモードの設定
        private JComponent createModeComp() {
            modeCompPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 5, 0, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;

            Collection<JCheckBox> modeCBs = modeCBMap.values();
            for (JCheckBox cb : modeCBs) {
                modeCompPanel.add(cb, c);
                c.gridy++;
            }

            modeCompPanel.setBorder(modeCompBorder);
            return modeCompPanel;
        }

        private JComponent createStateComp() {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            String inputCountLabel =
                Main.getStringProperty(
                    "component.string.input.count.label");
            c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 5, 0, 0);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel(inputCountLabel), c);

            c.gridx = 1; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(inputLengthLabel, c);

            c.gridx = 0; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 5, 0, 0);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel("groupCount : "), c);

            c.gridx = 1; c.gridy = 1; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(groupCountLabel, c);

            c.gridx = 0; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 5, 0, 0);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel("requireEnd : "), c);

            c.gridx = 1; c.gridy = 2; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(requireEndLabel, c);

            c.gridx = 0; c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 5, 0, 0);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel("hitEnd : "), c);

            c.gridx = 1; c.gridy = 3; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(hitEndLabel, c);

            c.gridx = 0; c.gridy = 4; c.gridwidth = 2; c.gridheight = 1;
            c.insets = new Insets(5, 5, 5, 5);
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.1; c.weighty = 0;
            panel.add(new JSeparator(), c);

            Box box = Box.createHorizontalBox();
            box.add(new JLabel("group "));
            box.add(groupComboBox);
            c.gridx = 0; c.gridy = 5; c.gridwidth = 2; c.gridheight = 1;
            c.insets = new Insets(0, 5, 0, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(box, c);


            c.gridx = 0; c.gridy = 6; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 5, 0, 0);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel("start : "), c);

            c.gridx = 1; c.gridy = 6; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 0, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(startLabel, c);

            c.gridx = 0; c.gridy = 7; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 5, 5, 0);
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(new JLabel("end : "), c);

            c.gridx = 1; c.gridy = 7; c.gridwidth = 1; c.gridheight = 1;
            c.insets = new Insets(0, 0, 5, 5);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0; c.weighty = 0;
            panel.add(endLabel, c);

            panel.setBorder(BorderFactory.createTitledBorder(
                                Main.getStringProperty(
                                "component.string.state.border.title")));

            return panel;
        }

        private JComponent createMetaCharList(String styleAttr) {
            List<MetaChar> metaChars = Main.getMetaCharList(false);
            JEditorPane editorPane = new JEditorPane();
            editorPane.setBackground(patternList.getBackground());
            editorPane.setEditable(false);
            editorPane.addHyperlinkListener(hyperlinkListener);
            editorPane.setContentType("text/html; charset=UTF8");
            StringBuilder sb = new StringBuilder();
            sb.append("<html><body ");
            sb.append(styleAttr);
            sb.append(">");
            sb.append("<table>");
            StringBuilder linkBuilder = new StringBuilder();
            linkBuilder.append("<font color=\"#");
            linkBuilder.append(LINK_RRGGBB);
            linkBuilder.append("\">");
            linkBuilder.append("<a href=\"");
            linkBuilder.append(LINKED_URL_PRE);
            linkBuilder.append("Example/");
            int initLinkBuilderLength = linkBuilder.length();
            boolean isFirstTitle = true;
            for (MetaChar metaChar : metaChars) {
            	if (metaChar.chars.equals(MetaChar.METACHARS_TITLE)) {
            		sb.append("<tr><td colspan=\"2\" align=\"center\">");
            		if (isFirstTitle) {
            			isFirstTitle = false;
            		} else {
            			sb.append("<hr>");
            		}
            		sb.append("【");
            		sb.append(metaChar.summary);
            		sb.append("】</td></tr>");
            	} else {
            		sb.append("<tr><td align=\"center\">");
                    sb.append(metaChar.chars);
                    sb.append("</td><td align=\"left\">");
                    String example = metaChar.example;
                    String summary;
                    if (example != null) {
                        linkBuilder.setLength(initLinkBuilderLength);
                        linkBuilder.append(example);
                        linkBuilder.append("\">");
                        linkBuilder.append(metaChar.summary);
                        linkBuilder.append("</a></font>");
                        summary = linkBuilder.toString();
                    } else {
                        summary = metaChar.summary;
                    }
                    sb.append("　");
                    sb.append(summary);
                    sb.append("</td></tr>");
            	}
            }

            sb.append("</table></body></html>");
            editorPane.setText(sb.toString());
            return editorPane;
        }

        private JComponent createCharClassesList(String styleAttr) {
            List<MetaChar> charclassesList = Main.getMetaCharList(true);
            JEditorPane editorPane = new JEditorPane();
            editorPane.setBackground(patternList.getBackground());
            editorPane.setEditable(false);
            editorPane.addHyperlinkListener(hyperlinkListener);
            editorPane.setContentType("text/html; charset=UTF8");
            StringBuilder sb = new StringBuilder();
            sb.append("<html><body ");
            sb.append(styleAttr);
            sb.append(">");

            StringBuilder linkBuilder = new StringBuilder();
            linkBuilder.append("<font color=\"#");
            linkBuilder.append(LINK_RRGGBB);
            linkBuilder.append("\">");
            linkBuilder.append("<a href=\"");
            linkBuilder.append(LINKED_URL_PRE);
            linkBuilder.append("Example/");
            
            int initLinkBuilderLength = linkBuilder.length();
            sb.append("<table>");
            boolean isFirst = true;
            for (MetaChar metaChar : charclassesList) {
                String chars = metaChar.chars;
                if (chars.equals("newtable")) {
                    sb.append("</table><div align=\"center\"><b>");
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        sb.append("<br>");
                    }
                    //sb.append(metaChar.summary);
                    String example = metaChar.example;
                    String summary;
                    if (example != null) {
                        linkBuilder.setLength(initLinkBuilderLength);
                        linkBuilder.append(example);
                        linkBuilder.append("\">");
                        linkBuilder.append(metaChar.summary);
                        linkBuilder.append("</a></font>");
                        summary = linkBuilder.toString();
                    } else {
                        summary = metaChar.summary;
                    }
                    sb.append(summary);
                    sb.append("</b></div><table>");
                    continue;
                }
                sb.append("<tr><td align=\"left\">");
                sb.append(chars);
                sb.append("</td><td align=\"left\">");
                String example = metaChar.example;
                String summary;
                if (example != null) {
                    linkBuilder.setLength(initLinkBuilderLength);
                    linkBuilder.append(example);
                    linkBuilder.append("\">");
                    linkBuilder.append(metaChar.summary);
                    linkBuilder.append("</a>");
                    summary = linkBuilder.toString();
                } else {
                    summary = metaChar.summary;
                }
                sb.append(summary);
                sb.append("</td></tr>");
            }

            sb.append("</table></body></html>");
            editorPane.setText(sb.toString());
            return editorPane;
        }

        private JComponent createTutorialList(String styleAttr) {
            List<Tutorial> tutorialList = Main.getTutorialList();
            JEditorPane editorPane = new JEditorPane();
            editorPane.setBackground(patternList.getBackground());
            editorPane.setEditable(false);
            editorPane.addHyperlinkListener(hyperlinkListener);
            editorPane.setContentType("text/html; charset=UTF8");
            StringBuilder sb = new StringBuilder();
            sb.append("<html><body ");
            sb.append(styleAttr);
            sb.append(">");

            String linkPre = "<font color=\"#" + LINK_RRGGBB + 
                             "\"><a href=\"" + LINKED_URL_PRE + "Example/";
            for (Tutorial tutorial : tutorialList) {
                sb.append(linkPre);
                sb.append(tutorial.example);
                sb.append("\">");
                sb.append(tutorial.caption);
                sb.append("</a></font><br>");
            }
            sb.append("</body></html>");
            editorPane.setText(sb.toString());

            return editorPane;
        }
    }



    // メソッドを表す文字定数、matcherMapのキーでもある。
    private static final String FIND_METHOD = "find";
    private static final String FIND_INT_METHOD = "find(int)";
    private static final String MATCHES_METHOD = "matches";
    private static final String LOOKING_AT_METHOD = "lookingAt";
    private static final String APPENDTAIL_METHOD = "appendTail";
    private static final String APPENDREPLACEMENT_METHOD = "appendReplacement";
    private static final String REPLACEALL_METHOD = "replaceAll";
    private static final String REPLACEFIRST_METHOD = "replaceFirst";

    // コンポーネントとイベントの両方で使用
    private static final Object LF = " LF ";
    private static final Object CRLF = "CRLF";

    private static final String LINKED_URL_PRE;
    private static final String HIGHLIGHT_RRGGBB_1;
    private static final String HIGHLIGHT_RRGGBB_2;
    private static final String EXPLAIN_RRGGBB;
    private static final String CODE_RRGGBB;
    private static final String ERROR_RRGGBB;
    private static final String LINK_RRGGBB;
    
    static {
    	LINKED_URL_PRE = Main.LINKED_URL_PRE;
        HIGHLIGHT_RRGGBB_1 = Main.HIGHLIGHT_RRGGBB_1;
    	HIGHLIGHT_RRGGBB_2 = Main.HIGHLIGHT_RRGGBB_2;
    	EXPLAIN_RRGGBB = Main.EXPLAIN_RRGGBB;
    	CODE_RRGGBB = Main.CODE_RRGGBB;
    	ERROR_RRGGBB = Main.ERROR_RRGGBB;
    	LINK_RRGGBB = Main.LINK_RRGGBB;
    }
    
    private static final Color HIGHLIGHT_COLOR =
        Main.getColorProperty("sourcearea.color.highlight");

    private static final Color OUT_OF_REGION_COLOR =
        Main.getColorProperty("sourcearea.color.outofregion");

    private static final String ILLEGAL_LINKED_URL_ERROR_MESSAGE =
        Main.getStringProperty(
                  "appmessage.string.illegal.linked.url.error.message");

    private static final String RESET_MESSAGE =
        Main.getStringProperty("appmessage.string.reset.message");

    private static final String FIND_ERROR_MESSAGE =
        Main.getStringProperty("appmessage.string.find.error.message");

    private static final String REGION_ERROR_MESSAGE =
        Main.getStringProperty("appmessage.string.region.error.message");

    private static final String CLEAR_MESSAGE =
        Main.getStringProperty("appmessage.string.clear.message");

    private static final String MATCHED_MESSAGE =
        Main.getStringProperty("appmessage.string.matched.message");

    private static final String NO_MATCHED_MESSAGE =
        Main.getStringProperty("appmessage.string.no.matched.message");

    private static final String NO_INPUT_ERROR_MESSAGE =
        Main.getStringProperty("appmessage.string.no.input.error.message");

    private static final String PATTERN_SYNTAX_ERROR_MESSAGE =
        Main.getStringProperty(
                         "appmessage.string.pattern.syntax.error.message");

    private static final String SET_REGION_MESSAGE =
        Main.getStringProperty("appmessage.string.set.region.message");

    private static final String APPENDTAIL_MESSAGE =
        Main.getStringProperty("appmessage.string.appendtail.message");

    private static final String GROUP_0_MESSAGE =
        Main.getStringProperty("appmessage.string.group.0.message");

    private static final String GROUP_MESSAGE =
        Main.getStringProperty("appmessage.string.group.message");

    private static final String REPLACEALL_MESSAGE =
        Main.getStringProperty("appmessage.string.replaceall.message");

    private static final String REPLACEFIRST_MESSAGE =
        Main.getStringProperty("appmessage.string.replacefirst.message");

    private static final String REPLACEMENT_ERROR_MESSAGE =
        Main.getStringProperty(
                    "appmessage.string.replacement.error.message");

    private static final String REPLACEMENT_ESCAPE_ERROR_MESSAGE =
        Main.getStringProperty(
                 "appmessage.string.replacement.escape.error.message");

    private static final String REPLACEMENT_QUOT_ERROR_MESSAGE =
        Main.getStringProperty(
            "appmessage.string.replacement.quot.error.message");

    private static final String APPENDREPLACEMENT_NO_MATCHED_ERROR_MESSAGE =
        Main.getStringProperty(
            "appmessage.string.appendreplacement.no.matched.error.message");

    private static final String APPENDREPLACEMENT_MESSAGE =
        Main.getStringProperty("appmessage.string.appendreplacement.message");

    private static final String ILLEGAL_APPEND_POSITION_ERROR_MESSAGE =
        Main.getStringProperty(
            "appmessage.string.illegal.append.position.error.message");
}
