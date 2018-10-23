package com.rock.antlr.demo.chapter4.rows;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: yanshi
 * Date: 2018-10-23
 * Time: 18:19
 */
public class Col {

    public static void main(String[] args) throws IOException {
        InputStream   inputStream = new FileInputStream(Col.class.getResource("t.rows").getFile());
        CharStream charStream = CharStreams.fromStream(inputStream);
        RowsLexer lexer = new RowsLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RowsParser parser = new RowsParser(tokens,3);
        parser.setBuildParseTree(false);
        parser.file();
    }
}
