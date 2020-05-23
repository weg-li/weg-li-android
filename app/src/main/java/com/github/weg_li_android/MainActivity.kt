package com.github.weg_li_android

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.github.weg_li_android.data.api.ApiHelper
import com.github.weg_li_android.data.api.ApiServiceImpl
import com.github.weg_li_android.data.model.Report
import com.github.weg_li_android.ui.base.ViewModelFactory
import com.github.weg_li_android.ui.main.viewmodel.MainViewModel
import com.github.weg_li_android.utils.AutoFitGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.io.OutputStream
import java.util.*

class MainActivity : AppCompatActivity(), PhotoRecyclerViewAdapter.ItemClickListener {

    private lateinit var photoAdapter: PhotoRecyclerViewAdapter
    private lateinit var mainViewModel: MainViewModel
    private val pickImage = 1
    private val takeImage = 2
    private var mActionMode: ActionMode? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(MainViewModel::class.java)

        setupPhotoCard(this)

        setupCarTypeSpinner()
        setupViolationSpinner()
        durationText.setOnClickListener {
        }

        sendButton.setOnClickListener { mainViewModel.sendReport() }

        val licenseObserver = Observer<Report> { newName ->
            if(newName.license != "") {
                status_image.setImageResource(R.drawable.ic_baseline_check_circle_24)
            }
            else {
                status_image.setImageResource(R.drawable.ic_baseline_error_24)
            }
        }

        mainViewModel.propertyAwareReport.observe(this, licenseObserver)

        car_license_plate_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                mainViewModel.changeLicense(s.toString())
            }
        })
    }

    private fun setupPhotoCard(context : Context) {
        setupPhotoRecyclerView(context)

        val pm: PackageManager = context.packageManager
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            take_picture_button.visibility = View.GONE
        }

        val photosCountObserver = Observer<Report> { newName ->
            if(newName.violationPhotos.size >= 2) {
                photos_status_image.setImageResource(R.drawable.ic_baseline_check_circle_24)
            }
            else {
                photos_status_image.setImageResource(R.drawable.ic_baseline_error_24)
            }
        }

        mainViewModel.propertyAwareReport.observe(this, photosCountObserver)
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    private fun Float.convertDpToPixel(context: Context): Float {
        return this * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }


    private fun setupPhotoRecyclerView(context: Context) {
        val recyclerView = findViewById<RecyclerView>(R.id.photos_grid)

        val layoutManager  = AutoFitGridLayoutManager(this, 120f.convertDpToPixel(context).toInt())
        recyclerView.layoutManager = layoutManager

        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                //layoutManager.setColumnWidth(convertPixelsToDp(recyclerView.width.toFloat(), context).toInt())
                Timber.e(convertPixelsToDp(recyclerView.width.toFloat(), context).toString()) //height is ready
            }
        })



        photoAdapter = PhotoRecyclerViewAdapter(this, mainViewModel.getViolationPhotos())
        photoAdapter.setClickListener(this)
        recyclerView.adapter = photoAdapter

        take_picture_button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN ->
                    dispatchTakePictureIntent()
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                    v.performClick()
            }
            false
        }

        add_picture_button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN ->
                    dispatchPickPictureIntent()
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                    v.performClick()
            }
            false
        }

        /**val callback = object : ActionMode.Callback {

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menuInflater.inflate(R.menu.photos_contextual_action_bar, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.delete -> {
                        // Handle delete icon press
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
            }
        }

        val actionMode = startSupportActionMode(callback)
        actionMode?.title = "1 selected"**/

    }

    private fun dispatchPickPictureIntent() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(pickPhoto, pickImage)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, takeImage)
            }
        }
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
        }
    }

    override fun onItemClick(view: View?, position: Int) {
        if(mActionMode != null) {
            Timber.e("You clicked number %s", position.toString())
            if(photoAdapter.deletePhotoSet.contains(position)) {
                photoAdapter.deletePhotoSet.remove(position)
                photoAdapter.notifyItemChanged(position)
            }
            else {
                photoAdapter.deletePhotoSet.add(position)
                photoAdapter.notifyItemChanged(position)
            }
        }
    }

    override fun onItemLongClick(view: View?, position: Int) {
        Timber.e("You looong clicked number %s", position.toString())
        if(mActionMode != null) {
            return
        }
        mActionMode =  startSupportActionMode(mActionModeCallback)
        if(photoAdapter.deletePhotoSet.contains(position)) {
            photoAdapter.deletePhotoSet.remove(position)
            photoAdapter.notifyItemChanged(position)
        }
        else
            photoAdapter.deletePhotoSet.add(position)
            photoAdapter.notifyItemChanged(position)
    }

    private val mActionModeCallback: ActionMode.Callback =
        object : ActionMode.Callback {
            override fun onCreateActionMode(
                mode: ActionMode,
                menu: Menu?
            ): Boolean {
                menuInflater.inflate(R.menu.photos_contextual_action_bar, menu)
                return true
            }

            override fun onPrepareActionMode(
                mode: ActionMode?,
                menu: Menu?
            ): Boolean {
                return false
            }

            override fun onActionItemClicked(
                mode: ActionMode,
                item: MenuItem
            ): Boolean {
                return when (item.itemId) {
                    R.id.delete_button -> {
                        Timber.e("I'm here. Option1 selected, i will remove "+photoAdapter.deletePhotoSet.sorted().toString())
                        photoAdapter.deletePhotoSet.sortedDescending().forEach {
                            photoAdapter.removeAt(it)
                        }
                        photoAdapter.notifyDataSetChanged()

                        mode.finish()
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode) {
                photoAdapter.deletePhotoSet.clear()
                photoAdapter.notifyDataSetChanged()
                mActionMode = null
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                pickImage -> {
                    if (data.clipData != null) {
                        val count = data.clipData!!.itemCount
                        for (i in 0 until count) {
                            val imageUri: Uri? = data.clipData!!.getItemAt(i).uri
                            val insertIndex = imageUri?.let { mainViewModel.addViolationPhoto(it) }
                            if (insertIndex != null) {
                                photoAdapter.notifyItemInserted(insertIndex)
                            }
                        }
                    } else if (data.data != null) {
                        val mImageUri: Uri = data.data!! // TODO: Add something useful here.
                    }

                }
                takeImage -> {
                    val bitmap = data.extras?.get("data") as Bitmap
                    if (isStoragePermissionGranted()) {
                        val imageUri : Uri = saveImageBitmap(bitmap, System.currentTimeMillis().toString())
                        Timber.e(imageUri.toString())
                        val insertIndex = mainViewModel.addViolationPhoto(imageUri)
                        photoAdapter.notifyItemInserted(insertIndex)
                    } // TODO: What do if not granted
                    else {
                        Timber.e("Not granted")
                    }
                }
            }
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        val tag = "Storage Permission"
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Timber.v(tag, "Permission is granted")
                true
            } else {
                Timber.v(tag, "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Timber.v(tag, "Permission is granted")
            true
        }
    }

    private fun saveImageBitmap(image_bitmap: Bitmap, image_name: String) : Uri {
        val fos: OutputStream
        val resolver: ContentResolver = contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, image_name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //this one
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + '/' + applicationInfo.loadLabel(packageManager).toString())
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }
        val imageUri: Uri =
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
        fos = resolver.openOutputStream(Objects.requireNonNull(imageUri))!!
        image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        Objects.requireNonNull(fos).close()
        return imageUri
    }

}