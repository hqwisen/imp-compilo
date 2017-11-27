# imp-compilo

Compiler for IMP, a simple imperative language. Written in Java.

Project for the 'Introduction to language theory and compiling' course.

## How to **java**

Tested with Java 9 (JDK) and JFlex 1.6.1.

```bash
java -jar dist/scanner.jar <input>
java -jar dist/parser.jar <input>
```

## How to **gradle**

Run the scanner or parser

```bash
gradle scanner -Pinput=<input>
gradle parser  -Pinput=<input>
```

Generate **more/src/GeneratedScanner.java** from LexicalAnalyzer.flex

```bash
gradle generateScanner
```

Generate jar **dist/scanner.jar** or **dist/parser.jar**

```bash
gradle generateScannerJar
gradle generateParserJar
```

Generate JavaDoc in **doc/javadoc**
```bash
gradle generateDoc
```

## Resources

http://jflex.de/manual.html
