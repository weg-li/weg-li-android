package com.github.weg_li_android.data.api

import com.github.weg_li_android.data.model.Report

interface ApiService {

    fun sendReport(report: Report)
}