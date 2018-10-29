grammar R;

prog : (expr_or_assign (';'|NL)| NL)* EOF ;

expr_or_assign : expr ('<-' | '='| '<<-') expr_or_assign
               | expr
               ;


expr : expr '[[' sublist ']' ']'
     | expr '[' sublist ']'
     | expr ('::' | ':::') expr
     | expr ('$'|'@') expr
     | expr '^'<assoc=right> expr
     | ('-'|'+') expr
     | expr ':' expr
     | expr USER_OP expr
     | expr ('*'|'/') expr
     | expr ('+'|'-') expr
     | expr ('>'|'>='|'<'|'<='|'=='|'!=') expr
     | '!' expr
     | expr ('&'|'&&') expr
     | expr ('|'|'||') expr
     | '~' expr
     | expr '~' expr
     | expr ('->' | '->>'|':=')  expr
     | 'function' '(' formlist? ')' expr
     | expr '(' sublist ')'
     ;

formlist : form (',' form)*;
sublist : sub (',' sub)* ;

sub :expr
    | ID '='
    | ID '=' expr
    | STRING '='
    | STRING '=' expr
    | 'NULL' '='
    | 'NULL' '=' expr
    | '...'
    |
    ;

form: ID
    | ID '=' expr
    | '...'
    ;

ID : '-' (LETTER | '-'|'.') (LETTER|DIGIT|'_'|'.')*
   | LETTER (LETTER|DIGIT|'_'|'.')*
   ;

STRING : LETTER ;
DIGIT : [0-9] ;
NL : '\r'? '\n' ;
fragment LETTER : [a-zA-Z] ;