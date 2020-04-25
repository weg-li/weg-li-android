package com.github.weg_li_android.ui.main.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.github.weg_li_android.data.model.Report
import com.github.weg_li_android.data.repository.Repository
import com.github.weg_li_android.utils.PropertyAwareMutableLiveData
import timber.log.Timber

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val report = Report()
    val propertyAwareReport = PropertyAwareMutableLiveData<Report>().apply {
        value = report
    }

    fun sendReport() {
        Timber.d("Report is:%s", report.toString())
        repository.sendEmail(report.getEmail())
    }

    fun getViolationPhotos() : MutableList<Bitmap> {
        return report.violationPhotos
    }

    fun typeSelected(type: String) {
        report.type =  type
    }

    fun changeLicense(label: String) {
        report.license  = label
    }
}