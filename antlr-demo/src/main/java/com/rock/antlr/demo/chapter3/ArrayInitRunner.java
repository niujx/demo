package com.rock.antlr.demo.chapter3;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: yanshi
 * Date: 2018-10-22
 * Time: 18:33
 */
public class ArrayInitRunner {

    public static void main(String[] args) throws IOException {
        CharStream charStream = CharStreams.fromStream(System.in);

        ArrayInitLexer lexer = new ArrayInitLexer(charStream);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        ArrayInitParser parser = new ArrayInitParser(tokens);

        ArrayInitParser.InitContext init = parser.init();
        System.out.println(init.toStringTree(parser));
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ShortToUnicodeString(),init);
        System.out.println();
    }
}
