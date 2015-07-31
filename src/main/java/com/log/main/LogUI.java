package com.log.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.log4j.Level;

import com.log.one.LogTestOne;
import com.log.two.LogTestTwo;

public class LogUI {
    private static LogUI instance;

    private static JFrame frame;

    private Log4JMonitor logMonitor;

    private static List<Object> logCache = new ArrayList<Object>();

    public LogUI() {
        instance = this;
        frame = new JFrame("LogUI ");
    }

    // /////////////////////UI
    private void buildUI() {
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

        });
        frame.getContentPane().add(buildContentPanel(), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(300, 100);
    }

    private Component buildContentPanel() {
        JSplitPane contentSplit = new JSplitPane();

        contentSplit.setTopComponent(new JTextArea("The Main Text"));
        contentSplit.setBottomComponent(buildLogPanel());
        contentSplit.setDividerLocation(550);
        contentSplit.setResizeWeight(1);
        return contentSplit;
    }

    private Component buildLogPanel() {
        logMonitor = new Log4JMonitor();
        logMonitor.addLogArea("log one", "com.log.one", true).setLevel(
                Level.DEBUG);
        logMonitor.addLogArea("log two", "com.log.two", true).setLevel(
                Level.DEBUG);

        for (Object message : logCache) {
            logMonitor.logEvent(message);
        }
        return logMonitor;
    }

    public void show() {
        buildUI();
        frame.setVisible(true);
    }

    // //////////////////Log
    public static synchronized void log(final Object msg) {
        if (instance == null || instance.logMonitor == null) {
            logCache.add(msg);
            return;
        }

        if (SwingUtilities.isEventDispatchThread()) {
            instance.logMonitor.logEvent(msg);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    instance.logMonitor.logEvent(msg);
                }
            });
        }
    }

    // ////////////////Test Cases
    public void doTests() {
        LogTestOne one = new LogTestOne();
        one.doLog();
        LogTestTwo two = new LogTestTwo();
        two.doLog();
    }

    public static void main(String[] args) throws Exception {
        LogUI logUi = new LogUI();
        logUi.show();
        logUi.doTests();
    }
}
