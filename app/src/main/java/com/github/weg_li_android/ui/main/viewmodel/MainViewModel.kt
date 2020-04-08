package com.github.weg_li_android.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.github.weg_li_android.data.model.Report
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MainViewModel() : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun sendReport(report: Report) {
        Timber.d("Report is:%s", report.toString())
    }

    override fun onCleared() {
        super.onCleared()
    }
}