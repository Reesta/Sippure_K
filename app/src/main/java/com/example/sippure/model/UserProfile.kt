package com.example.sippure.model

data class UserProfile(val fullName: String = "",
                       val email: String = "",
                       val preferences: List<String> = emptyList(),
                       val photoUrl: String? = null)

