grammar Tuga;

prog   : stat+ EOF; //mudar

stat   : 'escreve' expr NEWLINE; //mudar

expr   : LPAREN expr RPAREN                     # Parens
       | <assoc=right> expr POW expr            # Pow //não aparece no trabalho
       | op=(MINUS|NOT) expr                    # Unary
       | expr op=(MULT|DIV|MOD) expr            # MulDivMod
       | expr op=(ADD|MINUS) expr               # AddSub
       | expr op=(LT|GT|LET|GET) expr           # Relational
       | expr op=(EQ|NEQ) expr                  #Equality
       | expr AND expr                          #And
       | expr OR expr                           #Or
       | INT                                    # Int
       | DOUBLE                                 # Double
       | STRING                                 #String
       | BOOLEAN                                #Boolean
       ;

INT: DIGIT+ ;
DOUBLE: DIGIT+'.'DIGIT+ ;
STRING: '"' ('\\"'|.)*? '"' ;
BOOLEAN: TRUE | FALSE ;

TRUE: 'true' ;
FALSE: 'false' ;

LPAREN: '(' ;
RPAREN: ')' ;
MINUS: '-' ;
NOT: '!' ;
POW: '^' ;
MULT: '*' ;
DIV: '/' ;
MOD: '%' ;
ADD: '+' ;
LT: '<' ;
GT: '>' ;
LET: '<=' ;
GET: '>=' ;
EQ: '==' ;
NEQ: '!=' ;
AND: '&&' ;
OR: '||' ;





NEWLINE  : '\r'? '\n' ;
WS       : [ \t\r\n]+ -> skip ;
SL_COMMENT : '//' .*? (EOF|'\n') -> skip;
ML_COMMENT : '/*' .*? '*/' -> skip ;

fragment
DIGIT    : [0-9] ;
LETTER: [A-Za-z];


//TESTE CARALHO
