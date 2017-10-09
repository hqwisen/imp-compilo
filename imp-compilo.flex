//import java_cup.runtime.*; uncommet if you use CUP

%% // Options of the scanner

%class ImpCompilo	//Name
%unicode
%line
%column
%standalone

// Constructor code
%init{
	System.out.println("Initialization");
%init}

// Class code (methods and attributes)
%{

%}

%eof{
   System.out.println("Done");
%eof}


// Regular Expressions
EndOfLine = "\r"?"\n"


// States

%xstate YYINITIAL,PRINT

%% // Identification of tokens and actions

<YYINITIAL>{
   {EndOfLine} {yybegin(PRINT);}
   .           {} //by default, all non matched char are printed on output
                  //we force to not print them
}

<PRINT>{
	{EndOfLine} {yybegin(YYINITIAL);}
	.           {System.out.println(yytext());} //we print them explicitly
}
