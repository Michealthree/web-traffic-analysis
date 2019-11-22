package com.zsz.spark.web

import java.util.concurrent.ConcurrentHashMap

import org.slf4j.LoggerFactory

object AbnormalUserDetector {

  private val logger = LoggerFactory.getLogger(classOf[AbnormalUserDetector])
}

trait AbnormalUserDetector {
  import AbnormalUserDetector._

  // Note: the number of the tasks of the first stage (the 'flatMap' step in current ETL) will be roughly
  // the number of the parquet file blocks (see http://spark.apache.org/docs/latest/tuning.html#level-of-parallelism)
  //
  // As long as we can handle #task * threshold for a single user, we are fine here.
  // (We will have to verify this in our cluster)
  //
  // Again, this is kind of UGLY work around since we are relying on too many internal details.
  private val MAX_USER_DATA_OBJECT_PER_EXECUTOR = Integer.getInteger("wd.etl.MaxUserDataObjectPerTask", 5000)

  private val userCounters = new ConcurrentHashMap[String, Int]


  private def checkAndUpdateCounter(user: String): Boolean = {
    val current = Option(userCounters.get(user)).getOrElse(0)
    userCounters.put(user, current + 1)
    if (current < MAX_USER_DATA_OBJECT_PER_EXECUTOR) {
      false
    } else {
      if(current % 5000 == 0)
        logger.warn(s"User ${user}: total number of data objects exceeded the limit ${MAX_USER_DATA_OBJECT_PER_EXECUTOR}, the current number is ${current + 1}")
      true
    }
  }

  @inline def hasUserReachLimit(user: String): Boolean = checkAndUpdateCounter(user)
}

