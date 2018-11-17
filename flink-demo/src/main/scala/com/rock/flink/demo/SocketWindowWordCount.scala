package com.rock.flink.demo

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.scala._

/**
  * Created with IntelliJ IDEA.
  * Description: 
  * User: yanshi
  * Date: 2018-11-13
  * Time: 16:02
  */
object SocketWindowWordCount {

  def main(args: Array[String]): Unit = {
    val port: Int = try {
      ParameterTool.fromArgs(args).getInt("port")
    } catch {
      case e: Exception => {
        System.err.println("No port specified. Please run 'SocketWindowWordCount --port <port>'")
        return
      }
    }


    val environment:StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
//    environment.setStateBackend(new FsStateBackend(""))

    val text = environment.socketTextStream("localhost", port, '\n')

    val windowCounts = text
      .flatMap { w => w.split("\\s") }
      .map { w => WordWithCount(w, 1) }
      .keyBy("word")
      .timeWindow(Time.seconds(5), Time.seconds(1))
      .sum("count")


    windowCounts.print().setParallelism(1)
    environment.execute("Socket Window WordCount")
  }

  case class WordWithCount(word: String, count: Long)

}
