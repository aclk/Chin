package demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class ConvertEncoding {
    public static void main(String[] args) {
//        try {
//            FileInputStream fis = new FileInputStream(args[0]);
//            byte[] contents = new byte[fis.available()];
//            fis.read(contents, 0, contents.length);
//            String asString = new String(contents, "ISO8859_1");
//            byte[] newBytes = asString.getBytes("UTF8");
//            FileOutputStream fos = new FileOutputStream(args[1]);
//            fos.write(newBytes);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        
//        Set hs1 = new HashSet();  //(1)オブジェクト生成
//
//        for (int i = 0; i < 10; i++) {
//          hs1.add(new Integer(i));  //(2)要素の追加
//        }
//        System.out.println("削除前" + hs1);  //(3)要素の表示
//
//        for (Iterator i = hs1.iterator(); i.hasNext();) {  //(4)Iterator処理
//          i.next();  //(5)次の要素の呼び出し
//          i.remove();  //(6)要素の削除
//        }
//        System.out.println("削除後" + hs1);  //(7)要素の表示
        
        
//        List<Integer> ll = new ArrayList<Integer>();//(1)オブジェクト生成
//        for (int i = 0; i < 10; i++) {
//            ll.add(i);// (2)要素の追加
//        }
//        System.out.println("削除前" + ll); // (3)要素の表示
//        for (Iterator<Integer> i = ll.iterator(); i.hasNext();) {// (4)Iterator処理
//            i.next(); // (5)次の要素の呼び出し
//            i.remove(); // (6)要素の削除
//        }
//        System.out.println("削除後" + ll); // (7)要素の表示
        
//        List<String> ll = new ArrayList<String>();//(1)オブジェクト生成
//        // バッファーインスタンスを生成する
//        ByteBuffer bb = ByteBuffer.allocate(16);
//        // バッファーへ書き込む
//        bb.put((byte) 0xaa);
//        // バッファーへ書き込む
//        bb.put((byte) 0xab);
//        bb.put((byte) 0xac);
//        bb.put((byte) 0xad);
//        bb.put((byte) 0xae);
//        // 読み込み状態に変更する
//        bb.flip();
//        // バッファーから読み取る
//        byte data = bb.get(); // dataの値は0xaa
//        ll.add(new String(new byte[]{data}));
//        // 追加書き込みできる状態にする
//        bb.compact();
//        // バッファーへ書き込む
//        bb.put((byte) 0xaa);
//        // 読み込み状態に変更する
//        bb.flip();
//        // バッファーから読み取る
//        while (bb.hasRemaining()) {
//            data = bb.get();
//            ll.add(new String(new byte[]{data}));
//        }
//        // バッファーをクリアする
//        bb.clear();
//        System.out.println("要素" + ll); // 要素の表示
        
        try {
            readFile("/tmp/test.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFile(String path) throws IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(path, "rw");
            FileChannel inChannel = raf.getChannel();
            // create buffer with capacity of 48 bytes
            ByteBuffer buf = ByteBuffer.allocate(48);
            // read into buffer.
            while ((inChannel.read(buf)) != -1) {
                buf.flip(); // make buffer ready for read
                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get()); // read 1 byte at a time
                }
                buf.clear(); // make buffer ready for writing
            }
        } finally {
            raf.close();
        }
    }
}
