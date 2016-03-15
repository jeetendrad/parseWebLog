ADD jar /home/cloudera/Jeetendra/weblog/jar/json-serde-1.3.1-SNAPSHOT-jar-with-dependencies.jar;

set hive.exec.dynamic.partition.mode=nonstrict;
set hive.exec.dynamic.partition=true;

INSERT OVERWRITE TABLE assignment.weblog
PARTITION (date_year, date_month)
SELECT wj.ip_address, wj.log_date, cast(wj.response_status as int), cast(wj.response_byte as int), wj.search_keywords, wj.user_url, year(wj.log_date) as date_year, month(wj.log_date) as date_month FROM assignment.weblog_json wj;
