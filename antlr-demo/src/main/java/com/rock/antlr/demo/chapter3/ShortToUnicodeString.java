package com.rock.antlr.demo.chapter3;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: yanshi
 * Date: 2018-10-22
 * Time: 18:44
 */
public class ShortToUnicodeString extends ArrayInitBaseListener {

    @Override
    public void enterInit(ArrayInitParser.InitContext ctx) {
        System.out.print('"');
    }

    @Override
    public void exitInit(ArrayInitParser.InitContext ctx) {
        System.out.print('"');
    }

    @Override
    public void enterValue(ArrayInitParser.ValueContext ctx) {
        int value = Integer.valueOf(ctx.INT().getText());
        System.out.printf("\\u%04x",value);
    }
}
