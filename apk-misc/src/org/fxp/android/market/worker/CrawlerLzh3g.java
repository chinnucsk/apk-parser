package org.fxp.android.market.worker;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.fxp.crawler.bean.MarketPageBean;
import org.fxp.crawler.worker.toolkits.SimpleWebCrawler;
import org.w3c.dom.Element;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public class CrawlerLzh3g extends MarketPageBean {

	/**
	 * @param args
	 * @throws ParserConfigurationException 
	 * @throws TransformerFactoryConfigurationError 
	 * @throws IOException 
	 * @throws TransformerException 
	 * @throws Exception
	 */

	public static void main(String[] args) throws Exception {
		MarketPageBean crawlerProcessor=new CrawlerLzh3g();
		crawlerProcessor.init("lzh3g.com");
		
		String dbbase = "data/crawl/" + crawlerProcessor.marketName;
		seed = new URL("http://lzh3g.com/");
		baseUrls = "http://lzh3g.com".split(" ");
//		customFilter = Pattern.compile(args[3]);
		customFilter = Pattern.compile("^http://down.lzh3g.com/.+");
		int workerNum = Integer.valueOf(5);

		CrawlController controller = new CrawlController(dbbase);
		controller.addSeed(seed.toString());
		SimpleWebCrawler.marketPager = crawlerProcessor;
		SimpleWebCrawler.baseUrls = baseUrls;

		controller.start(SimpleWebCrawler.class, workerNum);
	}

	@Override
	public void processPage(Page page) {
		ArrayList<WebURL> urls = page.getURLs();
		for (WebURL url : urls) {
			if (customFilter.matcher(url.toString()).matches()) {
				// Get download url and page content
				synchronized (document) {
					Element apkEm = document.createElement("APK");
					apkEm.setAttribute("CRAWLTIME", new Date().toString());
					apkEm.setAttribute("MARKET", marketName);
					apkEm.setAttribute("DOWNURL", url.toString());
					apkEm.setAttribute("DESCRURL", page.getWebURL().toString());
					apkEm.setAttribute("PAGEHTML", page.getHTML());
					rootElement.appendChild(apkEm);
					System.out.println(url.toString());
					return;
				}
			}
		}
	}
}
