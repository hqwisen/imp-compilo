/* The following code was generated by JFlex 1.6.1 */

//import java_cup.runtime.*; uncommet if you use CUP


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.6.1
 * from the specification file <tt>src/LexicalAnalyzer.flex</tt>
 */
public class GeneratedScanner extends Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int COMMENT = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1, 1
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\5\4\22\0\1\4\7\0\1\5\1\12\1\6\1\13\1\0"+
    "\1\14\1\0\1\15\1\2\11\3\1\10\1\7\1\37\1\11\1\36"+
    "\2\0\32\1\6\0\1\35\1\33\1\1\1\24\1\22\1\17\1\41"+
    "\1\21\1\16\2\1\1\25\1\32\1\23\1\30\1\40\1\1\1\31"+
    "\1\26\1\20\2\1\1\27\1\1\1\34\1\1\12\0\1\4\32\0"+
    "\1\4\u15df\0\1\4\u097f\0\13\4\35\0\2\4\5\0\1\4\57\0"+
    "\1\4\u0fa0\0\1\4\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\ud00f\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\1\2\2\3\1\4\1\5\1\6\1\7"+
    "\1\1\1\10\1\11\1\12\1\13\1\14\13\2\1\15"+
    "\1\16\1\2\2\4\1\17\1\20\1\21\3\2\1\22"+
    "\3\2\1\23\1\2\1\24\2\2\1\25\1\2\1\26"+
    "\1\27\1\30\1\2\1\31\1\32\2\2\1\33\1\2"+
    "\1\34\4\2\1\35\1\2\1\36\1\37\1\2\1\40"+
    "\1\41\1\2\1\42\2\2\1\43\1\44\1\45\1\46";

  private static int [] zzUnpackAction() {
    int [] result = new int[79];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\42\0\104\0\146\0\104\0\210\0\252\0\314"+
    "\0\104\0\104\0\356\0\104\0\104\0\104\0\104\0\104"+
    "\0\u0110\0\u0132\0\u0154\0\u0176\0\u0198\0\u01ba\0\u01dc\0\u01fe"+
    "\0\u0220\0\u0242\0\u0264\0\u0286\0\u02a8\0\u02ca\0\104\0\u02ec"+
    "\0\104\0\104\0\146\0\u030e\0\u0330\0\u0352\0\146\0\u0374"+
    "\0\u0396\0\u03b8\0\u03da\0\u03fc\0\146\0\u041e\0\u0440\0\146"+
    "\0\u0462\0\104\0\104\0\104\0\u0484\0\104\0\146\0\u04a6"+
    "\0\u04c8\0\u04ea\0\u050c\0\146\0\u052e\0\u0550\0\u0572\0\u0594"+
    "\0\146\0\u05b6\0\146\0\146\0\u05d8\0\146\0\146\0\u05fa"+
    "\0\146\0\u061c\0\u063e\0\146\0\146\0\146\0\146";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[79];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12"+
    "\1\13\1\14\1\15\1\16\1\17\1\20\1\21\1\22"+
    "\1\23\1\4\1\24\1\25\1\26\2\4\1\27\1\30"+
    "\1\31\1\4\1\32\1\4\1\33\1\34\1\35\1\36"+
    "\1\4\4\37\1\7\1\37\1\40\33\37\43\0\3\4"+
    "\12\0\20\4\2\0\2\4\2\0\2\6\42\0\1\7"+
    "\43\0\1\41\44\0\1\42\31\0\3\4\12\0\1\4"+
    "\1\43\16\4\2\0\2\4\1\0\3\4\12\0\12\4"+
    "\1\44\1\45\4\4\2\0\2\4\1\0\3\4\12\0"+
    "\3\4\1\46\6\4\1\47\5\4\2\0\2\4\1\0"+
    "\3\4\12\0\5\4\1\50\1\4\1\51\10\4\2\0"+
    "\2\4\1\0\3\4\12\0\12\4\1\52\5\4\2\0"+
    "\2\4\1\0\3\4\12\0\12\4\1\53\5\4\2\0"+
    "\2\4\1\0\3\4\12\0\3\4\1\54\14\4\2\0"+
    "\2\4\1\0\3\4\12\0\13\4\1\55\4\4\2\0"+
    "\2\4\1\0\3\4\12\0\4\4\1\56\13\4\2\0"+
    "\2\4\1\0\3\4\12\0\4\4\1\57\11\4\1\60"+
    "\1\4\2\0\2\4\1\0\3\4\12\0\5\4\1\61"+
    "\12\4\2\0\2\4\11\0\1\62\41\0\1\63\24\0"+
    "\1\64\4\0\3\4\12\0\13\4\1\65\4\4\2\0"+
    "\2\4\12\0\1\66\30\0\3\4\12\0\13\4\1\67"+
    "\4\4\2\0\2\4\1\0\3\4\12\0\12\4\1\70"+
    "\5\4\2\0\2\4\1\0\3\4\12\0\4\4\1\71"+
    "\13\4\2\0\2\4\1\0\3\4\12\0\6\4\1\72"+
    "\11\4\2\0\2\4\1\0\3\4\12\0\10\4\1\73"+
    "\7\4\2\0\2\4\1\0\3\4\12\0\2\4\1\74"+
    "\15\4\2\0\2\4\1\0\3\4\12\0\5\4\1\75"+
    "\12\4\2\0\2\4\1\0\3\4\12\0\1\76\17\4"+
    "\2\0\2\4\1\0\3\4\12\0\17\4\1\77\2\0"+
    "\2\4\1\0\3\4\12\0\20\4\2\0\1\4\1\100"+
    "\1\0\3\4\12\0\6\4\1\101\11\4\2\0\2\4"+
    "\1\0\3\4\12\0\1\102\17\4\2\0\2\4\1\0"+
    "\3\4\12\0\14\4\1\103\3\4\2\0\2\4\1\0"+
    "\3\4\12\0\5\4\1\104\12\4\2\0\2\4\1\0"+
    "\3\4\12\0\1\105\17\4\2\0\2\4\1\0\3\4"+
    "\12\0\4\4\1\106\13\4\2\0\2\4\1\0\3\4"+
    "\12\0\4\4\1\107\13\4\2\0\2\4\1\0\3\4"+
    "\12\0\7\4\1\110\10\4\2\0\2\4\1\0\3\4"+
    "\12\0\6\4\1\111\11\4\2\0\2\4\1\0\3\4"+
    "\12\0\1\112\17\4\2\0\2\4\1\0\3\4\12\0"+
    "\5\4\1\113\12\4\2\0\2\4\1\0\3\4\12\0"+
    "\1\4\1\114\16\4\2\0\2\4\1\0\3\4\12\0"+
    "\4\4\1\115\13\4\2\0\2\4\1\0\3\4\12\0"+
    "\5\4\1\116\12\4\2\0\2\4\1\0\3\4\12\0"+
    "\2\4\1\117\15\4\2\0\2\4";

  private static int [] zzUnpackTrans() {
    int [] result = new int[1632];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\1\11\1\1\1\11\3\1\2\11\1\1\5\11"+
    "\16\1\1\11\1\1\2\11\17\1\3\11\1\1\1\11"+
    "\31\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[79];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /* user code: */
    // Implementation of ImpCompilo abstract methods

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

    public void changeState(int state){
        yybegin(state);
    }

    public boolean atEOF(){
        return this.zzAtEOF;
    }

    public Symbol lex() throws java.io.IOException{
        return this.yylex();
    }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public GeneratedScanner(java.io.Reader in) {
      this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 160) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      throw new java.io.IOException("Reader returned 0 characters. See JFlex examples for workaround.");
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      /* If numRead == requested, we might have requested to few chars to
         encode a full Unicode character. We assume that a Reader would
         otherwise never return half characters. */
      if (numRead == requested) {
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public Symbol yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
            zzDoEOF();
          {   return new Symbol(LexicalUnit.EOS, yyline, yycolumn);
 }
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { throw new UnknownTokenException("Error: Unknown token '" + text() +
                    "' in line " + (line() + 1));
            }
          case 39: break;
          case 2: 
            { return symbol(LexicalUnit.VARNAME);
            }
          case 40: break;
          case 3: 
            { return symbol(LexicalUnit.NUMBER);
            }
          case 41: break;
          case 4: 
            { 
            }
          case 42: break;
          case 5: 
            { return symbol(LexicalUnit.LPAREN);
            }
          case 43: break;
          case 6: 
            { return symbol(LexicalUnit.TIMES);
            }
          case 44: break;
          case 7: 
            { return symbol(LexicalUnit.SEMICOLON);
            }
          case 45: break;
          case 8: 
            { return symbol(LexicalUnit.EQ);
            }
          case 46: break;
          case 9: 
            { return symbol(LexicalUnit.RPAREN);
            }
          case 47: break;
          case 10: 
            { return symbol(LexicalUnit.PLUS);
            }
          case 48: break;
          case 11: 
            { return symbol(LexicalUnit.MINUS);
            }
          case 49: break;
          case 12: 
            { return symbol(LexicalUnit.DIVIDE);
            }
          case 50: break;
          case 13: 
            { return symbol(LexicalUnit.GT);
            }
          case 51: break;
          case 14: 
            { return symbol(LexicalUnit.LT);
            }
          case 52: break;
          case 15: 
            { changeState(COMMENT);
            }
          case 53: break;
          case 16: 
            { return symbol(LexicalUnit.ASSIGN);
            }
          case 54: break;
          case 17: 
            { return symbol(LexicalUnit.IF);
            }
          case 55: break;
          case 18: 
            { return symbol(LexicalUnit.TO);
            }
          case 56: break;
          case 19: 
            { return symbol(LexicalUnit.DO);
            }
          case 57: break;
          case 20: 
            { return symbol(LexicalUnit.OR);
            }
          case 58: break;
          case 21: 
            { return symbol(LexicalUnit.BY);
            }
          case 59: break;
          case 22: 
            { return symbol(LexicalUnit.GEQ);
            }
          case 60: break;
          case 23: 
            { return symbol(LexicalUnit.LEQ);
            }
          case 61: break;
          case 24: 
            { return symbol(LexicalUnit.NEQ);
            }
          case 62: break;
          case 25: 
            { changeState(YYINITIAL);
            }
          case 63: break;
          case 26: 
            { return symbol(LexicalUnit.FOR);
            }
          case 64: break;
          case 27: 
            { return symbol(LexicalUnit.END);
            }
          case 65: break;
          case 28: 
            { return symbol(LexicalUnit.NOT);
            }
          case 66: break;
          case 29: 
            { return symbol(LexicalUnit.AND);
            }
          case 67: break;
          case 30: 
            { return symbol(LexicalUnit.FROM);
            }
          case 68: break;
          case 31: 
            { return symbol(LexicalUnit.THEN);
            }
          case 69: break;
          case 32: 
            { return symbol(LexicalUnit.ELSE);
            }
          case 70: break;
          case 33: 
            { return symbol(LexicalUnit.DONE);
            }
          case 71: break;
          case 34: 
            { return symbol(LexicalUnit.READ);
            }
          case 72: break;
          case 35: 
            { return symbol(LexicalUnit.ENDIF);
            }
          case 73: break;
          case 36: 
            { return symbol(LexicalUnit.WHILE);
            }
          case 74: break;
          case 37: 
            { return symbol(LexicalUnit.BEGIN);
            }
          case 75: break;
          case 38: 
            { return symbol(LexicalUnit.PRINT);
            }
          case 76: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }

  /**
   * Runs the scanner on input files.
   *
   * This is a standalone scanner, it will print any unmatched
   * text to System.out unchanged.
   *
   * @param argv   the command line, contains the filenames to run
   *               the scanner on.
   */
  public static void main(String argv[]) {
    if (argv.length == 0) {
      System.out.println("Usage : java GeneratedScanner [ --encoding <name> ] <inputfile(s)>");
    }
    else {
      int firstFilePos = 0;
      String encodingName = "UTF-8";
      if (argv[0].equals("--encoding")) {
        firstFilePos = 2;
        encodingName = argv[1];
        try {
          java.nio.charset.Charset.forName(encodingName); // Side-effect: is encodingName valid? 
        } catch (Exception e) {
          System.out.println("Invalid encoding '" + encodingName + "'");
          return;
        }
      }
      for (int i = firstFilePos; i < argv.length; i++) {
        GeneratedScanner scanner = null;
        try {
          java.io.FileInputStream stream = new java.io.FileInputStream(argv[i]);
          java.io.Reader reader = new java.io.InputStreamReader(stream, encodingName);
          scanner = new GeneratedScanner(reader);
          while ( !scanner.zzAtEOF ) scanner.yylex();
        }
        catch (java.io.FileNotFoundException e) {
          System.out.println("File not found : \""+argv[i]+"\"");
        }
        catch (java.io.IOException e) {
          System.out.println("IO error scanning file \""+argv[i]+"\"");
          System.out.println(e);
        }
        catch (Exception e) {
          System.out.println("Unexpected exception:");
          e.printStackTrace();
        }
      }
    }
  }


}