package util;

import java.io.*;
import java.net.*;
import java.util.*;
import data.*;

public class HistoryUtil {
	private static HistoryUtil me;
	private static final String HISTORY_FILENAME = "db.history";
	
	private int historyCount;
	
	private HistoryUtil(int historyCount) {
		this.historyCount = historyCount;
	}
	public static HistoryUtil getInstance(int historyCount) {
		if (me == null) {
			me = new HistoryUtil(historyCount);
		}
		return me;
	}
	
	public void addHistory(String jdbcUrl, String user, String password, String schema) throws Exception {
		HistoryData addData = new HistoryData(jdbcUrl, user, password, schema);
		
		List<HistoryData> list = loadHistory();
		list.remove(addData);
		List<HistoryData> tmpList = new ArrayList<HistoryData>();
		tmpList.add(addData);
		if (!list.isEmpty()) {
			if (list.size() < historyCount) {
				tmpList.addAll(list);
			} else {
				tmpList.addAll(list.subList(0, historyCount - 1));
			}
		}
		
		saveHistory(tmpList);
	}
	
	public List<HistoryData> loadHistory() {
		BufferedReader reader = null;
		List<HistoryData> list = new ArrayList<HistoryData>();
		try {
			reader = new BufferedReader(new FileReader(new File(getHistoryFilePath())));
            String line;
            while((line = reader.readLine()) != null) {
            	list.add(new HistoryData(line));
            }
		} catch (Exception e) {
		} finally {
			if (reader != null) try {reader.close();}catch(Exception e){}
		}
		if (list.size() > historyCount) {
			return list.subList(0, historyCount);
		} else {
			return list;
		}
	}
	
	void saveHistory(List<HistoryData> list) throws Exception {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File(getHistoryFilePath()));
			for (HistoryData data : list) {
				pw.println(data.getHistoryData());
			}
			pw.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (pw != null) pw.close();
		}
	}
	private String getHistoryFilePath() throws Exception {
		URL url = ClassLoader.getSystemClassLoader().getResource(System.getProperty("db.config","dbSetting")+".properties");
		File f = null;
		f = new File(url.toURI());
		return f.getParent() + "/" + HISTORY_FILENAME;
	}
}
