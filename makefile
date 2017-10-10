all: compil
	java ImpCompilo source.imp 

compil:
	jflex imp-compilo.flex
	javac ImpCompilo.java
