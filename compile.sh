
runOutput(){
  echo "COMPILED IN output.ll"
  echo "RUNNING output.bc"
  llvm-as output.ll && lli output.bc
}

gradle compilerJar
java -jar dist/compiler.jar $1
# > output.ll 

if [ $? -eq 0 ]; then
	echo ""
	# runOutput
fi
