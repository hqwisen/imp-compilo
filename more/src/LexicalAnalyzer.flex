//import java_cup.runtime.*; uncommet if you use CUP

%% // Options of the scanner

%class GeneratedScanner
%public
%extends Scanner
%unicode
%line
%column
%type Symbol
%standalone

%{
    // Implementation of ImpCompilo abstract methods

    public int column(){
        return yycolumn;
    }

    public int line(){
        return yyline;
    }

    public String text(){
        return yytext();
    }

    public int length(){
        return yylength();
    }

    public void changeState(int state){
        yybegin(state);
    }

    public boolean atEOF(){
        return this.zzAtEOF;
    }

    public Symbol lex() throws java.io.IOException{
        return this.yylex();
    }
%}

%init{
%init}



%eof{
%eof}


%eofval{
  return new Symbol(LexicalUnit.EOS, yyline, yycolumn);
%eofval}

// Extended Regular Expressions: Shortcuts
AlphaUpperCase = [A-Z]
AlphaLowerCase = [a-z]
Alpha          = {AlphaUpperCase}|{AlphaLowerCase}
Numeric        = [0-9]
AlphaNumeric   = {Alpha}|{Numeric}
Number         = ([1-9]{Numeric}*)|0
VarName        = {Alpha}{AlphaNumeric}*
// \s+ * greedy: match as much space as possible
// \s matches also the new line character
Blank          = \s+
%xstate COMMENT

%% // Identification of tokens and actions

<YYINITIAL>{
    // Comment
    "(*"            {changeState(COMMENT);}

    // Language specifics
    ";"             {return symbol(LexicalUnit.SEMICOLON);}
    ":="            {return symbol(LexicalUnit.ASSIGN);}
    "("             {return symbol(LexicalUnit.LPAREN);}
    ")"             {return symbol(LexicalUnit.RPAREN);}


    "+"             {return symbol(LexicalUnit.PLUS);}
    "-"             {return symbol(LexicalUnit.MINUS);}
    "*"             {return symbol(LexicalUnit.TIMES);}
    "/"             {return symbol(LexicalUnit.DIVIDE);}

    "if"             {return symbol(LexicalUnit.IF);}
    "then"             {return symbol(LexicalUnit.THEN);}
    "endif"             {return symbol(LexicalUnit.ENDIF);}
    "else"             {return symbol(LexicalUnit.ELSE);}

    "while"             {return symbol(LexicalUnit.WHILE);}

    "for"             {return symbol(LexicalUnit.FOR);}
    "from"             {return symbol(LexicalUnit.FROM);}
    "by"             {return symbol(LexicalUnit.BY);}
    "to"             {return symbol(LexicalUnit.TO);}

    "do"             {return symbol(LexicalUnit.DO);}
    "done"             {return symbol(LexicalUnit.DONE);}

    "not"             {return symbol(LexicalUnit.NOT);}

    "and"             {return symbol(LexicalUnit.AND);}
    "or"             {return symbol(LexicalUnit.OR);}


    "="             {return symbol(LexicalUnit.EQ);}
    ">="             {return symbol(LexicalUnit.GEQ);}
    ">"             {return symbol(LexicalUnit.GT);}
    "<="             {return symbol(LexicalUnit.LEQ);}
    "<"             {return symbol(LexicalUnit.LT);}
    "<>"             {return symbol(LexicalUnit.NEQ);}


    "print"             {return symbol(LexicalUnit.PRINT);}
    "read"             {return symbol(LexicalUnit.READ);}

    "begin"        {return symbol(LexicalUnit.BEGIN);}
    "end"          {return symbol(LexicalUnit.END);}

    {VarName}      {return symbol(LexicalUnit.VARNAME);}
    {Number}       {return symbol(LexicalUnit.NUMBER);}

    {Blank}        {} // Blank (space and new lines) are ignored
    [^]            {throw new UnknownTokenException("Error: Unknown token '" + text() +
                    "' in line " + (line() + 1));}
}

<COMMENT>{
    "*)"    {changeState(YYINITIAL);}
    [^]|{Blank}       {} // Ignoring all characters
}
