package crawler;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|mpeg"
                                                           + "|png|pdf|mp3|mp4|zip|doc|gz))$");
    
    private final static Pattern MATCH = Pattern.compile("^http://.*\\.ics\\.uci\\.edu/.*");

    private final static Pattern AVOID = Pattern.compile("^http://calendar.*");

    private ArrayList<String> urls = new ArrayList<String>();
    private String delimiter = "##--------------------------------------------------------##";
    private long startTime = System.currentTimeMillis();
    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
     @Override
     public boolean shouldVisit(Page referringPage, WebURL url) {
         String href = url.getURL().toLowerCase();
         return !FILTERS.matcher(href).matches()
                && MATCH.matcher(href).matches()
                && !AVOID.matcher(href).matches();
     }

     /**
      * This function is called when a page is fetched and ready
      * to be processed by your program.
      */
     @Override
     public void visit(Page page) {
    	 countPage ++;
         String url = page.getWebURL().getURL();
         urls.add(url);

         if (page.getParseData() instanceof HtmlParseData) {
             HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
             String text = htmlParseData.getText();
             //String html = htmlParseData.getHtml();
             //Set<WebURL> links = htmlParseData.getOutgoingUrls();
             
             try {
            	// String filePath = "./file/html/" + Integer.toString(countPage) + ".html";
            	// Files.write(page.getContentData(), new File(filePath));
            	 String filePath = "./file/text/" + Integer.toString(countPage) + ".txt";
            	 Writer writer = new BufferedWriter(new OutputStreamWriter(
                         new FileOutputStream(filePath)));
            	 writer.write(text);
            	 writer.close();
            	 Integer fileNum = countPage / 5000;
            	 PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("./file/myfile" + fileNum.toString() + ".txt", true)));
            	 out.println(text);
            	 out.println(delimiter);
            	 out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            // System.out.println("Text length: " + text.length());
            // System.out.println("Html length: " + html.length());
            // System.out.println("Number of outgoing links: " + links.size());
             if(countPage % 10000 == 0){
            	 long curTime = System.currentTimeMillis();
            	 System.out.println(countPage.toString() + " pages have been crawled in " + Long.toString(curTime - startTime) + "ms");;
             }
             if(countPage % 100000 == 0){
            	 PrintWriter out;
				try {
					out = new PrintWriter(new BufferedWriter(new FileWriter("./file/urls" + countPage.toString() + ".txt", true)));
					for(int i = 0; i < urls.size(); i ++)
	            		 out.println(urls.get(i));
	            	 urls.clear();
	            	 out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	 
             }
             System.out.println("Finished "+countPage+" "+url);
         }
    }
    public static Integer countPage = 0;
}