package wipro.hadoop.weblog.parser;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

import wipro.hadoop.weblog.pojo.WebLog;


public class WebLogRecordReader extends RecordReader<Text, WebLogWritable> implements WebLog{

    private LineRecordReader lineReader;
    private WebLogWritable value;
    private Text key;
    
	@Override
	public void close() throws IOException {
        if (lineReader != null) {
            lineReader.close();
        }
	}
	@Override
	public Text getCurrentKey() throws IOException,
			InterruptedException {
		return key;
	}
	@Override
	public WebLogWritable getCurrentValue() throws IOException,
			InterruptedException {
		
		return value;
	}
	@Override
	public float getProgress() throws IOException, InterruptedException {
		
		return lineReader.getProgress();
	}
	@Override
	public void initialize(InputSplit inputSplit, TaskAttemptContext context)
			throws IOException, InterruptedException {
		lineReader = new LineRecordReader();
		lineReader.initialize(inputSplit, context);
		
	}
	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		if (!lineReader.nextKeyValue()) {
			return false;
		}
		
		Map<String, String> webLogMapObj = WebLogParser.parse(lineReader.getCurrentValue().toString());
		
		if(webLogMapObj.isEmpty()) {
			return nextKeyValue();
		} else {
			key = new Text(webLogMapObj.get(IP_ADDRESS));
			value = new WebLogWritable();
			value.set(webLogMapObj);
			return true;			
		}

	} 
}