grammar JSON ;

json : object
     | array
     ;


object : '{' pair (',' pair)* '}'
       | '{' '}'
       ;

pair : STRING ':' value ;


array : '[' value (',' value)* ']'
      | '[' ']'
      ;

value : STRING
      | NUMBER
      | object
      | array
      | 'true'
      | 'false'
      | 'null'
      ;

STRING : '"' (ESC |~["\\])* '"' ;
NUMBER : '-'? INT '.' INT EXP?
       | '-' INT EXP
       | '-'? INT
       ;
WS : [ \t\n\r]+ -> skip ;

fragment EXP : [Ee] [+\-]? INT ;
fragment INT  : '0' | [1-9][0-9]* ;
fragment ESC : '\\' (["\\/bfnrt] | UNICODE) ;
fragment UNICODE : 'u' HEX HEX HEX HEX ;
fragment HEX : [0-9a-fA-F] ;