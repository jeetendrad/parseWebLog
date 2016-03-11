package wipro.hadoop.weblog.parser;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

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
	}

	public Text toJson() {
		
		String searchKw = SearchKeyWords.toString();
		
		String keyWords = "";
		if(!searchKw.isEmpty()){
			keyWords = searchKw.replaceAll(" ", "','");	
			keyWords = "'" + keyWords + "'";
		}

		
		String jsonRecord = "{" +
							"\"IpAddress\":\"" + IpAddress +"\"," +
							"\"DateTime\":\"" + DateTime +"\"," +
							"\"RequestType\":\"" + RequestType +"\"," +
							"\"ResponseStatus\":" + ResponseStatus +"," +
							"\"ResponseByte\":" + ResponseByte +"," +
							"\"RefferUrl\":\"" + RefferUrl +"\"," +
							"\"SearchKeyWords\":[" + keyWords +"]," +
							"\"UserBrowser\":\"" + UserBrowser +"\"," +
							"\"UserUrl:\"" + UserUrl + "\"" +
							"}";
		return new Text(jsonRecord);
	}
}
