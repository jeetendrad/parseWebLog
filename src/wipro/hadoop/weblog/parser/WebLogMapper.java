package wipro.hadoop.weblog.parser;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WebLogMapper extends Mapper<Text,WebLogWritable,NullWritable,Text> {

	protected void map(Text key, WebLogWritable value, Context context) throws IOException, InterruptedException {
						
		context.write(NullWritable.get(), value.toJson());			
	}
}