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

    private Symbol symbol(LexicalUnit lexicalUnit){
        Symbol symbolObject = new Symbol(lexicalUnit, yyline,
                                         yycolumn, yytext());
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
NewLine        = "\n"

// States

%xstate YYINITIAL

%% // Identification of tokens and actions

<YYINITIAL>{

	"begin"        {return symbol(LexicalUnit.BEGIN);}
    "end"          {return symbol(LexicalUnit.END);}

	// Decimal number in scientific notation
	// {Number}       {System.out.println("NUMBER: " + yytext()); return new Symbol(LexicalUnit.NUMBER,yyline, yycolumn, new Integer(yytext()));}

	// C99 variable identifier
	// {Varname}   {System.out.println("VARNAME: " + yytext()); return new Symbol(LexicalUnit.VARNAME,yyline, yycolumn, yytext());}

	// Ignore other characters
	.|{NewLine}    {}

}
