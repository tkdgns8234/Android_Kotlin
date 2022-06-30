package com.test.kotlin

fun main() {
//    kotlinTest()
//    println(add(2.2f,3))
//
//    val name : String = "정상훈"
//    println("My name is ${name + 1}..") //  {} 생략 가능
//
//    checkNum(90)
//
//    arrayTest()
//    forAndWhile()
    nullCheck()
}

// 함수

fun kotlinTest() {
    println("kotlin_test")
}

fun add(a : Float, b: Int) = (a + b).toInt()


// 조건문

/*
Expression & Statement
값을 반환하면 Expression, 명령문: Statement
코틀린의 모든 함수는 Expression 왜? 기본적으로 Unit 을 반환
*/

fun max(a : Int, b: Int) : Int {
    return if (a>b) a else b
}

fun checkNum(num : Int) {
    when(num) {
        1 -> println("number 1") // Statement 로 쓰인 when
        else -> println(num)
    }

    val score = when(num) { // Expression 으로 쓰인 when
        1 -> -1
        else -> num
    }

    when(num) {
        in 90..100 -> println("A")
        in 80..95 -> println("B") // break 자동 적용
        else -> println("C")
    }
}

// Array and List
/*
 - Array
 - List
 1. mutable /ex arrayList
 2. immutable (수정 불가) /ex list (읽기전용)
*/

fun arrayTest() {
    val array = arrayOf(1, 2, 3, "정상훈")
    val list = listOf(1, 2, 3, 6, "정상훈")
    val arrayList = arrayListOf<Int>(1,2,3,4,5,6)

    //list 는 set 불가

    println(array[3])
    println(list.get(4))
}

// 반복문 for & while

fun forAndWhile() {
    val numbers = arrayListOf(1,2,3,4,5)
    for (num in numbers) {
        println(num)
    }

    for (i in 1..10) {
        println(i)
    }

    for (i in 1..10 step 2) {
        println(i)
    }

    for (i in 10 downTo 1) {
        println(i)
    }

    for (i in 1 until 10) {
        println(i)
    }

    var index = 0
    while(index < 10) {
        println("index 값은 ${index}입니다.")
        index ++
    }

    val students = arrayListOf("정상훈", "김성원", "가나다")
    for ((index, student) in students.withIndex()) {
        println("${index + 1}번째 학생은 ${student}입니다.")
    }
}

// 자바와 다른 가장 큰 특징
// 7. Nullable / NonNull
// 자바의 NPE => null값을 compile time에 확인 불가
// 코틀린 -> 컴파일타임에 null 체크 가능
fun nullCheck() {
    var name : String = "hoon" // NonNull
    var nullName : String? = null // Nullable
    // null 을 넣으려면 ? 를 붙여야한다.

    //null 체크
    var nameInUpperCase = name.uppercase()
    println(nameInUpperCase)

    // 불가
    // var nullNameUpperCase = nullName.uppercase()

    // nullName이 null 이 아니면 함수 실행, null이면 null 반환
    // 동시에 nullNameUpperCase 변수도 Nullable 변수가 된다.
    var nullNameUpperCase = nullName?.uppercase()


    // ?:    엘비스 연산자
    var firstName = "상훈"
    var lastName : String? = null

    var fullName = lastName?:"성이 없습니다." + firstName
    println(fullName)

    // !! 연산자    컴파일러에게 null값이 들어가지 않을거다! 라고 알려주는 기능
    // 왠만하면 사용을 지양해야함
    var nName : String? = null
    var notNullName : String = nName!!

    //let

    val email : String?= "tkdgns8324"
    email?.let {
        println("test $email")
    }
}

// class
