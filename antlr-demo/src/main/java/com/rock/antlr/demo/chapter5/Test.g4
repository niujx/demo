grammar Test ;

//expr : <assoc=right> expr '^' expr
//     | expr '*' expr
//     | expr '+' expr
//     | INT
//     ;


decl : decl '[' ']'
     | '*' decl
     | '(' decl ')'
     | ID
     | INT
     | FLOAT
     ;

FLOAT : DIGIT+ '.' DIGIT*
      | '.' DIGIT+
      ;

INT  : [0-9]+ ;
ID   : [a-zA-Z]+;
STRING : '"' (ESC|.)*?  '"' ;

fragment
DIGIT : [0-9] ;
ESC : '\\"' | '\\\\'