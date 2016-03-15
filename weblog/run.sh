#!/bin/bash

hdfs dfs -rm -r skipTrash /user/cloudera/weblog/output

hdfs dfs -rm -r skipTrash /user/cloudera/weblog/table_data

hdfs dfs -rm -r skipTrash /user/cloudera/weblog/table_weblog_data/

hadoop jar weblog.jar wipro.hadoop.weblog.parser.WebLogDriver /user/cloudera/weblog/input /user/cloudera/weblog/output

hive -f create_weblog_json_table.hql

hive -f create_weblog_table.hql

hive -f load_weblog_table.hql

