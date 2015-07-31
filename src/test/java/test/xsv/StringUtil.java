package test.xsv;

/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

//package org.opentides.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {

	/**
	 * Parse a line of text in CSV format and returns array of Strings
	 * Implementation of parsing is extracted from open-csv.
	 * http://opencsv.sourceforge.net/
	 *
	 * @param csvLine
	 * @param separator
	 * @param quotechar
	 * @param escape
	 * @param strictQuotes
	 * @return
	 * @throws IOException
	 */
	public static List<String> parseCsvLine(String csvLine, char separator,
			char quotechar, char escape, boolean strictQuotes) {
		List<String> tokensOnThisLine = new ArrayList<String>();
		StringBuilder sb = new StringBuilder(50);
		boolean inQuotes = false;
		for (int i = 0; i < csvLine.length(); i++) {
			char c = csvLine.charAt(i);
			if (c == escape) {
				boolean isNextCharEscapable = inQuotes
						&& csvLine.length() > (i + 1)
						&& (csvLine.charAt(i + 1) == quotechar || csvLine
								.charAt(i + 1) == escape);
				if (isNextCharEscapable) {
					sb.append(csvLine.charAt(i + 1));
					i++;
				}
			} else if (c == quotechar) {
				boolean isNextCharEscapedQuote = inQuotes
						&& csvLine.length() > (i + 1)
						&& csvLine.charAt(i + 1) == quotechar;
				if (isNextCharEscapedQuote) {
					sb.append(csvLine.charAt(i + 1));
					i++;
				} else {
					inQuotes = !inQuotes;
					if (!strictQuotes) {
						if (i > 2 && csvLine.charAt(i - 1) != separator
								&& csvLine.length() > (i + 1)
								&& csvLine.charAt(i + 1) != separator) {
							sb.append(c);
						}
					}
				}
			} else if (c == separator && !inQuotes) {
				tokensOnThisLine.add(sb.toString());
				sb = new StringBuilder(50);
			} else {
				if (!strictQuotes || inQuotes)
					sb.append(c);
			}
		}
		if (inQuotes) {
			// _log.warn("Un-terminated quoted field at end of CSV line. \n ["+csvLine+"]");
		}
		if (sb != null) {
			tokensOnThisLine.add(sb.toString());
		}
		return tokensOnThisLine;
	}
}