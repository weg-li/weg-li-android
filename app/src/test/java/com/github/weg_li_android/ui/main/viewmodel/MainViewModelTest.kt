package com.github.weg_li_android.ui.main.viewmodel

import com.github.weg_li_android.data.model.Report
import com.github.weg_li_android.data.repository.Repository
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
    fun `sendReport calls repository`() {
        mainViewModel.sendReport(Report())
        verify(mockRepository).sendEmail("")
    }

    @Test
    fun `sendReport transforms Report into String`() {
        val report = Report(
            0,
            "Straße",
            "BMW",
            "blue",
            "B-MW-23",
            "Parked on bike lane",
            "3",
            true,
            "Mr. Smith",
            "User Straße",
            "12045",
            "1234567890"
        )

        mainViewModel.sendReport(report)
        verify(mockRepository).sendEmail("Straße, BMW, blue")
    }
}