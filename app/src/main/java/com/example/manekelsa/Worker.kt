package com.example.manekelsa

data class Worker(

    val name: String = "",
    val skill: String = "",
    val phoneNumber: String = "",
    val dailyRate: Int = 0,
    val availableToday: Boolean = false,
    val experience: Int = 0,
    val city: String = "",
    val rating: Double = 0.0,
    val profileImageUrl: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)