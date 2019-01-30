package source

import org.apache.flink.api.common.io.FileInputFormat
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.source.FileProcessingMode
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.junit.{Before, Test}
import org.scalatest.junit.JUnitSuite
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created with IntelliJ IDEA.
  * Description: 
  * User: yanshifdsfdsSSS
  * Date: 2018-12-1fd
  * Time: 11:06
  */
class StreamingSourceTest extends JUnitSuite {

  val log: Logger = LoggerFactory.getLogger(classOf[StreamingSourceTest])
  var environment: StreamExecutionEnvironment = null
  var path: String = null

  @Before
  def initialize = {
    environment = StreamExecutionEnvironment.getExecutionEnvironment
    path = classOf[StreamingSourceTest].getClassLoader.getResource("APR82L.DAT").getPath
  }


  @Test
  def testReadTextFile: Unit = {
    val lines = environment.readTextFile(path)
    lines.print()
    environment.execute("testReadTextFile")
  }

  @Test
  def testReadWithInputFormat: Unit = {
    val lines = environment.readFile(new StringFileInputFormat, path)
    lines.print()
    environment.execute("testReadWithInputFormat")

  }

  @Test
  def testReadWithInputFormatAndWatch: Unit = {
    //   val lines = environment.readFile(new StringFileInputFormat(),path,FileProcessingMode.PROCESS_CONTINUOUSLY,1)
    val lines = environment.readFile(new StringFileInputFormat(), path, FileProcessingMode.PROCESS_ONCE, 1)
    lines.print()
    environment.execute("testReadWithInputFormat")
  }

  @Test
  def testSourceFromCollection: Unit = {
    environment.fromCollection(List(1, 2, 3, 4, 5, 5))
      .map(num => num * 10)
      .print()
    environment.execute("testSourceFromCollection")
  }

  @Test
  def testIterations: Unit = {
    environment.generateSequence(0, 1000)
      .iterate(iteration => {
        val minusOne = iteration.map(v => v - 1)
        val stillGreaterThanZero = minusOne.filter(_ > 0)
        val lessThanZero = minusOne.filter(_ <= 0)
        (stillGreaterThanZero, lessThanZero)
      }).print()
    environment.setBufferTimeout(1000)
    environment.execute("testIterations")

  }

  @Test
  def testAccumulators: Unit = {

  }

  @Test
  def testSplitStream: Unit = {
    val splitStream = environment.generateSequence(0, 10)
      .split(
        (num: Long) =>
          (num % 2) match {
            case 0 => List("even")
            case 1 => List("odd")
          })

    splitStream.select("even").countWindowAll(6).sum(0).map(sum => s"even_$sum").print()
    splitStream.select("odd").countWindowAll(5).sum(0).map(sum => s"odd_$sum").print()
    splitStream.select("even", "odd").countWindowAll(11).sum(0).map(sum => s"all_$sum").print()

    environment.execute("split_stream_test")
  }

  @Test
  def testAggregations: Unit = {
    environment.generateSequence(0, 10).map(i => (0L, i))
      .keyBy(0)
      .reduce((value1: (Long, Long), value2: (Long, Long)) => {
        (value1._1 + value2._1, value1._2 + value2._2)
      })
      //  .sum(1)
      .print()
    environment.execute("aggregations_test ")
  }

  @Test
  def testWindow: Unit = {
    environment.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    environment.generateSequence(0, 1000).assignAscendingTimestamps(_ => System.currentTimeMillis())
      .map(i => (0, i))
      .keyBy(0)
      .window(TumblingEventTimeWindows.of(Time.milliseconds(1)))
      .sum(1)
      .print()

    environment.execute("windows_test ")
  }

  @Test
  def testWindowAll: Unit = {
    environment.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    environment.generateSequence(0, 1000).assignAscendingTimestamps(_ => System.currentTimeMillis())
      .map(i => (0, i))
      .windowAll(TumblingEventTimeWindows.of(Time.milliseconds(1)))
      .sum(1)
      .print()

    environment.execute("windowsAll_test ")
  }

  @Test
  def testWindowApply: Unit = {
    environment.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    environment.generateSequence(0, 1000).assignAscendingTimestamps(_ => System.currentTimeMillis())
      .map(i => ("a", i))
      .keyBy(0)
    //  .keyBy(key=>key._1)
      .window(TumblingEventTimeWindows.of(Time.milliseconds(1)))
      .apply(new SumWindowFunction)
      .print()

    environment.execute("testWindowApply ")

  }

  @Test
  def testUnion:Unit={
    val streams = environment.fromCollection(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
      .split(num => {
        (num % 2) match {
          case 0 => List("even")
          case 1 => List("odd")
        }
      })
    val even = streams.select("even").map(num=> (s"even num is $num",num))
    val odd = streams.select("odd").map(num => (s"odd num is $num",num))
    even.union(odd).print()
    environment.execute("testUnion")
  }
}





class SumWindowFunction extends WindowFunction[(String, Long), (String, Long), Tuple, TimeWindow] {
  override def apply(key: Tuple, window: TimeWindow, input: Iterable[(String, Long)], out: Collector[(String, Long)]): Unit = {
    val keyValue:String = key.getField[String](0)
    val sum:Long = input.map(v=>v._2).sum
    val result = (keyValue,sum)
    out.collect(result)
  }
}

class StringFileInputFormat extends FileInputFormat[String] {

  override def reachedEnd(): Boolean = this.stream.getPos >= this.splitStart + this.splitLength

  override def nextRecord(reuse: String): String = {
    val data = new Array[Byte](1024)
    val read = this.stream.read(data)
    if (read == -1) throw new IllegalStateException()
    data.map(_.toChar).mkString
  }
}

