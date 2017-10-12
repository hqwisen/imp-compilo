//import java_cup.runtime.*; uncommet if you use CUP

import java.util.logging.Logger;
import java.util.logging.Level;


%% // Options of the scanner

%class Main
%unicode
%line
%column
%type Symbol
%standalone

// Constructor code
// Class code (methods and attributes)
%{
    private Logger log = Logger.getLogger("ImpCompilo");

    private Symbol symbol(LexicalUnit lexicalUnit){
        String value = yytext();
        Symbol symbolObject = new Symbol(lexicalUnit, yyline, yycolumn, value);
        /*if(lexicalUnit == LexicalUnit.VARNAME && !identifiers.containsKey(value)) {
            identifiers.add(value, yyline);
            // log something
        }*/
        System.out.println(symbolObject);
        new States().print();
        return symbolObject;
    }

%}

%init{
%init}



%eof{
%eof}


// Return value of the program
%eofval{
  // FIXME is eofval return necessary ?
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
LineTerminator = \r|\n|\r\n
Spaces         = \s* // * greedy: match as much space as possible
Blank          = {Spaces} // \s matches also the new line character

// States

%xstate CODE, INSTLIST, INSTRUCTION, COND

%% // Identification of tokens and actions



<YYINITIAL>{
    // Language specifics
    "begin"        {yybegin(CODE); return symbol(LexicalUnit.BEGIN);}
    "end"          {return symbol(LexicalUnit.END);}
}

/*/<CODE>{

}*/
/*
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
}*/
