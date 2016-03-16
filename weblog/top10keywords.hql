ADD jar /home/cloudera/Jeetendra/weblog/weblog.jar;

CREATE TEMPORARY FUNCTION web_rank AS 'wipro.hadoop.weblog.hive.udf.UDFRank';

SELECT * 
FROM 
(SELECT date_year,date_month, search_word, totalcnt, web_rank(date_year,date_month) as row_number 
FROM (
SELECT date_year,date_month, search_word, count(*) totalcnt FROM assignment.weblog LATERAL VIEW explode(search_keywords) searchKeywordsView AS search_word 
WHERE search_word is not null
AND search_word <>""
GROUP BY date_year,date_month, search_word
HAVING COUNT(*)>1
DISTRIBUTE BY date_year,date_month
SORT BY date_year,date_month, totalcnt desc 
)T1
)T2
WHERE row_number <10 
