/*
 * Copyright 2014 NAKANO Hideo.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package hello;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.annotation.Transformer;

@MessageEndpoint
public class Scraper {

    private static Logger LOG = LoggerFactory.getLogger(Scraper.class);

    private final Pattern patter = Pattern
            .compile("^<li>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} \\S+");

    @Splitter(inputChannel = "scrapeChl", outputChannel = "filterChl")
    public List<Element> scrape(ResponseEntity<String> payload) {
        String html = payload.getBody();
        final Document htmlDoc = Jsoup.parse(html);
        final Elements anchorNodes = htmlDoc.select("body").select("ul")
                .select("li");

        final List<Element> anchorList = new ArrayList<Element>();
        anchorNodes.traverse(new NodeVisitor() {

            public void head(Node node, int depth) {
                if (node instanceof org.jsoup.nodes.Element) {
                    Element e = (Element) node;
                    anchorList.add(e);
                }
            }

            public void tail(Node node, int depth) {
            }
        });

        return anchorList;
    }

    @Filter(inputChannel = "filterChl", outputChannel = "convertChl")
    public boolean filter(Element payload) {
        Matcher m = patter.matcher(payload.toString());
        return m.find();
    }

    @Transformer(inputChannel = "convertChl", outputChannel = "outChl")
    public DumpEntry convert(Element payload) throws ParseException {
        LOG.info(payload.toString());
        String id = "";
        String ref = null;
        String status = "";
        String dateStr = payload.ownText().substring(0, 19);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date timestamp = format.parse(dateStr);
        if (payload.select("a") != null) {
            Elements list = payload.select("a");
            if (list.size() > 0) {
                Element a = list.get(0);
                id = a.ownText();
                ref = a.attr("href");
            } else {
                id = "private data";
            }
            Element span = payload.select("span").get(0);
            status = span.ownText();
        }

        return new DumpEntry(timestamp, id, ref, status);
    }

    public static void main(String[] args) {
        // String test =
        // "2015-02-13 02:39:35 labswiki (new): has not yet been dumped";
        // String dateStr = test.substring(0, 19);
        // String id = test.split("\\s")[2];
        // DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // format.setTimeZone(TimeZone.getTimeZone("GMT"));
        // Date timestamp = null;
        // try {
        // timestamp = format.parse(dateStr);
        // } catch (ParseException e) {
        // //ignore
        // }
        // System.err.println(timestamp);
        // System.err.println(id);

        // //Eメール　の正規表現パターン
        // String emailPattern =
        // "[0-9a-zA-Z_\\-]+@[0-9a-zA-Z_\\-]+(\\.[0-9a-zA-Z_\\-]+){1,}";
        //
        // //Eメールであるかどうかの判定
        //
        // //false
        // System.out.println("12345".matches(emailPattern));
        // //true
        // System.out.println("x21@syboos.com".matches(emailPattern));
        // System.out.println("x21@syboos.jp".matches(emailPattern));
        // //false
        // System.out.println("x21@syboos".matches(emailPattern));
        // System.out
        // .println(matchReplace("<a href=\"#{url}\" target=\"#{target}\">this is a test</a>"));

        String data = "___[アアア]___[イイイ]___[ウウウ]___[エエエ]___[オオオ]___";

        String regex = "\\[.*?\\]";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            System.err.println("Start index : " + matcher.start()
                    + " End index : " + matcher.end() + "\nMacth : "
                    + matcher.group() + " Replace : "
                    + matcher.group().replaceAll("\\[|\\]", ""));
        }
    }
}
