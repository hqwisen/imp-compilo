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
	%1 = add i32 0, 7
	%x = alloca i32
	store i32 %1, i32* %x
	call void @println(i32* %x)
	%2 = call i32 @readInt()
	%b = alloca i32
	store i32 %2, i32* %b
	; T(<Assign>)
	%3 = load i32, i32* %b
	%4 = add i32 0, %3
	%5 = add i32 0, 1
	%6 = load i32, i32* %x
	%7 = add i32 0, %6
	%8 = add i32 %5, %7
	%9 = add i32 %4, %8
	%c = alloca i32
	store i32 %9, i32* %c
	call void @println(i32* %b)
	call void @println(i32* %c)
	; T(<Assign>)
	%10 = add i32 0, 0
	%a = alloca i32
	store i32 %10, i32* %a
	; T(<While>)loopCount=1
	%11 = load i32, i32* %a
	%12 = icmp sle i32 %11, 12
	br i1 %12, label %beginLoop1, label %endLoop1
	beginLoop1:
	call void @println(i32* %a)
	; T(<Assign>)
	%13 = load i32, i32* %a
	%14 = add i32 0, %13
	%15 = add i32 0, 2
	%16 = add i32 0, 1
	%17 = mul i32 %15, %16
	%18 = add i32 %14, %17
	store i32 %18, i32* %a
	
	%19 = load i32, i32* %a
	%20 = icmp sle i32 %19, 12
	br i1 %20, label %beginLoop1, label %endLoop1
	endLoop1:
	
	ret void
}
