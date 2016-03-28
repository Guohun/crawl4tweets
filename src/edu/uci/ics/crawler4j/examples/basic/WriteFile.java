/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uci.ics.crawler4j.examples.basic;

import java.util.ArrayList;
import java.util.Formatter;

/**
 *
 * @author zgh
 */
class WriteFile {
    private Formatter x;

public void openfile(String keyword) {

    try {
        x = new Formatter(keyword + ".html");
    } catch (Exception e) {

        System.out.println("ERROR");
    }
}

public void StartHtml() {
    x.format("%s %n %s %n %s %n %s %n %s %n ", "<html>", "<head>", "</head>", "<body>", "<center>");
}

public void addUrl(ArrayList<String> UrlHits) {

    for (String list : UrlHits) {
        x.format("%s%s%s%s%s%n%s%n", "<a href=\"", list, "\" target=\"_blank\">", list, "</a>", "<br>");
    }
}

public void EndHtml() {
    x.format("%s %n %s %n %s %n", "</center>", "</body>", "</html>");
}

public void closeFile() {
    x.close();
}
}
