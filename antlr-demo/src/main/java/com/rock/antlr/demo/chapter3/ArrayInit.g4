grammar ArrayInit;

init : '{' value (',' value) * '}' ;

value : init
      | INT
      ;

INT : [0-9]+ ;
SW  : [ \t\r\n]+ -> skip ;