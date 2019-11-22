package com.zsz.spark.web

import com.esotericsoftware.kryo.Kryo
import com.zsz.iplocation.IpLocation
import com.zsz.parser.dataobject._
import com.zsz.parser.dataobject.dim._
import org.apache.spark.serializer.KryoRegistrator

/**
  *  使用Kryo序列化机制
  */
class WebRegistrator extends KryoRegistrator {
  override def registerClasses(kryo: Kryo): Unit = {
    kryo.register(classOf[BaseDataObject])
    kryo.register(classOf[PvDataObject])
    kryo.register(classOf[HeartbeatDataObject])
    kryo.register(classOf[EventDataObject])
    kryo.register(classOf[McDataObject])
    kryo.register(classOf[TargetPageDataObject])

    kryo.register(classOf[AdInfo])
    kryo.register(classOf[BrowserInfo])
    kryo.register(classOf[ReferrerInfo])
    kryo.register(classOf[SiteResourceInfo])
    kryo.register(classOf[TargetPageInfo])

    kryo.register(classOf[IpLocation])
  }
}
