package com.github.weg_li_android.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.github.weg_li_android.data.model.Report
import com.github.weg_li_android.data.repository.Repository
import timber.log.Timber

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val report = Report()

    fun sendReport() {
        Timber.d("Report is:%s", report.toString())
        repository.sendEmail(report.getEmail())
    }

    fun getReport() = report

    fun typeSelected(type: String) {
        report.type = type
    }

    fun violationSelected(violation: String) {
        report.violation = violation
    }
}