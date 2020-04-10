package com.github.weg_li_android.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.github.weg_li_android.data.model.Report
import com.github.weg_li_android.data.repository.Repository
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun sendReport(report: Report) {
        Timber.d("Report is:%s", report.toString())
        repository.sendEmail(report.address + ", " + report.type + ", " + report.color)
    }

    private fun createEmail(report: Report): String {


        return ""
    }

    override fun onCleared() {
        super.onCleared()
    }
}