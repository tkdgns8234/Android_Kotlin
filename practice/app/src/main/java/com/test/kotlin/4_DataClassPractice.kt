package com.test.kotlin

// 데이터 class
data class Ticket(val company : String, val name : String, var date : String, var seatNumber: Int)
class TicketNormal(val company : String, val name : String, var date : String, var seatNumber: Int)
fun main() {
    val ticketA = Ticket("koreanAir", "정상훈", "2025-01-32", 32)
    val ticketB = TicketNormal("koreanAir", "정상훈", "2025-01-32", 32)

    println(ticketA)
    println(ticketB)
}