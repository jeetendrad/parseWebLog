package wipro.hadoop.weblog.hive.udf;

import java.util.Arrays;

import org.apache.hadoop.hive.ql.exec.UDF;

public class UDFRank extends UDF{
	Object[] previous_keys = null;
	int previous_index;

	public int evaluate(Object... keys) {
		if (previous_keys == null || !Arrays.equals(previous_keys, keys)) {
			previous_index = 0;
			previous_keys = keys.clone();
		}
		previous_index++;
		return previous_index;
	}
}