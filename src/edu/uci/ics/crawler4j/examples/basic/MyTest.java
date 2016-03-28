/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//https://groups.google.com/forum/#!search/UQ%7Csort:date

package edu.uci.ics.crawler4j.examples.basic;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author zgh
 */
public class MyTest {
    
    
    public static void main(String[] args) throws Exception {

    RobotstxtConfig robotstxtConfig2 = new RobotstxtConfig();

    String crawlStorageFolder = "/home/zgh/octave";
    int numberOfCrawlers = 1;

    CrawlConfig config = new CrawlConfig();
    config.setCrawlStorageFolder(crawlStorageFolder);

    config.setMaxDepthOfCrawling(21);
    config.setMaxPagesToFetch(24);

    PageFetcher pageFetcher = new PageFetcher(config);
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();

    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

    Scanner perentUrl = new Scanner(System.in);
    System.out.println("Enter full perant Url... example. http://www.domain.co.uk/");
    String Url = perentUrl.nextLine();

    Scanner keyword = new Scanner(System.in,"utf-8");
    System.out.println("Enter search term... example. Pies");
    String Search = keyword.nextLine();
    
    

    System.out.println("Searching domain :" + Url);
    System.out.println("Keyword:" + Search);
    //System.out.println("Input Keyword:" + new String(Search.getBytes("utf-8")));

    ArrayList<String> DomainsToInv = new ArrayList<String>();
    ArrayList<String> SearchTerms = new ArrayList<String>();
    ArrayList<String> UrlHits = new ArrayList<String>();

    DomainsToInv.add(Url);
    SearchTerms.add(Search);

    controller.addSeed(Url);

    controller.setCustomData(DomainsToInv);
    controller.setCustomData(SearchTerms);
    controller.start(Crawler.class, numberOfCrawlers);

    WriteFile f = new WriteFile();
    f.openfile(Search);
    f.StartHtml();
    f.addUrl(UrlHits);
    f.EndHtml();
    f.closeFile();
}
}


