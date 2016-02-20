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

    //private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|mpeg"
    //                                                       + "|png|pdf|mp3|mp4|zip|doc|gz))$");
	private final static Pattern FILTERS = Pattern.compile(
			".*(\\.(csv|css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf"
					+ "|webm|tar|wma|zip|rar|gz|xz|bz|lz|7z|dmg))$");
    
    private final static Pattern MATCH = Pattern.compile("^http://.*\\.ics\\.uci\\.edu/.*");

    private final static Pattern AVOID = Pattern.compile("^http://(wicsd|jp3-pc2|duttgroup|archive|calendar).*");

    private String delimiter_url = 		"##------------------URL-------------------------##";
    private String delimiter_title = 	"##-----------------TITLE------------------------##";
    private String delimiter_text = 	"##------------------TEXT------------------------##";

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
         
         if (page.getParseData() instanceof HtmlParseData) {
             HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
             String text = htmlParseData.getText();
             String title = htmlParseData.getTitle();
             try {
                 Integer fileNum = countPage / 5000;
                 PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("./file/myfile" + (fileNum.toString()) + ".txt", true)));
                 out.println(delimiter_url);
                 out.println(url);
                 out.println(delimiter_title);
                 out.println(title);
                 out.println(delimiter_text);
                 out.println(text);
                 out.close();

                 PrintWriter out2 = new PrintWriter(new BufferedWriter(new FileWriter("./file/html/myfile" + (fileNum.toString()) + ".html", true)));
                 out2.println(url);
                 out2.println(htmlParseData.getHtml());
                 out2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          
             System.out.println("Finished "+countPage+" "+url);
         }
    }
    public static Integer countPage = 0;
}