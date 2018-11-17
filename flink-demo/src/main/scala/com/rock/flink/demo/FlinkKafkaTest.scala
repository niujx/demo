package com.rock.flink.demo

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011
import org.apache.kafka.common.serialization.StringDeserializer

/**
  * Created with IntelliJ IDEA.
  * Description: 
  * User: yanshi
  * Date: 2018-11-15
  * Time: 10:51
  */
object FlinkKafkaTest {


  def main(args: Array[String]): Unit = {


    val environment = StreamExecutionEnvironment.getExecutionEnvironment
    environment.enableCheckpointing(5000)

    val properties = new Properties()
    properties.put("bootstrap.servers", "10.152.18.15:9092,10.152.18.28:9092,10.152.18.40:9092")
    properties.put("key.deserializer", classOf[StringDeserializer].getName)
    properties.put("value.deserializer", classOf[StringDeserializer].getName)
    properties.put("group.id", "canal_test")
    val consumer = new FlinkKafkaConsumer011[String]("canal_test", new SimpleStringSchema(), properties)
    consumer.setStartFromEarliest()
    val messageStream = environment.addSource(consumer)
//    messageStream.map(s=>{
//       val mapper = new ObjectMapper().convertValue(s,classOf[Map])
//    })
    messageStream.print()

    environment.execute()

  }


}

