package com.github.weg_li_android

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import com.github.weg_li_android.data.api.ApiHelper
import com.github.weg_li_android.data.api.ApiServiceImpl
import com.github.weg_li_android.ui.base.ViewModelFactory
import com.github.weg_li_android.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()

        setupCarTypeSpinner()
        carColorText.addTextChangedListener {
            mainViewModel.colorSelected(it.toString())
        }
        carLicenseText.addTextChangedListener {
            mainViewModel.licenseSelected(it.toString())
        }
        setupViolationSpinner()
        durationText.addTextChangedListener {
            mainViewModel.durationSelected(it.toString())
        }
        obstructionSwitch.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.obstructionSelected(
                isChecked
            )
        }
        sendButton.setOnClickListener { mainViewModel.sendReport() }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(MainViewModel::class.java)
    }

    private fun setupCarTypeSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.car_type_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            carTypeSpinner.adapter = adapter
        }
        carTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mainViewModel.typeSelected((view as AppCompatTextView).text.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupViolationSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.violation_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            violationSpinner.adapter = adapter
            violationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    mainViewModel.violationSelected((view as AppCompatTextView).text.toString())
                }
            }
        }
    }
}