package com.rock.graphx.demo;

import com.google.common.collect.Maps;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.neo4j.spark.Neo4JavaSparkContext;

import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: yanshi
 * Date: 2018-11-17
 * Time: 15:04
 */
public class SparkGraphXConnectorTest {

    private static SparkConf conf;
    private static JavaSparkContext sc;
    private static Neo4JavaSparkContext csc;

    public static void main(String[] args) {

        conf = new SparkConf()
                .setAppName("neoTest")
                .setMaster("local[*]")
                .set("spark.driver.allowMultipleContexts", "true")
                .set("spark.neo4j.bolt.url", "bolt://10.152.9.32")
                .set("spark.neo4j.bolt.user", "neo4j")
                .set("spark.neo4j.bolt.password", "wLOeAbtgNG74yqn");
        sc = new JavaSparkContext(conf);
        csc = Neo4JavaSparkContext.neo4jContext(sc);
        JavaRDD<Map<String, Object>> query = csc.query("MATCH (n:Person) RETURN n LIMIT 25", Maps.newHashMap());
        List<Object> requestId = query.map(person -> person.get("requestId")).collect();
        System.out.println(requestId);
    }
}
