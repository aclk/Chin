package main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

/**
 * �w�肵��URL����ABufferedReader���擾����B
 * �����A���ꂾ���B
 *
 * @author H.Uchimoto
 * @version $Id$
 * @since JDK5.0
 */
public class GetHtmlSource {

    private String charset = "UTF-8";

    private BufferedReader bufferedReader;

    // �R���X�g���N�^
    public GetHtmlSource(URL url) {

        try {
            // �ڑ�
            URLConnection uc = url.openConnection();
            // HTML��ǂݍ���
            BufferedInputStream bis =
                new BufferedInputStream(uc.getInputStream());
            bufferedReader =
                new BufferedReader(new InputStreamReader(bis, charset));

        } catch (MalformedURLException ex) {
            System.out.println("URL���s���ł��B");
            ex.printStackTrace();
        } catch (UnknownHostException ex) {
            System.out.println("�T�C�g��������܂���B");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void closeBufferedReader() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
}