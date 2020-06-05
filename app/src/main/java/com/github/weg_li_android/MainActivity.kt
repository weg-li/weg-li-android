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
import com.github.weg_li_android.data.repository.Repository
import com.github.weg_li_android.ui.base.ViewModelFactory
import com.github.weg_li_android.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

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

        val repository = Repository()
        val districts = repository.getDistricts()

        carLicenseText.addTextChangedListener {
            mainViewModel.licenseSelected(it.toString())
        }

        setupViolationSpinner()

        timeText.setOnClickListener { openTimePickerDialog() }

        durationText.addTextChangedListener {
            mainViewModel.durationSelected(it.toString())
        }

        obstructionSwitch.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.obstructionSelected(
                isChecked
            )
        }

        carWasEmptySwitch.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.carWasEmptySelected(
                isChecked
            )
        }

        sendButton.setOnClickListener {
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
                timeText.setText(formattedTime)
                mainViewModel.timeSelected(formattedTime)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        timePickerDialog.show()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory()
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
                view?.let {
                    mainViewModel.typeSelected((it as AppCompatTextView).text.toString())
                }
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