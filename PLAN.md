PLAN

GRAMMAR

Explain Grammar Transformation (each step) (a) (b) (c)
    - How it is reachable ? check algo p 108
//    - Improved explaination of disjonction and conjonction explaination
    - Explain that arith are separated from boolean

// Explain First and Follow and how it is used for action table

Give the Action Table: explain produce, accept, match, error


IMPLEMENTATION

Introduce improvements made in the scanner

// - UnknownTokenException
// - . vs [^]
// - Instead of running Generated, we created a method scan to call the
 scanner in the parser
 - Update of LexicalUnit

How is the action table implemented in the parser

How is the grammar implemented in the code

How is epislon implemented in csv and in files, and also why it is ignored
in buildGrammar, explained that epsilon is not a token

How error are handled (from the scanner to the parser)
  -  UnkownToken (section scanner improvements)
  - SyntaxError
  - expected token explaination

How is the parser implemented in the code:
    - Use of the stack
    - Use of scan symbols and nextToken
    - PRODUCE? ACCEPT, SYNTAXERROR, MATCH RULES

Output:
  - leftmost
  - derivation

HOW TO

How to run the scanner

How to run the parser - leftmost

How to run the parser - derivation tree
