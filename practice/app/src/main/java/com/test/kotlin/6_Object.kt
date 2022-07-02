package com.test.kotlin

// object 선언을 하면 static 클래스가 됨, 싱글톤 자동 지원
object CarFactory {
    val cars = mutableListOf<Car>() // ArrayList 를 반환 ArrayListOf랑 동작이 같음
    fun makeCar(holesPower: Int): Car {
        val car = Car(holesPower)
        cars.add(car)
        return car
    }
}

data class Car(val holesPower : Int)

fun main() {
    val carA = CarFactory.makeCar(10)
    val carB = CarFactory.makeCar(20)

    println(CarFactory.cars.size)
    println(carA)
    println(carB)
    println(carA.holesPower.toString() + " " + carB.holesPower)
}