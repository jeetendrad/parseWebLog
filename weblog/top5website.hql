ADD jar /home/cloudera/Jeetendra/weblog/weblog.jar;

CREATE TEMPORARY FUNCTION web_rank AS 'wipro.hadoop.weblog.hive.udf.UDFRank';

SELECT * 
FROM 
(SELECT date_year,date_month, user_url, totalcnt, web_rank(date_year,date_month) as row_number 
FROM (
SELECT date_year,date_month, user_url, count(*) totalcnt FROM assignment.weblog
WHERE user_url is not null
AND user_url <>""
GROUP BY date_year,date_month, user_url
HAVING COUNT(*)>1
DISTRIBUTE BY date_year,date_month
SORT BY date_year,date_month, totalcnt desc 
)T1
)T2
WHERE row_number < 5
