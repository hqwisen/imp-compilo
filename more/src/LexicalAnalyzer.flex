//import java_cup.runtime.*; uncommet if you use CUP
import math;

%% // Options of the scanner

%class ImpCompilo	//Name
%unicode
%line
%column
%type Symbol
%standalone

// Constructor code
%init{
%init}

// Class code (methods and attributes)
%{
    private HasMap<String, Intenger> identifiers;

    private Symbol symbol(LexicalUnit lexicalUnit){
        value = yytext();
        Symbol symbolObject = new Symbol(lexicalUnit, yyline,
                                         yycolumn, value));
        if(lexicalUnit == LexicalUnit.VARNAME &&
            !identifiers.containsKey(value)) {
            identifiers.add(value, yyline);
            // log something
        }
        System.out.println(symbolObject);
        return symbolObject;
    }

%}

%eof{
%eof}


// Return value of the program
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

%xstate YYINITIAL

%% // Identification of tokens and actions

<YYINITIAL>{
    // Language specifics
	"begin"        {return symbol(LexicalUnit.BEGIN);}
    "end"          {return symbol(LexicalUnit.END);}

    // Assign
    {VarName}      {return symbol(LexicalUnit.VARNAME);}
    ":="           {return symbol(LexicalUnit.ASSIGN);}

    // Operations
    // TODO to test
    // "+"           {return symbol(LexicalUnit.PLUS);}
    // "-"           {return symbol(LexicalUnit.MINUS);}
    // "*"           {return symbol(LexicalUnit.TIMES);}
    // "/"           {return symbol(LexicalUnit.DIVIDE);}

    // {VarName}{Spaces}":="{Spaces}{Number} {System.out.println(yytext());}




	// Decimal number in scientific notation
	// {Number}       {System.out.println("NUMBER: " + yytext()); return new Symbol(LexicalUnit.NUMBER,yyline, yycolumn, new Integer(yytext()));}

	// C99 variable identifier
	// {Varname}   {System.out.println("VARNAME: " + yytext()); return new Symbol(LexicalUnit.VARNAME,yyline, yycolumn, yytext());}

	// Ignore other characters
	.|{NewLine}    {}

}
