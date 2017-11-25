public enum LexicalUnit{
    VARNAME("[VarName]"),
    NUMBER("[Number]"),
    BEGIN("begin"),
    END("end"),
    SEMICOLON(";"),
    ASSIGN(":="),
    LPAREN("("),
    RPAREN(")"),
    MINUS("-"),
    PLUS("+"),
    TIMES("*"),
    DIVIDE("/"),
    IF("if"),
    THEN("then"),
    ENDIF("endif"),
    ELSE("else"),
    NOT("not"),
    AND("and"),
    OR("or"),
    EQ("="),
    GEQ(">="),
    GT(">"),
    LEQ("<="),
    LT("<"),
    NEQ("<>"),
    WHILE("while"),
    DO("do"),
    DONE("done"),
    FOR("for"),
    FROM("from"),
    BY("by"),
    TO("to"),
    PRINT("print"),
    READ("read"),
    EOS("EOS");

    private String value;

    LexicalUnit(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
