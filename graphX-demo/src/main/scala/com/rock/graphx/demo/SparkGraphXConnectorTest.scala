package com.rock.graphx.demo

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.neo4j.driver.internal.InternalNode
import org.neo4j.spark.Neo4j

/**
  * Created with IntelliJ IDEA.
  * Description: 
  * User: yanshi
  * Date: 2018-11-17
  * Time: 16:15
  */
object SparkGraphXConnectorTest {

  def main(args: Array[String]): Unit = {

    //创建spark 配置
    val conf = new SparkConf()
      .setAppName("neoTest")
      //设置本地模式 cup 使用数量实际数量
      .setMaster("local[*]")
      //连接neo4j的配置
      .set("spark.driver.allowMultipleContexts", "true")
      .set("spark.neo4j.bolt.url", "bolt://10.152.9.32")
      .set("spark.neo4j.bolt.user", "neo4j")
      .set("spark.neo4j.bolt.password", "wLOeAbtgNG74yqn")
    val sc = new JavaSparkContext(conf)
    val neo4j = Neo4j(sc)
    //rdd 操作
    //查询neo4j 数据返回rdd
    val rdd = neo4j.cypher("MATCH (n:Person) RETURN n limit 25").loadRowRdd

    //惰性求值
    val personRdd = rdd.map(row => {
      val map = row.get(0).asInstanceOf[InternalNode]
      new Person(map.get("requestId").asString(),
        map.get("birthday").asString(),
        map.get("isBlackList").asBoolean(),
        map.get("idType").asString(),
        map.get("gender").asString(),
        map.get("name").asString(),
        map.get("idNo").asString(),
        map.get("age").asInt())
    })

    //取得年龄大于30的人
    personRdd.filter(p => p.age > 30).foreach(println(_))

    //加载图
    import org.apache.spark.graphx._
    val graphQuery = "Match (p:Person)-[r:person_owner_apply]->(a:Apply) return id(p) as source,id(a) as target ,type(r) as value  limit 200 "
    val graph: Graph[Long, String] = neo4j.rels(graphQuery).loadGraph
    graph.edges.foreach(println(_))

  }

  /**
    * "birthday": "19921208",
    * "isBlackList": false,
    * "idType": "身份证",
    * "gender": "男",
    * "createTime": "2018-03-22 11:39:21",
    * "requestId": "Person@362302199212080553",
    * "name": "黄辉",
    * "idNo": "362302199212080553",
    * "age": 27
    *
    * @param requestId
    */
  case class Person(val requestId: String,
                    val birthday: String,
                    val isBlackList: Boolean,
                    val idType: String,
                    val gender: String,
                    val name: String,
                    val idNo: String,
                    val age: Int)

}
