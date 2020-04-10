package com.github.weg_li_android.ui.main.viewmodel

import com.github.weg_li_android.data.model.Report
import com.github.weg_li_android.data.repository.Repository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    lateinit var mainViewModel: MainViewModel
    private val mockRepository: Repository = mock()

    @Before
    fun setup() {
        mainViewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `sendEmail calls repository`() {
        mainViewModel.sendReport(Report())
        verify(mockRepository).sendEmail(any())
    }
}