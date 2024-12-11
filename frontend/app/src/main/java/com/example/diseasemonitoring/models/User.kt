package com.example.diseasemonitoring.models

data class User(
    val name: String,
    val lastName: String,
    val email: String,
    val password: String,
    val age: Int
)

data class RegisterRequest(
    val name: String,
    val lastName: String,
    val email: String,
    val password: String,
    val age: Int
)

data class LoginRequest(val email: String, val password: String)
data class ApiResponse(val success: Boolean, val message: String)
