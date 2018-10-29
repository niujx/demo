grammar DOT ;

graph : STRICT? (GRAPH |DIGRAPH) id? '{' stmt_list '}' ;
stmt_list   : (stmt ';'? )* ;
stmt        : node_stmt
            | edge_stmt
            | attr_stmt
            | id '=' id
            | subgraph
            ;

attr_stmt : (GRAPH|NODE|EDGE) attr_list ;
attr_list : ('[' a_list? ']')+ ;
a_list : (id ('=' id)? ','?)+ ;
edge_stmt : (node_id|subgraph) edgeRHS attr_list? ;
ddgeRHS   : (edgeop (node_id| subgraph))+ ;
degeop :  '->' | '--' ;
node_stmt : node_id attr_list? ;
node_id : id port? ;
port   : ':' id (':' id)? ;

subgraph : (SUBGRAPH id?)? '{' stmt_list '}';
id         : ID
           | STRING
           | HTML_STRING
           | NUMBER
           ;

STRICT : [Ss][Tt][Rr][Ii][Cc][Tt] ;
GRAPH : [Gg][Rr][Aa][Pp][Hh] ;
DIGRAPH : [Dd][Ii][Gg][Rr][Aa][Pp][Hh] ;
NODE: [Nn][Oo][Dd][Ee] ;
EDGE: [Ee][Dd][Gg][Ee] ;
SUBGRAPH : [Ss][Uu][Bb][Gg][Rr][Aa][Pp][Hh];
ID : LETTER (LETTER|DIGIT)* ;
NUMBER : '-'? ('.' DIGIT+ |DIGIT+ ('.' DIGIT*)? );
STRING : '"' ('\\"'|.)* '"' ;
HTML_STRING : '<' (TAG|~[<>])* '>' ;
PREPROC : '#' .*? '\n' -> skip ;

fragment TAG : '<' .*? '>' ;
fragment DIGIT : [0-9];
fragment LETTER : [a-zA-Z\u0001-\u00FF] ;


