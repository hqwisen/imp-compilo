//import java_cup.runtime.*; uncommet if you use CUP

import java.util.logging.Logger;
import java.util.logging.Level;


%% // Options of the scanner

%class Main
// %public
%extends ImpCompilo
%unicode
%line
%column
%type Symbol
%standalone

// Constructor code
// Class code (methods and attributes)
%{
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

    public void pushback(int number){
        yypushback(number);
    }

    public void changeState(int state){
        yybegin(state);
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

// Code states

CodeTerminator = "end"

// States

%xstate CODE, INSTLIST
 /*, INSTLIST, INSTRUCTION, COND*/

%% // Identification of tokens and actions



<YYINITIAL>{
    // Language specifics
    "begin"        {startState(YYINITIAL, CODE); return symbol(LexicalUnit.BEGIN);}
    "end"          {return symbol(LexicalUnit.END);}
}

<CODE>{
    {CodeTerminator}   {endState();}
}

<INSTLIST>{
    ";"     {return symbol(LexicalUnit.SEMICOLON);}
}

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
