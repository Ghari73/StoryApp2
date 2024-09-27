package com.syahdi.storyapp.ui.createStory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.syahdi.storyapp.R
import com.syahdi.storyapp.databinding.ActivityAddBinding
import com.syahdi.storyapp.data.local.preferences.UserPreferences
import com.syahdi.storyapp.ui.main.MainActivity
import com.syahdi.storyapp.utils.createCustomTempFile
import com.syahdi.storyapp.utils.reduceFileImage
import com.syahdi.storyapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddActivity : AppCompatActivity() {
    private var getFile: File? = null
    private lateinit var addViewModel: AddViewModel
    private lateinit var binding: ActivityAddBinding
    private lateinit var progressBar: ProgressBar

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        progressBar = findViewById(R.id.progressBarAdd)
        progressBar.visibility = View.GONE

        setupViewModel()
        binding.cameraButton.setOnClickListener { startTakePhoto() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.addStoryButton.setOnClickListener { uploadImage() }
    }

    private fun setupViewModel() {
        addViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[AddViewModel::class.java]
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddActivity,
                "com.syahdi.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        progressBar.visibility = View.VISIBLE
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val inputDescription = binding.descriptionEditText.text.toString()
            val token = UserPreferences.getString("token", "")

            val description = inputDescription.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            if (token != "") {
                addViewModel.addStory(imageMultipart, description, token)
                addViewModel.message.observe(this) { message ->
                    when (message) {
                        "success" -> {
                            progressBar.visibility = View.GONE
                            alertDialogSuccess()
                        }
                        else -> {
                            progressBar.visibility = View.GONE
                            showToast("Failed to upload, try again")
                        }
                    }
                }
            }
        } else {
            progressBar.visibility = View.GONE
            Toast.makeText(this@AddActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun alertDialogSuccess() {
        AlertDialog.Builder(this).apply {
            setTitle("Upload Successful")
            setMessage("Anda berhasil upload image. Ayo kembali ke halaman utama!")
            setPositiveButton("Lanjut") { _, _ ->
                showToast("upload successful!")
                onLoginSuccess()
            }
            create()
            show()
        }
    }

    private fun onLoginSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                // Silakan gunakan kode ini jika mengalami perubahan rotasi
                // rotateFile(file)
                getFile = file
                binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddActivity)
                getFile = myFile
                binding.previewImageView.setImageURI(uri)
            }
        }
    }
}