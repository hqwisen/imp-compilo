# imp-compilo

Tested with Java 8 (JDK) and JFlex 1.6.1.

## How to **java**
```bash
java -jar dist/imp-compilo.jar <input>
```

## How to **gradle**

Run scanner
```bash
gradle scan -Pinput=<input>
```
Run scanner with j
Generate **more/src/Main.java** from LexicalAnalyzer.flex
```bash
gradle generateMain
```
Generate jar **dist/imp-compilo.jar**
```bash
gradle generateJar
```

Generate JavaDoc in **doc/javadoc**
```bash
gradle generateDoc
```

## Resources

http://jflex.de/manual.html
