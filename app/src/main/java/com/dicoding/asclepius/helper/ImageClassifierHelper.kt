package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import com.dicoding.asclepius.ml.CancerClassification
import org.tensorflow.lite.support.image.TensorImage

class ImageClassifierHelper(private val context: Context) {
    fun classifyStaticImage(bitmap: Bitmap): Pair<String, String> {
        val model = CancerClassification.newInstance(context)
        val image = TensorImage.fromBitmap(bitmap)

        val outputs = model.process(image)
        val probability = outputs.probabilityAsCategoryList

        val result = probability.maxByOrNull { it.score }

        model.close()

        val confidencePercentage = (result?.score ?: 0f) * 100
        return Pair(result?.label ?: "Tidak teridentifikasi nih boy", String.format("%.2f%%", confidencePercentage))
    }
}