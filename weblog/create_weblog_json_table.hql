ADD jar /home/cloudera/Jeetendra/weblog/jar/json-serde-1.3.1-SNAPSHOT-jar-with-dependencies.jar;

DROP TABLE assignment.weblog_json;

CREATE EXTERNAL TABLE assignment.weblog_json (
	ip_address string,
	date_time string,
	request_type string,
	response_status string,
	response_byte string,
	reffer_url string,
	search_keywords array<string>,
	user_browser string,
	user_url string,
	log_date string 
) 
ROW FORMAT SERDE 'org.openx.data.jsonserde.JsonSerDe'
Location '/user/cloudera/weblog/table_data/';

LOAD DATA INPATH "/user/cloudera/weblog/output/part-r-*" OVERWRITE INTO TABLE assignment.weblog_json;
