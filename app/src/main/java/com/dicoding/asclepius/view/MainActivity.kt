package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.helper.ImageViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var imageViewModel: ImageViewModel

    private val galleryPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri: Uri? ->
        imageUri?.let {
            imageViewModel.imageUri = it
            mainBinding.previewImageView.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        imageViewModel = ViewModelProvider(this).get(ImageViewModel::class.java)

        mainBinding.galleryButton.setOnClickListener { openGallery() }
        mainBinding.analyzeButton.setOnClickListener { processImage() }

        imageViewModel.imageUri?.let {
            mainBinding.previewImageView.setImageURI(it)
        }
    }

    private fun openGallery() {
        galleryPicker.launch("image/*")
    }

    private fun processImage() {
        imageViewModel.imageUri?.let { uri ->
            val bitmapImage = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            val imageClassifier = ImageClassifierHelper(this)
            val (classificationResult, confidence) = imageClassifier.classifyStaticImage(bitmapImage)
            navigateToResult(classificationResult, confidence)
        } ?: displayToast("Silakan pilih gambar terlebih dahulu")
    }

    private fun navigateToResult(classificationResult: String, confidence: String) {
        val resultIntent = Intent(this, ResultActivity::class.java).apply {
            putExtra("RESULT_TEXT", "$classificationResult $confidence")
            putExtra("RESULT_IMAGE_URI", imageViewModel.imageUri.toString())
        }
        startActivity(resultIntent)
    }

    private fun displayToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}