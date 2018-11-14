package com.rock.antlr.demo.chapter6.csv;

import com.rock.antlr.demo.chapter4.rows.Col;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: yanshi
 * Date: 2018-10-30
 * Time: 17:33
 */
public class Loader extends CSVBaseListener {

    public static final String EMPTY = "";

    List<Map<String, String>> rows = new ArrayList<>();

    List<String> header;

    List<String> currentRowFieldValues;

    @Override
    public void exitString(CSVParser.StringContext ctx) {
        currentRowFieldValues.add(ctx.STRING().getText());
    }

    @Override
    public void exitText(CSVParser.TextContext ctx) {
        currentRowFieldValues.add(ctx.TEXT().getText());
    }

    @Override
    public void exitEmpty(CSVParser.EmptyContext ctx) {
        currentRowFieldValues.add(EMPTY);
    }

    @Override
    public void exitHdr(CSVParser.HdrContext ctx) {
        header = new ArrayList<>();
        header.addAll(currentRowFieldValues);
    }

    @Override
    public void enterRow(CSVParser.RowContext ctx) {
        currentRowFieldValues = new ArrayList<>();
    }

    @Override
    public void exitRow(CSVParser.RowContext ctx) {
        if(ctx.getParent().getRuleIndex() == CSVParser.RULE_hdr) return;
        Map<String,String> m = new LinkedHashMap<>();
        int i=0;
        for(String v:currentRowFieldValues){
            m.put(header.get(i),v);
            i++;
        }
        rows.add(m);
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream(Loader.class.getClassLoader().getResource("data.csv").getFile());
        CharStream charStream = CharStreams.fromStream(inputStream);
        CSVLexer lexer = new CSVLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CSVParser parser = new CSVParser(tokens);
        CSVParser.FileContext file = parser.file();
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        Loader loader = new Loader();
        parseTreeWalker.walk(loader,file);
        System.out.println(loader.rows);


    }
}
