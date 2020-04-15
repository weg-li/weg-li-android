package com.github.weg_li_android.ui.main.viewmodel

import com.github.weg_li_android.data.repository.Repository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MainViewModelTest {

    lateinit var mainViewModel: MainViewModel
    private val mockRepository: Repository = mock()

    @Before
    fun setup() {
        mainViewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `sendReport calls repository`() {
        mainViewModel.sendReport()
        verify(mockRepository).sendEmail(any())
    }

    @Test
    fun `typeSelected updates report`() {
        val type = "BMW"
        mainViewModel.typeSelected(type)
        assertEquals(type, mainViewModel.getReport().type)
    }

    @Test
    fun `violationSelected updates report`() {
        val violation = "Auf dem Radweg geparkt"
        mainViewModel.typeSelected(violation)
        assertEquals(violation, mainViewModel.getReport().type)
    }
}