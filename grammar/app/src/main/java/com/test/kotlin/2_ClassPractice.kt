package com.test.kotlin

/*
자바와의 차이점
여러 클래스를 한 파일에 넣을 수 있음
항상 클래스 명이 파일 명과 일치하지 않아도 됨
 */

fun main() {
//    val human = Human()
//    human.eatingCake()
//    println("human`s name is ${human.name}")

//    val sister = Human("정횰", 22)
    val korean = Korean()
    korean.singASong()
}


// constructor 키워드 생략 가능
// 생성자 변수에 Default 값 선언 시, default 생성자로 생성 가능 ex) val human = Human()

//open 키워드 사용하지 않는 경우, final class 로 선언된것으로 간주, 상속 불가
open class Human constructor(val name: String = "Anonymous") { // 프로퍼티와 생성자를 함께 쓰는 방법

    // 부 생성자는 항상 주 생성자를 초기화해야한다
    constructor(name : String, age : Int) : this(name) {
        println("my name is $name age is $age")
    }

    constructor(test : Int) : this() {
        println(test)
    }

    init {
        println("new Human has been born!")
    }

    fun eatingCake() {
        println("This is so YUMMMYYY~~~")
    }

    open fun singASong() {
        println("lalala")
    }
}

//상속
class Korean: Human() {
    override fun singASong() {
        super.singASong()
        println("lalalala")
        println("my name is ${name}")
    }
}
