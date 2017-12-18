@.printstr = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1
@.readstr = private unnamed_addr constant [3 x i8] c"%d\00", align 1
@y = global i32 8


define i32 @readInt() {
  %x = alloca i32, align 4
  %1 = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr
       inbounds ([3 x i8], [3 x i8]* @.readstr, i32 0, i32 0),
        i32* %x)

  %2 = load i32, i32* %x, align 4
  ret i32 %2
}

define void @println(i32 %x) {
  %1 = alloca i32, align 4
  store i32 %x, i32* %1, align 4
  %2 = load i32, i32* %1, align 4
  %3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds
       ([4 x i8], [4 x i8]* @.printstr, i32 0, i32 0), i32 %2)

  ret void
}

declare i32 @__isoc99_scanf(i8*, ...)
declare i32 @printf(i8*, ...)

define i32 @assign(i32 %number){
    ret i32 %number
}

define void @print(i32 %varname){
    call void @println(i32 %varname)
    ret void
}

define i32 @main(){
    %x = call i32 @assign(i32 2)
    call void @print(i32 %x)
    ret i32 0
}