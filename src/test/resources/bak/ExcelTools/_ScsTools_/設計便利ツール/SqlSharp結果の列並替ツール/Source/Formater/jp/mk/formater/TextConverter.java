package jp.mk.formater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class TextConverter {

	private String column;
	private String dataText;


	@SuppressWarnings("unchecked")
	public String format() {

		// カラム分解
		ArrayList<String> columnList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(column , " ,\t");
		while (st.hasMoreTokens()) {
			String columnName = st.nextToken().trim();
			if (columnName.length() > 0) columnList.add(columnName);
		}

		// データ分解

		ArrayList<HashMap<String, String>> lines = new ArrayList<HashMap<String, String>>();

		StringTokenizer rowSt = new StringTokenizer(dataText , "\n");
		while (rowSt.hasMoreTokens()) {

			HashMap<String, String> colMap = new HashMap<String, String>();

			// 両脇の括弧を取り去る
			String data = rowSt.nextToken();
			data = data.substring(1, data.length() - 1);

			StringTokenizer colSt = new StringTokenizer(data , ",");
			while (colSt.hasMoreTokens()) {
				String line = colSt.nextToken().trim();
				int sepPos = line.indexOf("=");
				if (sepPos != -1){
					String key = line.substring(0 , sepPos);
					String value = line.substring(sepPos + 1);

					colMap.put(key, value);

				}
			}

			lines.add(colMap);

		}

		// 全行まわしながら、列をカラムにしたがって出力
		StringBuilder allSb = new StringBuilder();

		// ヘッダ作成
		for (Iterator iterator = columnList.iterator(); iterator.hasNext();) {
			String column = (String) iterator.next();
			if (allSb.length() > 0) allSb.append("\t");
			allSb.append(column);
		}

		// データ部
		for (Iterator iter = lines.iterator(); iter.hasNext();) {

			HashMap<String, String> colMap = (HashMap<String, String>) iter.next();

			StringBuilder sb = new StringBuilder();

			boolean isFirst = true;
			for (Iterator iterator = columnList.iterator(); iterator.hasNext();) {

				String column = (String) iterator.next();
				if (! isFirst) {
					sb.append("\t");
				} else {
					isFirst = false;
				}
				if (colMap.containsKey(column)) {
					sb.append(colMap.get(column));
				}


			}

			if (allSb.length() > 0) allSb.append("\n");

			allSb.append(sb.toString());

		}

		return allSb.toString();
	}

	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getDataText() {
		return dataText;
	}
	public void setDataText(String dataText) {
		this.dataText = dataText;
	}

}
