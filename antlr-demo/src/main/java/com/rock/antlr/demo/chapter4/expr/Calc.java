package com.rock.antlr.demo.chapter4.expr;

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
 * Time: 15:28
 */
public class Calc {

    public static void main(String[] args) throws IOException {
        String inputFile = null;
        if (args.length > 0) inputFile = args[0];
        InputStream inputStream = System.in;
        if (inputFile != null) {
            inputStream = new FileInputStream(inputFile);
        }
        CharStream charStream = CharStreams.fromStream(inputStream);
        ExprLexer exprLexer = new ExprLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(exprLexer);
        ExprParser exprParser = new ExprParser(tokens);
        ExprParser.ProgContext prog = exprParser.prog();
        EvalVisitor evalVisitor = new EvalVisitor();
        evalVisitor.visit(prog);
    }
}
