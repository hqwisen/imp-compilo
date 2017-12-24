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
	%1 = add i32 0, 1
	%2 = add i32 0, 5
	%3 = add i32 %1, %2
	%a = alloca i32
	store i32 %3, i32* %a
	; T(<Assign>)
	%4 = add i32 0, 7
	%b = alloca i32
	store i32 %4, i32* %b
	; T(<Assign>)
	%5 = add i32 0, 1
	%c = alloca i32
	store i32 %5, i32* %c
	; T(<If>)ifCount=1
	%6 = load i32, i32* %a
	%7 = icmp  eq i32 %6, 6
	%8 = load i32, i32* %b
	%9 = icmp  eq i32 %8, 8
	%10 = add i1 %7, %9
	%11 = icmp  eq i1 %10, 2
	%12 = load i32, i32* %c
	%13 = icmp  eq i32 %12, 2
	%14 = add i1 %11, %13
	%15 = icmp  uge i1 %14, 1
	br i1 %15, label %iftrue1, label %iffalse1
	iftrue1:
	; T(<Assign>)
	%16 = add i32 0, 7
	store i32 %16, i32* %b
	call void @println(i32* %b)
	ret void
	iffalse1:
	; T(<Assign>)
	%17 = add i32 0, 187
	store i32 %17, i32* %c
	call void @println(i32* %c)
	ret void
	
	ret void
}
