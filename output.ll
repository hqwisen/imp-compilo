@.printstr = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1
@.readstr = private unnamed_addr constant [3 x i8] c"%d\00", align 1

define i32 @readInt() {
  %x = alloca i32, align 4
  %1 = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.readstr, i32 0, i32 0), i32* %x)
  %2 = load i32, i32* %x, align 4
  ret i32 %2
}

define void @_println(i32 %x) {
  %1 = alloca i32, align 4
  store i32 %x, i32* %1, align 4
  %2 = load i32, i32* %1, align 4
  %3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.printstr, i32 0, i32 0), i32 %2)
  ret void
}

define void @println(i32* %x){
    %1 = load i32, i32* %x
    call void @_println(i32 %1)
    ret void
}

declare i32 @__isoc99_scanf(i8*, ...)
declare i32 @printf(i8*, ...)



; Generated LL below
define void @main(){
	; T(<For>)loopCount=1
	%1 = add i32 0, 0
	%i = alloca i32
	store i32 %1, i32* %i
	%2 = add i32 0, 10
	%3 = load i32, i32* %i
	%4 = icmp  slt i32 %3, %2
	br i1 %4, label %beginLoop1, label %endLoop1
	beginLoop1:
	; T(<Assign>)
	%5 = load i32, i32* %x
	%6 = add i32 0, %5
	%7 = add i32 0, 2
	%8 = add i32 %6, %7
	%x = alloca i32
	store i32 %8, i32* %x
	
	%9 = add i32 0, 1
	%10 = load i32, i32* %i
	%11 = add i32 %10, %9
	store i32 %11, i32* %i
	%12 = add i32 0, 10
	%13 = load i32, i32* %i
	%14 = icmp  slt i32 %13, %12
	br i1 %14, label %beginLoop1, label %endLoop1
	endLoop1:
	
	ret void
}
