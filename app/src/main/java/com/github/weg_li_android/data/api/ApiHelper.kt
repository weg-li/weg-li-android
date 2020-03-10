package com.github.weg_li_android.data.api

import com.github.weg_li_android.data.model.Report

class ApiHelper(private val apiService: ApiService) {

    fun sendReport(report: Report) = apiService.sendReport(report)
}