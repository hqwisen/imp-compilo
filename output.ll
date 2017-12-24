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
	%1 = add i32 0, 2
	%a = alloca i32
	store i32 %1, i32* %a
	; T(<While>)loopCount=1
	%2 = load i32, i32* %a
	%3 = icmp  slt i32 %2, 50
	br i1 %3, label %beginLoop1, label %endLoop1
	beginLoop1:
	call void @println(i32* %a)
	; T(<Assign>)
	%4 = load i32, i32* %a
	%5 = add i32 0, %4
	%6 = add i32 0, 1
	%7 = add i32 %5, %6
	store i32 %7, i32* %a
	
	%8 = load i32, i32* %a
	%9 = icmp  slt i32 %8, 50
	br i1 %9, label %beginLoop1, label %endLoop1
	endLoop1:
	; T(<Assign>)
	%10 = add i32 0, 20
	%c = alloca i32
	store i32 %10, i32* %c
	; T(<Assign>)
	%11 = load i32, i32* %a
	%12 = add i32 0, %11
	%13 = load i32, i32* %c
	%14 = add i32 0, %13
	%15 = add i32 %12, %14
	store i32 %15, i32* %a
	call void @println(i32* %a)
	
	ret void
}
