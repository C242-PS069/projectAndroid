package com.dicoding.asclepius.view

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resultImageUri = intent.getStringExtra("RESULT_IMAGE_URI")?.let { Uri.parse(it) }
        val resultTextWithConfidence = intent.getStringExtra("RESULT_TEXT") ?: "Hasil tidak tersedia"

        resultImageUri?.let { binding.resultImage.setImageURI(it) }
            ?: binding.resultImage.setImageResource(R.drawable.ic_place_holder)

        binding.resultText.text = resultTextWithConfidence
    }
}