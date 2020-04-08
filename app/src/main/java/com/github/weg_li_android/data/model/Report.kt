package com.github.weg_li_android.data.model

data class Report(
    val id: Int = 0,
    val address: String = "",
    val type: String = "",
    val color: String = "",
    val license: String = "",
    val violation: String = "",
    val duration: String = "",
    val obstructionOthers: Boolean = false,
    val fullName: String = "",
    val userAddress: String = "",
    val userPostalCode: String = "",
    val phoneNumber: String = ""
)