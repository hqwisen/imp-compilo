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

// Class code (methods and attributes)
%{
    // private HasMap<String, Integer> identifiers;

    private Symbol symbol(LexicalUnit lexicalUnit){
        String value = yytext();
        Symbol symbolObject = new Symbol(lexicalUnit, yyline, yycolumn, value);
        /*if(lexicalUnit == LexicalUnit.VARNAME && !identifiers.containsKey(value)) {
            identifiers.add(value, yyline);
            // log something
        }*/
        System.out.println(symbolObject);
        return symbolObject;
    }

%}

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
NewLine        = "\n" // FIXME Windows new lines (/r) ?
Spaces         = \s* // * greedy: match as much space as possible

// States

%xstate YYINITIAL, CODE, INSTLIST, INSTRUCTION

%% // Identification of tokens and actions

<YYINITIAL>{
    // Language specifics
	  "begin"        {yybegin(CODE); return symbol(LexicalUnit.BEGIN);}
      // FIXME where to put the end ? in CODE or INITIAL ?
      "end"          {return symbol(LexicalUnit.END);}

    // Assign
    // {VarName}      {return symbol(LexicalUnit.VARNAME);}
    // ":="           {return symbol(LexicalUnit.ASSIGN);}

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

<CODE>{
    // "="            {return symbol(LexicalUnit.ASSIGN);}
    // FIXME INITIAL or generate error ?
    // NOTE beginend.imp: show END because of the NewLine RE
    .             {}
    {NewLine}     {yybegin(INSTLIST);}
}

<INSTLIST>{
    .|{NewLine}     {yybegin(YYINITIAL);}
}

<INSTRUCTION>{
    ":="            {return symbol(LexicalUnit.ASSIGN);}
    .|{NewLine}     {yybegin(YYINITIAL);}
}

<ASSIGN>{
    .|{NewLine}     {yybegin(YYINITIAL);}
}

<IF>{
    .|{NewLine}     {yybegin(YYINITIAL);}
}

<WHILE>{
    .|{NewLine}     {yybegin(YYINITIAL);}
}

<FOR>{
    .|{NewLine}     {yybegin(YYINITIAL);}
}

<PRINT>{
    .|{NewLine}     {yybegin(YYINITIAL);}
}

<READ>{
    .|{NewLine}     {yybegin(YYINITIAL);}
}
