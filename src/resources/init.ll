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


