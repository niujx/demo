// Generated from /Users/yanshi/work/mywork/demo/antlr-demo/src/main/java/com/rock/antlr/demo/chapter5/Test.g4 by ANTLR 4.7
package com.rock.antlr.demo.chapter5;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TestParser}.
 */
public interface TestListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TestParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(TestParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(TestParser.ExprContext ctx);
}