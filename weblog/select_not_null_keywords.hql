ADD jar /home/cloudera/Jeetendra/weblog/jar/json-serde-1.3.1-SNAPSHOT-jar-with-dependencies.jar;

select * from assignment.weblog_json where search_keywords[0] is not null limit 10;
