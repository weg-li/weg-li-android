package com.github.weg_li_android.data.repository

import com.github.weg_li_android.data.api.ServiceBuilder
import com.github.weg_li_android.data.api.WegliEndpoints
import com.github.weg_li_android.data.model.District
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository() {

    fun getDistricts(): List<District> {
        val request = ServiceBuilder.buildService(WegliEndpoints::class.java)
        val call = request.getDistricts()
        var districts = listOf<District>()

        call.enqueue(object : Callback<List<District>> {
            override fun onResponse(
                call: Call<List<District>>,
                response: Response<List<District>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        districts = it
                    }
                } else {
                    TODO("Implement Failstate")
                }
            }

            override fun onFailure(call: Call<List<District>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        return districts
    }
}