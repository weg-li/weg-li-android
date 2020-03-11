package com.github.weg_li_android.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.github.weg_li_android.data.model.Report
import com.github.weg_li_android.data.repository.Repository
import io.reactivex.disposables.CompositeDisposable

class MainViewModel(private val repo: Repository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun sendReport(report: Report) {
        //TODO send report
    }

    override fun onCleared() {
        super.onCleared()
    }
}