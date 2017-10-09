#!/bin/bash

# Must be run in project root

FLEX=imp-compilo.flex
CLASS=ImpCompilo
JFLEXJAR=lib/jflex.jar
INPUT=input.txt
java -jar $JFLEXJAR  $FLEX
javac $CLASS.java
java $CLASS $INPUT
