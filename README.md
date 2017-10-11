# imp-compilo

Tested with Java 8 (JDK)

## How To

Run scanner
```bash
gradle scan -Pinput=<input>
```
Generate **more/src/Main.java** from LexicalAnalyzer.flex
```bash
gradle generateMain
```
Generate jar **dist/imp-compilo.jar**
```bash
gradle generateJar
```

## Dependencies

* gradle

## Resources

http://jflex.de/manual.html
