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
	%a = alloca i32
	store i32 %1, i32* %a
	; T(<For>)loopCount=1
	%2 = load i32, i32* %a
	%3 = add i32 0, %2
	%4 = add i32 0, 2
	%5 = add i32 %3, %4
	%b = alloca i32
	store i32 %5, i32* %b
	%6 = add i32 0, 10
	%7 = load i32, i32* %b
	%8 = icmp  slt i32 %7, %6
	br i1 %8, label %beginLoop1, label %endLoop1
	beginLoop1:
	call void @println(i32* %b)
	; T(<Assign>)
	%9 = load i32, i32* %a
	%10 = add i32 0, %9
	%11 = add i32 0, 1
	%12 = add i32 %10, %11
	store i32 %12, i32* %a
	
	%13 = load i32, i32* %a
	%14 = add i32 0, %13
	%15 = add i32 0, 2
	%16 = add i32 %14, %15
	%17 = load i32, i32* %b
	%18 = add i32 %17, %16
	store i32 %18, i32* %b
	%19 = add i32 0, 10
	%20 = load i32, i32* %b
	%21 = icmp  slt i32 %20, %19
	br i1 %21, label %beginLoop1, label %endLoop1
	endLoop1:
	
	ret void
}
