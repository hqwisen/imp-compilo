all: compil
	java ImpCompilo Euclid.imp 

compil:
	jflex imp-compilo.flex
	javac ImpCompilo.java
