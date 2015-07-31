package test.xsv;

/*
 * Copyright (c) 2008-2011 Simon Ritchie.
 * All rights reserved. 
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program.  If not, see http://www.gnu.org/licenses/>.
 */
//package org.rimudb.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * @author Simon Ritchie
 * 
 */
public class TokenizerUtil {

    public static String[] convertCSVStringToArray(String s) {
        List<String> list = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(s, "|");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            list.add(token);
        }
        return list.toArray(new String[list.size()]);
    }

    public static String convertArrayToCSVString(String[] s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length; i++) {
            if (i > 0) {
                sb.append("|");
            }
            sb.append(s[i]);
        }
        return sb.toString();
    }

    public static String convertMapToCSVString(Map<String, String> urlDefaultMap) {
        StringBuffer sb = new StringBuffer();
        Iterator<String> it = urlDefaultMap.keySet().iterator();
        while (it.hasNext()) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            String jdbcDriver = it.next();
            String defaultURL = urlDefaultMap.get(jdbcDriver);
            sb.append(jdbcDriver + "|" + defaultURL);
        }
        return sb.toString();
    }

    public static Map<String, String> convertCSVStringToMap(String s) {
        TreeMap<String, String> map = new TreeMap<String, String>();
        StringTokenizer st = new StringTokenizer(s, "|");
        while (st.hasMoreTokens()) {
            String keyToken = st.nextToken();
            String valueToken = st.nextToken();
            map.put(keyToken, valueToken);
        }
        return map;
    }
}