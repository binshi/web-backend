# web-backend

Scala Partial functions
http://blog.bruchez.name/2011/10/scala-partial-functions-without-phd.html

Big Data

1. Apache Hadoop is the most well-known and popular open source implementation of batch processing and a distributed system using the MapReduce paradigm.

Real-time processing:
Cloudera Impala, Facebook Presto, Apache Drill, and Hive on Tez powered by Stinger whose effort is to make a 100x performance improvement over Apache Hive.
In memory processing: Spark can be easily integrated with Hadoop and the resilient distributed dataset can be generated from data sources such as HDFS and HBase for efficient caching.

Stream processing: Storm from Twitter and S4 from Yahoo!. Both the frameworks run on the Java Virtual Machine (JVM) and both process keyed streams.

Hive is a standard for SQL queries over petabytes of data in Hadoop. It provides SQL-like
access for data in HDFS making Hadoop to be used like a warehouse structure.
