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
	; T(<Assign>)
	%1 = add i32 0, 0
	%2 = add i32 0, 2
	%3 = add i32 0, 1
	%4 = sub i32 %2, %3
	%5 = sub i32 %1, %4
	%x = alloca i32
	store i32 %5, i32* %x
	; T(<For>)loopCount=1
	%6 = add i32 0, 0
	%i = alloca i32
	store i32 %6, i32* %i
	%7 = add i32 0, 10
	%8 = load i32, i32* %i
	%9 = icmp  slt i32 %8, %7
	br i1 %9, label %beginLoop1, label %endLoop1
	beginLoop1:
	; T(<Assign>)
	%10 = load i32, i32* %x
	%11 = add i32 0, %10
	%12 = add i32 0, 2
	%13 = add i32 %11, %12
	store i32 %13, i32* %x
	
	%14 = add i32 0, 1
	%15 = load i32, i32* %i
	%16 = add i32 %15, %14
	store i32 %16, i32* %i
	%17 = add i32 0, 10
	%18 = load i32, i32* %i
	%19 = icmp  slt i32 %18, %17
	br i1 %19, label %beginLoop1, label %endLoop1
	endLoop1:
	call void @println(i32* %x)
	%20 = call i32 @readInt()
	%y = alloca i32
	store i32 %20, i32* %y
	; T(<Assign>)
	%21 = load i32, i32* %x
	%22 = add i32 0, %21
	%23 = load i32, i32* %y
	%24 = add i32 0, %23
	%25 = add i32 %22, %24
	%z = alloca i32
	store i32 %25, i32* %z
	call void @println(i32* %z)
	
	ret void
}
