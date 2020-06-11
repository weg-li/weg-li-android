package com.github.weg_li_android.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.weg_li_android.data.repository.Repository
import com.github.weg_li_android.ui.main.viewmodel.MainViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Repository()) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}