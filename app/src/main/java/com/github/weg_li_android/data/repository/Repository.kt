package com.github.weg_li_android.data.repository

import com.github.weg_li_android.data.api.ApiHelper
import com.github.weg_li_android.data.model.Report

class Repository(private val apiHelper: ApiHelper) {

    fun sendReport(report: Report) {
        apiHelper.sendReport(report)
    }
}