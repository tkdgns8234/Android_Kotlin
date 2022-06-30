package com.test.kotlin
/*
코틀린은 객체지향 + 함수형 프로그래밍 패러다임을 모두 지향하는 다중 패러다임 언어이다
함수형 프로그래밍
객체지향언어의 상태변화에따른 대응의 어려움을 극복하기위해 나옴

최근 프로그래밍 패러다임 아래와 같이 구분됨
명령형 프로그래밍: 무엇을 할지보다 어떻게 할지를 정의
- 절차지향 프로그래밍: 순차적으로 처리하는 방식
- 객체지향 프로그래밍: 객체간의 유기적인 상호작용으로 프로그램을 표현
선언형 프로그래밍: 어떻게 할지보다 무엇을 할지를 설명하는 방식
- 함수형 프로그래밍: 순수함수를 조합하고 소프트웨어를 만드는 방식

함수형 프로그래밍
-> 순수함수를 사용해야한다
-> 람다식을 사용
-> 고차함수를 사용할 수 있다. (return이나 파라미터로 일급 객체(일급 함수)를 주고받을 수 있는 함수)

순수함수 특징
1. 같은 파라미터에대하여 항상 같은 값을 return 한다. ='부작용이 없는 함수'
2. 함수 외부의 어떤 상태도 바꾸지 않는다.

일급객체 특징
1. 일급 객체는 함수의 인자로 전달할 수 있다.
2. 함수의 반환값에 사용할 수 있다.
3. 변수에 담을 수 있다.
 */


// 1. Lambda
// 람다식: 변수처럼 다룰 수 있는 익명 함수를 뜻한다
// 람다 또한 일급객체이다.
// -> value 로 전달 가능, return 가능, 변수에 담을 수있음

// 람다의 기본 정의
// val lambdaName : Type = {argumentList -> codeBody}

val square : (Int) -> (Int) = {number -> number*number}
val square2 = {number: Int -> number*number}

val nameAge = {age : Int, name : String ->
    "이름은 $name 이고 나이는$age 입니다."
}


// 람다의 Return
// 람다는 마지막 value 값을 return 한다
val calculate : (Int) -> String = {
    when(it) { // when 도 결국 값을 return 함, it = 파라미터 하나일때 사용가능
        in 0..40 -> "bad"
        in 41..70 -> "good"
        in 70..100 -> "perfect"
        else -> "error"
    }
}


// 람다를 표현하는 여러가지 방법
fun invokeLambda(lambda : (Double) -> Boolean) : Boolean {
    return lambda(2311.5)
}


fun main() {
    val rs1 = square(14)
    val rs2 = square2(14)
    val rs3 = nameAge(28, "정상훈")
    println(rs1.toString() + rs2 + rs3)

    val str : String = "my"
    println(str.pizzaIsGreat())
    println(extendString("정상훈", 25))

    println(calculate(70))

//    val doubleToBoolean : (Double) -> Boolean = {
//        when(it) {
//            in 0.0..5.0 -> false
//            else -> true
//        }
//    }

    val doubleToBoolean = {num : Double ->
        num == 5.0
    }

    println(invokeLambda(doubleToBoolean))
    // 람다 리터럴 (선언없이 바로 대입)
    println(invokeLambda({ true }))
    println(invokeLambda({ it > 3.22 }))
    println(invokeLambda({ number: Double -> number > 3.22 }))
    // 괄호 생략
    println(invokeLambda { it > 3.22 })
}


/*
익명 내부함수
사용 조건
1. 적용 인터페이스가 Kotlin interface 가 아닌 java 인터페이스여야 한다.
2. 그 인터페이스는 딱 하나의 메소드만 가져야 한다.
ex)
button.setOnClickListener(object : View.OnClickListener {
    override fun onClick() {
        //to do..
    }
})
를 아래로 변환 가능

button.setOnClickListener {
    //to do..
}
*/



// 2. 확장 함수
// 기존 클래스에 메서드 추가, 스태틱 메소드로 추가됨
val pizzaIsGreat : String.() -> String = {
    "$this pizza is great !!"
}

fun extendString(name: String, age: Int) : String{

    val introduceMySelf : String.(Int) -> String = {"I`m $this and $it years old"}
    return name.introduceMySelf(age)
}


