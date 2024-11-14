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
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ImageViewModel

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.imageUri = it
            binding.previewImageView.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }

        viewModel.imageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun startGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun analyzeImage() {
        viewModel.imageUri?.let { uri ->
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            val classifier = ImageClassifierHelper(this)
            val (resultText, confidenceScore) = classifier.classifyStaticImage(bitmap)
            moveToResult(resultText, confidenceScore)
        } ?: showToast("Pilih gambar terlebih dahulu coy.")
    }

    private fun moveToResult(resultText: String, confidenceScore: String) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("RESULT_TEXT", resultText + " $confidenceScore")
            putExtra("RESULT_IMAGE_URI", viewModel.imageUri.toString())
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}