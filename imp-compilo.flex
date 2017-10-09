//import java_cup.runtime.*; uncommet if you use CUP

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

%}

%eof{
%eof}


// Return value of the program
%eofval{
	return new Symbol(LexicalUnit.END, yyline, yycolumn);
%eofval}

// Extended Regular Expressions
AlphaUpperCase = [A-Z]
AlphaLowerCase = [a-z]
Alpha          = {AlphaUpperCase}|{AlphaLowerCase}
Numeric        = [0-9]
AlphaNumeric	 = {Alpha}|{Numeric}

Sign           = [+-]
Integer        = {Sign}?(([1-9][0-9]*)|0)
Decimal        = \.[0-9]*
Exponent       = [eE]{Integer}
Real           = {Integer}{Decimal}?{Exponent}?
Identifier     = {Alpha}{AlphaNumeric}*

// States

%xstate YYINITIAL

%% // Identification of tokens and actions

<YYINITIAL>{

	"!"		        {System.out.println("NOT: " + yytext()); return new Symbol(LexicalUnit.NOT,yyline, yycolumn);}

	// Decimal number in scientific notation
	{Real}			  {System.out.println("NUMBER: " + yytext()); return new Symbol(LexicalUnit.NUMBER,yyline, yycolumn, new Integer(yytext()));}

	// C99 variable identifier
	{Identifier}  {System.out.println("VARNAME: " + yytext()); return new Symbol(LexicalUnit.VARNAME,yyline, yycolumn, yytext());}

	// Ignore other characters
	.             {}

}
