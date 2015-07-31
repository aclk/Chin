package spiritstoolkit.dbsettings.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import spiritstoolkit.dbsettings.data.HistoryData;
import spiritstoolkit.dbsettings.output.ConsoleUnit;

public class HistoryUtil {

    private static HistoryUtil me;

    private static final String HISTORY_FILENAME = "c:/db.history";

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

    public void addHistory(String jdbcUrl, String user, String password,
        String schema) throws Exception {
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
            reader =
                new BufferedReader(new FileReader(
                    new File(getHistoryFilePath())));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(new HistoryData(line));
            }
        } catch (Exception e) {
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (Exception e) {
    			ConsoleUnit console = new ConsoleUnit();
    			console.outPutStream(e.getMessage());
            }
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
			ConsoleUnit console = new ConsoleUnit();
			console.outPutStream(e.getMessage());
            throw e;
        } finally {
            if (pw != null) pw.close();
        }
    }

    private String getHistoryFilePath() throws Exception {
        return HISTORY_FILENAME;
    }
}
