/**
 *@author ZJ,2007-11-3
 *
 */
package com.log.main;

import java.awt.Component;

import javax.swing.JTabbedPane;

import org.apache.log4j.spi.LoggingEvent;

@SuppressWarnings("serial")
public class Log4JMonitor extends JTabbedPane {
	private JLogList defaultLogArea;

	public Log4JMonitor() {
		super(JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	public JLogList addLogArea(String title, String loggerName,
			boolean isDefault) {
		JLogList logArea = new JLogList(title);
		logArea.addLogger(loggerName, !isDefault);
		addTab(title, logArea);

		if (isDefault)
			defaultLogArea = logArea;
		return logArea;
	}

	public void logEvent(Object msg) {
		if (msg instanceof LoggingEvent) {
			LoggingEvent event = (LoggingEvent) msg;
			String loggerName = event.getLoggerName();
			for (int c = 0; c < getTabCount(); c++) {
				Component tabComponent = getComponentAt(c);
				if (tabComponent instanceof JLogList) {
					JLogList logArea = (JLogList) tabComponent;
					if (logArea.monitors(loggerName)) {
						logArea.addLine(msg);
					}
				}
			}
		} else if (defaultLogArea != null) {
			defaultLogArea.addLine(msg);
		}
	}

	public boolean hasLogArea(String loggerName) {
		for (int c = 0; c < getTabCount(); c++) {
			Component tabComponent = getComponentAt(c);
			if (tabComponent instanceof JLogList) {
				JLogList logArea = (JLogList) tabComponent;
				if (logArea.monitors(loggerName)) {
					return true;
				}
			}
		}
		return false;
	}
}
