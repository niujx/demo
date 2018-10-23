package com.rock.antlr.demo.chapter4.expr;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: yanshi
 * Date: 2018-10-23
 * Time: 15:35
 */
public class EvalVisitor extends ExprBaseVisitor<Integer> {
    Map<String, Integer> memory = new HashMap<>();

    @Override
    public Integer visitAssign(ExprParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        int value = visit(ctx.expr());
        memory.put(id, value);
        return value;
    }

    @Override
    public Integer visitPrintExpr(ExprParser.PrintExprContext ctx) {
        Integer value = visit(ctx.expr());
        System.out.println(value);
        return 0;
    }

    @Override
    public Integer visitInt(ExprParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    @Override
    public Integer visitId(ExprParser.IdContext ctx) {
        String id = ctx.ID().getText();
        if (memory.containsKey(id)) return memory.get(id);
        return 0;
    }

    @Override
    public Integer visitAddsub(ExprParser.AddsubContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        if (ctx.op.getType() == ExprParser.ADD) return left + right;
        return left - right;
    }

    @Override
    public Integer visitMulDiv(ExprParser.MulDivContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        if (ctx.op.getType() == ExprParser.MUL) return left * right;
        return left / right;
    }

    @Override
    public Integer visitParents(ExprParser.ParentsContext ctx) {
        return visit(ctx.expr());
    }
}
