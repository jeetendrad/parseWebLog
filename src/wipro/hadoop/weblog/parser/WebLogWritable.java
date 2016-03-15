package wipro.hadoop.weblog.parser;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.json.simple.JSONObject;

import wipro.hadoop.weblog.pojo.WebLog;

public class WebLogWritable implements Writable,WebLog {

	private Text IpAddress;
	private Text DateTime;
	private Text RequestType;
	private IntWritable ResponseStatus;
	private IntWritable ResponseByte;
	private Text RefferUrl;
	private Text SearchKeyWords;
	private Text UserBrowser;
	private Text UserUrl;
	private Text LogDate;

	public WebLogWritable() {
		super();
		
		this.IpAddress = new Text();
		this.DateTime = new Text();
		this.RequestType = new Text();
		this.ResponseStatus = new IntWritable();
		this.ResponseByte = new IntWritable();
		this.RefferUrl = new Text();
		this.SearchKeyWords = new Text();
		this.UserBrowser = new Text();
		this.UserUrl = new Text();
		this.LogDate = new Text();
	}
	
	public void set(Map<String, String> webLogMapObj) {
		// super();
		this.IpAddress.set(webLogMapObj.get(IP_ADDRESS));
		this.DateTime.set(webLogMapObj.get(DATE_TIME));
		this.RequestType.set(webLogMapObj.get(REQUEST_TYPE));
		this.ResponseStatus.set(Integer.parseInt(webLogMapObj.get(RESPONSE_STATUS)));
		this.ResponseByte.set(Integer.parseInt(webLogMapObj.get(RESPONSE_BYTE)));
		
    	if(webLogMapObj.containsKey(REFERER_URL)) {
    		this.RefferUrl.set(webLogMapObj.get(REFERER_URL));
    		if(webLogMapObj.containsKey(SEARCH_KEY_WORDS)) {
    			this.SearchKeyWords.set(webLogMapObj.get(SEARCH_KEY_WORDS));
    		}
    	}

		this.UserBrowser.set(webLogMapObj.get(USER_BROWSER));
		this.UserUrl.set(webLogMapObj.get(USER_URL));
		this.LogDate.set(webLogMapObj.get(LOG_DATE));
	}
	
	public Text getIpAddress() {
		return IpAddress;
	}

	public void setIpAddress(Text ipAddress) {
		IpAddress = ipAddress;
	}

	public Text getDateTime() {
		return DateTime;
	}

	public void setDateTime(Text dateTime) {
		DateTime = dateTime;
	}

	public Text getRequestType() {
		return RequestType;
	}

	public void setRequestType(Text requestType) {
		RequestType = requestType;
	}

	public IntWritable getResponseStatus() {
		return ResponseStatus;
	}

	public void setResponseStatus(IntWritable responseStatus) {
		ResponseStatus = responseStatus;
	}

	public IntWritable getResponseByte() {
		return ResponseByte;
	}

	public void setResponseByte(IntWritable responseByte) {
		ResponseByte = responseByte;
	}

	public Text getRefferUrl() {
		return RefferUrl;
	}

	public void setRefferUrl(Text refferUrl) {
		RefferUrl = refferUrl;
	}

	public Text getSearchKeyWords() {
		return SearchKeyWords;
	}

	public void setSearchKeyWords(Text searchKeyWords) {
		SearchKeyWords = searchKeyWords;
	}

	public Text getUserBrowser() {
		return UserBrowser;
	}

	public void setUserBrowser(Text userBrowser) {
		UserBrowser = userBrowser;
	}

	public Text getUserUrl() {
		return UserUrl;
	}

	public void setUserUrl(Text userUrl) {
		UserUrl = userUrl;
	}

	
	public Text getLogDate() {
		return LogDate;
	}

	public void setLogDate(Text logDate) {
		LogDate = logDate;
	}

	@Override
	public void readFields(DataInput input) throws IOException {
		IpAddress.readFields(input);
		DateTime.readFields(input);
		RequestType.readFields(input);
		ResponseStatus.readFields(input);
		ResponseByte.readFields(input);
		RefferUrl.readFields(input);
		SearchKeyWords.readFields(input);
		UserBrowser.readFields(input);
		UserUrl.readFields(input);
		LogDate.readFields(input);
	}

	@Override
	public void write(DataOutput output) throws IOException {
		IpAddress.write(output);
		DateTime.write(output);
		RequestType.write(output);
		ResponseStatus.write(output);
		ResponseByte.write(output);
		RefferUrl.write(output);
		SearchKeyWords.write(output);
		UserBrowser.write(output);
		UserUrl.write(output);
		LogDate.write(output);
	}

	public Text toJson() {
		
		String searchKw = SearchKeyWords.toString();
		String jsonRecord = "";
		
		JSONObject webLogJsonObj = new JSONObject();
		webLogJsonObj.put("ip_address",IpAddress.toString());
		webLogJsonObj.put("date_time",DateTime.toString());
		webLogJsonObj.put("request_type",RequestType.toString());
		webLogJsonObj.put("response_status",ResponseStatus.get());
		webLogJsonObj.put("response_byte",ResponseByte.get());
		webLogJsonObj.put("reffer_url",RefferUrl.toString());
		
		String[] keyWords;
		ArrayList keyWordsArr = new ArrayList();
		if(!searchKw.isEmpty()){
			keyWords = searchKw.split(" ");
			if(keyWords.length>0){
				for (String kw: keyWords) {
					keyWordsArr.add(kw);
				}				
			}

		}
		webLogJsonObj.put("search_keywords",keyWordsArr);
		webLogJsonObj.put("user_browser",UserBrowser.toString());
		webLogJsonObj.put("user_url",UserUrl.toString());
		webLogJsonObj.put("log_date",LogDate.toString());
		
	      StringWriter strOutput = new StringWriter();
	      try {
	    	  webLogJsonObj.writeJSONString(strOutput);
	    	  jsonRecord = webLogJsonObj.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
/*		String keyWords = "";
		if(!searchKw.isEmpty()){
			keyWords = searchKw.replaceAll("\"", "");
			keyWords = searchKw.replaceAll(" ", "\",\"");	
			keyWords = "\"" + keyWords + "\"";
		}

		
		String jsonRecord = "{" +
							"\"ip_address\":\"" + IpAddress +"\"," +
							"\"date_time\":\"" + DateTime +"\"," +
							"\"request_type\":\"" + RequestType +"\"," +
							"\"response_status\":" + ResponseStatus +"," +
							"\"response_byte\":" + ResponseByte +"," +
							"\"reffer_url\":\"" + RefferUrl +"\"," +
							"\"search_keywords\":[" + keyWords +"]," +
							"\"user_browser\":\"" + UserBrowser +"\"," +
							"\"user_url\":\"" + UserUrl +"\"," +
							"\"log_date\":\"" + LogDate + "\"" +
							"}";
*/		
		return new Text(jsonRecord);
	}
}
