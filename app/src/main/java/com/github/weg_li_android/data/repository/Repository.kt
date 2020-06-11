package com.github.weg_li_android.data.repository

import com.github.weg_li_android.data.api.ServiceBuilder
import com.github.weg_li_android.data.api.WegliEndpoints

class Repository() {
    var service = ServiceBuilder.buildService(WegliEndpoints::class.java)

    suspend fun getDistricts() = service.getDistricts()
}