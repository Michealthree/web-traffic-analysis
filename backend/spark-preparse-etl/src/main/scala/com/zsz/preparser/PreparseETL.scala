package com.zsz.preparser

import com.zsz.prepaser.{PreParsedLog, WebLogPreParser}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Encoders, SaveMode, SparkSession}

/**
  *4.原始与解析后入Hive库
  */
object PreparseETL {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("PreparseETL")
      .master("local[2]")
      .enableHiveSupport()
      .getOrCreate()

    val rawdataInputPath = spark.conf.get("spark.traffic.analysis.rawdata.input",
      "hdfs://master:9999/user/hadoop-zsz/traffic-analysis/rawlog/20180615")

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
