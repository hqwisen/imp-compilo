public enum LexicalUnit{
    VARNAME("[VarName]", false),
    NUMBER("[Number]", false),
    BEGIN("begin"),
    END("end"),
    SEMICOLON(";"),
    ASSIGN(":="),
    LPAREN("("),
    RPAREN(")"),
    MINUS("-", false),
    PLUS("+", false),
    TIMES("*", false),
    DIVIDE("/", false),
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
    private boolean informative;

    LexicalUnit(String value){
        this(value, true);
    }


    LexicalUnit(String value, boolean informative){
        this.value = value;
        this.informative = informative;
    }

    public String getValue(){
        return this.value;
    }

    public boolean isInformative(){
        return this.informative;
    }

    public static LexicalUnit unitFromValue(String value){
        for(LexicalUnit unit : LexicalUnit.values()){
            if (value.equals(unit.getValue())){
                return unit;
            }
        }
        throw new ImpCompiloException("Cannot get LexicalUnit from value '" + value + "'.");
    }
}
