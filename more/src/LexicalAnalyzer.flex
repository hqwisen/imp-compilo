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

%xstate CODE, INSTLIST, INSTRUCTION, READ, PRINT, EXPR_ARITH, ASSIGN
 /*, INSTLIST, INSTRUCTION, COND*/

%% // Identification of tokens and actions

<YYINITIAL>{
    // Language specifics
    "begin"        {pushCodeState(YYINITIAL);
                    changeState(INSTRUCTION);
                    return symbol(LexicalUnit.BEGIN);}
    "end"          {return symbol(LexicalUnit.END);}
}

<CODE>{
    <INSTLIST> {CodeTerminator}   {endLastCodeState();}
}

<INSTLIST>{
    ";"     {changeState(INSTRUCTION); return symbol(LexicalUnit.SEMICOLON);}
    .*      {System.out.println("[InstList] Error: " + text());}
}


<INSTRUCTION> {
    "read"      {changeState(READ); return symbol(LexicalUnit.READ);}
    "print"     {changeState(PRINT); return symbol(LexicalUnit.PRINT);}
    {VarName}   {changeState(ASSIGN); return symbol(LexicalUnit.VARNAME);}
}

<ASSIGN> {
    ":="        {changeState(EXPR_ARITH); return symbol(LexicalUnit.ASSIGN);}
}

<EXPR_ARITH> {
    {Number}    {changeState(INSTLIST); return symbol(LexicalUnit.NUMBER);}
    {VarName}   {changeState(INSTLIST); return symbol(LexicalUnit.VARNAME);}
    "-"         {return symbol(LexicalUnit.MINUS);}
    {Number}|{VarName}({Operation}{})
}


<OP> {
    "+"         {return symbol(LexicalUnit.PLUS);}
    "-"         {return symbol(LexicalUnit.MINUS);}
    "*"         {return symbol(LexicalUnit.TIMES);}
    "/"         {return symbol(LexicalUnit.DIVIDE);}

}

<READ, PRINT> {
    "("         {return symbol(LexicalUnit.LPAREN);}
    ")"         {changeState(INSTLIST); return symbol(LexicalUnit.RPAREN);}
}

<READ, PRINT> {VarName}   {return symbol(LexicalUnit.VARNAME);}


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
