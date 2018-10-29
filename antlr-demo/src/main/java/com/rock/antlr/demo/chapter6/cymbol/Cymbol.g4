grammar Cymbol ;

file : (functionDecl |varDecl)+ ;

varDecl : type ID ('=' expr)? ';' ;

type : 'float'
     | 'int'
     | 'void' ;


functionDecl : type ID '(' formatParameters? ')' block ;
formatParameters : formatParameter (',' formatParameter)* ;
formatParameter : type ID ;

block : '{' stat* '}' ;
stat  : block
      | varDecl
      | 'if' expr 'then' stat ('else' stat)?
      | 'return' expr? ';'
      | expr '=' expr ';'
      | expr ';'
      ;

expr : ID '(' exprList? ')'
     | expr '[' expr ']'
     | '-' expr
     | '!' expr
     | expr '*' expr
     | expr ('+'|'-') expr
     |expr '==' expr
     | ID
     | INT
     | '(' expr ')'
     ;

exprList : expr (',' expr)* ;


ID : LETTER (LETTER|DIGIT)* ;
INT : DIGIT ;
WS : [ \t\n]+ -> skip ;

fragment DIGIT : [0-9];
fragment LETTER : [a-zA-Z] ;