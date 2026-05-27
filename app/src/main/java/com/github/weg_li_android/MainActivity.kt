package com.github.weg_li_android

import android.app.TimePickerDialog
import android.content.Intent
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
import com.github.weg_li_android.databinding.ActivityMainBinding
import com.github.weg_li_android.ui.base.ViewModelFactory
import com.github.weg_li_android.ui.main.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()

        setupCarTypeSpinner()

        binding.carColorText.addTextChangedListener {
            mainViewModel.colorSelected(it.toString())
        }

        binding.carLicenseText.addTextChangedListener {
            mainViewModel.licenseSelected(it.toString())
        }

        setupViolationSpinner()

        binding.timeText.setOnClickListener { openTimePickerDialog() }

        binding.durationText.addTextChangedListener {
            mainViewModel.durationSelected(it.toString())
        }

        binding.obstructionSwitch.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.obstructionSelected(
                isChecked
            )
        }

        binding.carWasEmptySwitch.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.carWasEmptySelected(
                isChecked
            )
        }

        binding.sendButton.setOnClickListener {
            startEmailIntent()
        }
    }

    private fun startEmailIntent() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "*/*"
            //TODO change to correct address
            putExtra(Intent.EXTRA_EMAIL, "name@email.de")
            putExtra(Intent.EXTRA_SUBJECT, mainViewModel.getReport().getEmail())
            //TODO Add photos
            //putExtra(Intent.EXTRA_STREAM, attachment)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun openTimePickerDialog() {
        val calendar = Calendar.getInstance()

        val timePickerDialog =
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN)
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val formattedTime = simpleDateFormat.format(calendar.time)
                binding.timeText.setText(formattedTime)
                mainViewModel.timeSelected(formattedTime)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        timePickerDialog.show()
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
            binding.carTypeSpinner.adapter = adapter
        }
        binding.carTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
            binding.violationSpinner.adapter = adapter
            binding.violationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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