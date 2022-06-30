package com.test.kotlin

// companion object
// 자바의 static 대용
// 정적 필드, 메서드 선언시 사용

class Book private constructor(val id : Int, val name : String) {
    val book = "전역 book"

    companion object BookFactory : IdProvider{  // 이름 정의, 인터페이스도 구현할 수 있음
        const val MY_BOOK_NAME = "static book" // const는 컴파일타임에 결정 val도 불변이지만 런타임에 결정
        fun create() = Book(getId(), MY_BOOK_NAME)

        override fun getId(): Int {
            return 444
        }
    }
}

interface IdProvider {
    fun getId() : Int
}

fun main() {
    val book = Book.create()
    println(book.name)
    println(book.book)

    println(Book.MY_BOOK_NAME)
}