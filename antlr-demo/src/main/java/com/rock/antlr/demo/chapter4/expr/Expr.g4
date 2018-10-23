grammar Expr ;
import CommonLexerRules;

prog : stat+ ;

stat : expr NEWLINE            # printExpr
     | ID '=' expr NEWLINE     # assign
     | 'delete' ID NEWLINE     # clear
     | NEWLINE                 # blank
     ;


expr : expr op=('*'|'/') expr     # MulDiv
     | expr op=('+'|'-') expr     # Addsub
     | INT                        # int
     | ID                         # id
     | '(' expr ')'               # parents
     ;


