package com.github.weg_li_android.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.github.weg_li_android.data.model.Report
import com.github.weg_li_android.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val report = Report()

    val districts = liveData(Dispatchers.IO) {
        val retrievedDistricts = repository.getDistricts()

        emit(retrievedDistricts)
    }

    fun sendReport() {
        Timber.d("Report is:%s", report.toString())
        Timber.d("Full Email:%s", report.getEmail())
    }

    fun getReport() = report

    fun typeSelected(type: String) {
        report.type = type
    }

    fun violationSelected(violation: String) {
        report.violation = violation
    }

    fun colorSelected(color: String) {
        report.color = color
    }

    fun licenseSelected(license: String) {
        report.license = license
    }

    fun durationSelected(duration: String) {
        report.duration = duration
    }

    fun timeSelected(time: String) {
        report.time = time
    }

    fun obstructionSelected(obstruction: Boolean) {
        report.obstructionOthers = obstruction
    }

    fun carWasEmptySelected(carWasEmpty: Boolean) {
        report.carWasEmpty = carWasEmpty
    }
}