all: compil
	java ImpCompilo input.txt

compil:
	jflex imp-compilo.flex
	javac ImpCompilo.java
