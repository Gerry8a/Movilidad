package com.bancomer.bbva.bbvamovilidad.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object BitmapUtils {
    fun stringToBitmap(image64: String): Bitmap{
        val decodeString = Base64.decode(image64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodeString, 0, decodeString.size)
    }
}