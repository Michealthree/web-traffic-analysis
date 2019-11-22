package com.zsz.preparser

import com.zsz.prepaser.{PreParsedLog, WebLogPreParser}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Encoders, SaveMode, SparkSession}


/**
  * 4.原始与解析后入Hive库
export HADOOP_CONF_DIR=/home/hadoop-zsz/bigdata/hadoop-2.7.7/etc/hadoop/
spark-submit --master yarn \
--class com.zsz.preparser.PreparseETL \
--driver-memory 512M \
--executor-memory 512M \
--num-executors 2 \
--executor-cores 1 \
--conf spark.traffic.analysis.rawdata.input=hdfs://master:9999/user/hadoop-zsz/traffic-analysis/rawlog/20180617 \
/home/hadoop-zsz/traffic-analysis/jars/spark-preparse-etl-1.0-SNAPSHOT-jar-with-dependencies.jar prod
  */
object PreparseETL {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    if(args.isEmpty){
      conf.setMaster("local")
    }
    val spark = SparkSession.builder()
      .appName("PreparseETL")
      .enableHiveSupport()
      .config(conf)
      .getOrCreate()

    val rawdataInputPath = spark.conf.get("spark.traffic.analysis.rawdata.input",
      "hdfs://master:9999/user/hadoop-zsz/traffic-analysis/rawlog/20180616")

    val numberPartitions = spark.conf.get("spark.traffic.analysis.rawdata.numberPartitions", "2").toInt

    val preParsedLogRDD :RDD[PreParsedLog] = spark.sparkContext.textFile(rawdataInputPath).flatMap(line =>
    Option(WebLogPreParser.parse(line)))

    val preParsedLogDS = spark.createDataset(preParsedLogRDD)(Encoders.bean(classOf[PreParsedLog]))

    preParsedLogDS.coalesce(numberPartitions)
      .write
      .mode(SaveMode.Append)
      .partitionBy("year", "month", "day")
      .saveAsTable("rawdata.web")

    spark.stop()
  }
}
