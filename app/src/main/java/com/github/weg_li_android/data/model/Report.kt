package com.github.weg_li_android.data.model

data class Report(
    val id: Int = 0,
    var address: String = "",
    var type: String = "",
    var color: String = "",
    var license: String = "",
    var violation: String = "",
    var duration: String = "",
    var obstructionOthers: Boolean = false,
    var fullName: String = "",
    var userAddress: String = "",
    var userPostalCode: String = "",
    var phoneNumber: String = "",
    var userEmail: String = ""
)