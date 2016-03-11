package wipro.hadoop.weblog.parser;

import java.util.regex.*;

public class Test {
	public static void main(String args[]) {
		String webLogEntryPattern = "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3})[\\s?](?:(\\d+)|\\S) \"([^\"]+)\" \"([^\"]+)\"";
		String webLogString = "72.14.192.65 - - [25/Jul/2009:00:00:23 -0800] \"GET /robots.txt HTTP/1.1\" 200 590 \"-\" \"msnbot/1.1 (+http://search.msn.com/msnbot.htm)\"";
		//String webLogString = "72.14.192.65 - - [25/Jul/2009:00:00:23 -0800] \"GET /robots.txt HTTP/1.1\" 200 590 \"-\" \"msnbot/1.1 (+http://search.msn.com)\"";
		
		String userUrlEntryPattern = "(http|https)://(.*?)[/\\)\\?\\s$]";
		
		System.out.println(webLogEntryPattern);
		System.out.println(webLogString);
		Pattern webLogpattern = Pattern.compile(webLogEntryPattern);
		Matcher matcherWebLog = webLogpattern.matcher(webLogString);

		Pattern userUrlpattern = Pattern.compile(userUrlEntryPattern);


		if(matcherWebLog.find()) {
			System.out.println("Group Count : " + matcherWebLog.groupCount());
			System.out.println("IP Address : " + matcherWebLog.group(1));	
			System.out.println("Reffer Url : " + matcherWebLog.group(8));
			
			String userBrowser = matcherWebLog.group(9);
			System.out.println("User Browser : " + userBrowser);
			Matcher matcherUserUrl = userUrlpattern.matcher(userBrowser);
			if(matcherUserUrl.find()) {
				System.out.println("Count : " + matcherUserUrl.groupCount());
				System.out.println("User URL : " + matcherUserUrl.group(2));
			}
		} else {
			System.out.println("Did not find match!!!");	
		}
		
	}
}
