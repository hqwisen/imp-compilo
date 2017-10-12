//import java_cup.runtime.*; uncommet if you use CUP

%% // Options of the scanner

%class Main
%unicode
%line
%column
%type Symbol
%standalone

// Constructor code
%init{
%init}

%{
    private Symbol symbol(LexicalUnit lexicalUnit){
        String value = yytext();
        Symbol symbolObject = new Symbol(lexicalUnit, yyline, yycolumn, value);
        System.out.println(symbolObject);
        return symbolObject;
    }

%}

%eof{
%eof}


%eofval{
	return new Symbol(LexicalUnit.END, yyline, yycolumn);
%eofval}

// Extended Regular Expressions: Shortcuts
AlphaUpperCase = [A-Z]
AlphaLowerCase = [a-z]
Alpha          = {AlphaUpperCase}|{AlphaLowerCase}
Numeric        = [0-9]
AlphaNumeric   = {Alpha}|{Numeric}
Number         = ([1-9]{Numeric}*)|0
VarName        = {Alpha}{AlphaNumeric}*
NewLine        = "\n" // FIXME Windows new lines (/r) ?
Spaces         = \s* // * greedy: match as much space as possible

// States

%state YYINITIAL


"end"       {yybegin(YYINITIAL); return symbol(LexicalUnit.END);}

<YYINITIAL>{
    "begin"     {yybegin(CODE); return symbol(LexicalUnit.BEGIN);}
}

<CODE>{
    {Blank}     {yybegin(INSTLIST);}
}

<INSTLIST>{
    ";" return SEMICOLUMN
    . {pushback(1), go to INSTRUCTION}
}

<INSTRUCTION>{
    "read" go to READ
}

<READ>{
    "("     {return symbol(LexicalUnit.LPAREN);}
    {VarName}   {return symbol(LexicalUnit.VARNAME);}
    ")"     {yybegin(INSTLIST); return symbol(LexicalUnit.LPAREN);}
}
