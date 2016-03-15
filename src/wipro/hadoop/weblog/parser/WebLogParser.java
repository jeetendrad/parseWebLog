package wipro.hadoop.weblog.parser;

import java.io.BufferedReader;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

import wipro.hadoop.weblog.pojo.WebLog;


/**
 * Parse an Web log file with Regular Expressions
 */
public class WebLogParser implements WebLog {

	/** The number of fields that must be found. */
	public static final int NUM_FIELDS = 9;
	//public static final String webLogEntryPattern = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"";
	public static final String webLogEntryPattern = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3})[\\s?](?:(\\d+)|\\S) \"([^\"]+)\" \"([^\"]+)\"";
	public static final String searchQueryPattern = "(.*?)q=(.*?)(?:&(.*?)|$)";
	public static final String userUrlEntryPattern = "(http|https)://(.*?)[/\\)\\?\\s$]";
	public static final String userDateEntryPattern = "^(.*?):(.*?)";
		
	public static Map<String, String> parse(String webLogString) {
		
		Map<String, String> webLogMap = new HashMap<String, String>();
		String UserUrl = "";
		String UserBrowser;
/*		System.out.println("Using REG Pattern:");
		System.out.println(webLogEntryPattern);

		System.out.println("Input line is:");
		System.out.println(webLogString);*/

		Pattern webLogpattern = Pattern.compile(webLogEntryPattern);
		Matcher matcherWebLog = webLogpattern.matcher(webLogString);
	//	System.out.println(matcherWebLog.groupCount());
		
		Pattern searchQueryattern = Pattern.compile(searchQueryPattern);
		Pattern userUrlpattern = Pattern.compile(userUrlEntryPattern);
		Pattern userDatepattern = Pattern.compile(userDateEntryPattern);
		
		if (!matcherWebLog.matches() || 
				NUM_FIELDS != matcherWebLog.groupCount()) {
			System.err.println("Bad Weblog entry (or problem with REG Expression?):");
			System.err.println(webLogEntryPattern);
			return webLogMap;
		}
		//System.out.println("IP Address: " + matcherWebLog.group(1));
		webLogMap.put(IP_ADDRESS, matcherWebLog.group(1));
		//System.out.println("Date&Time: " + matcherWebLog.group(4));
		String dateTimeStr = matcherWebLog.group(4);
		if(!dateTimeStr.isEmpty()) {
			webLogMap.put(DATE_TIME, dateTimeStr);	
			Matcher matcherUserDate = userDatepattern.matcher(dateTimeStr);
			if(matcherUserDate.find()) {
				//System.out.println("Logging Date: " + matcherUserDate.group(1));
				SimpleDateFormat simpleDF = new SimpleDateFormat ("dd/MMM/yyyy"); //07/Aug/2009
				SimpleDateFormat newSimpleDF = new SimpleDateFormat ("yyyy-MM-dd"); //2009-Aug-07
				
				try {
					Date logDateFormatted = simpleDF.parse(matcherUserDate.group(1).toString());
					
					webLogMap.put(LOG_DATE, newSimpleDF.format(logDateFormatted).toString());	
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
			
		}
		
		//System.out.println("Request: " + matcherWebLog.group(5));
		webLogMap.put(REQUEST_TYPE, matcherWebLog.group(5));
		//System.out.println("Response: " + matcherWebLog.group(6));
		webLogMap.put(RESPONSE_STATUS, matcherWebLog.group(6));
		//System.out.println("Bytes Sent: " + matcherWebLog.group(7));
		String responseByte = matcherWebLog.group(7);
		if(responseByte == null) {
			responseByte = "0";
		}
		webLogMap.put(RESPONSE_BYTE, responseByte);
		
		
		if (!matcherWebLog.group(8).equals("-")) {
    		String webReffer;
    		webReffer = matcherWebLog.group(8);
			//System.out.println("Referer: " + matcherWebLog.group(8));
			
    		webLogMap.put(REFERER_URL, webReffer);
			Matcher matcherSearchQuery = searchQueryattern.matcher(webReffer);
			
			if(matcherSearchQuery.find( )) {
				String searchKeyWord;
				try {
					searchKeyWord = java.net.URLDecoder.decode(matcherSearchQuery.group(2), "UTF-8");
					//System.out.println("Search Key Words: " + searchKeyWord);
					webLogMap.put(SEARCH_KEY_WORDS,  searchKeyWord);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
			Matcher matcherUserUrl = userUrlpattern.matcher(webReffer);
			if(matcherUserUrl.find()) {
				UserUrl = matcherUserUrl.group(2);
			}
			
		}
		UserBrowser = matcherWebLog.group(9);
		//System.out.println("Browser: " + matcherWebLog.group(9));
		webLogMap.put(USER_BROWSER, UserBrowser);
		if(UserUrl.isEmpty()) {
			Matcher matcherUserUrl = userUrlpattern.matcher(UserBrowser);
			if(matcherUserUrl.find()) {
				UserUrl = matcherUserUrl.group(2);
			}
		}
		webLogMap.put(USER_URL, UserUrl);
		
		return webLogMap;
	}		
	
	
	/*public static void main(String args[]) {
		
		//public static final String webLogString = "123.45.67.89 - - [27/Oct/2000:09:27:09 -0400] \"GET /java/javaResources.html HTTP/1.0\" 200 10450 \"-\" \"Mozilla/4.6 [en] (X11; U; OpenBSD 2.8 i386; Nav)\"";
		//String webLogString = "72.14.192.65 - - [25/Jul/2009:00:00:23 -0800] \"GET /robots.txt HTTP/1.1\" 200 590 \"-\" \"msnbot/1.1 (+http://search.msn.com/msnbot.htm)\"";
		
		String file = "/home/cloudera/workspace/parseWebLogs/webLogData/input/logdata_sample1";
		//String file = "/home/cloudera/workspace/parseWebLogs/webLogData/input/logdata_sample2";

			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(file));
				
			    for(String webLogString; (webLogString = br.readLine()) != null; ) {
			        // process the line.
			    	Map<String, String> webLogMapObj = parse(webLogString);
			    	
			    	System.out.println("IP Address: " + webLogMapObj.get(IP_ADDRESS));
			    	System.out.println("Date&Time: " + webLogMapObj.get(DATE_TIME));
			    	System.out.println("Request: " + webLogMapObj.get(REQUEST_TYPE));
			    	System.out.println("Response Status: " + webLogMapObj.get(RESPONSE_STATUS));
			    	System.out.println("Bytes Sent: " + webLogMapObj.get(RESPONSE_BYTE));

			    	if(webLogMapObj.containsKey(REFERER_URL)) {
			    		System.out.println("Referer: " + webLogMapObj.get(REFERER_URL));
			    		if(webLogMapObj.containsKey(SEARCH_KEY_WORDS)) {
			    			
			    			System.out.println("Search Key Words: " + webLogMapObj.get(SEARCH_KEY_WORDS));
			    		}
			    	}
			    	System.out.println("Browser: " + webLogMapObj.get(USER_BROWSER));
			    	System.out.println("User Url: " + webLogMapObj.get(USER_URL));
			    	System.out.println("Log Date: " + webLogMapObj.get(LOG_DATE));
			    }
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    // line is not visible here.
		
		
	}
*/
}
