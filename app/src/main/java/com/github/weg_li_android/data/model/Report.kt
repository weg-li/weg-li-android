package com.github.weg_li_android.data.model

import com.google.gson.annotations.SerializedName

data class Report(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("email")
    val email: String = ""
)