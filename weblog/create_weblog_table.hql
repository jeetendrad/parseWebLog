
DROP TABLE assignment.weblog;

CREATE EXTERNAL TABLE assignment.weblog (
	ip_address string,
	date_time string,
	response_status int,
	response_byte int,
	search_keywords array<string>,
	user_url string
)
PARTITIONED BY (date_year int, date_month int)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
Location '/user/cloudera/weblog/table_weblog_data/';
