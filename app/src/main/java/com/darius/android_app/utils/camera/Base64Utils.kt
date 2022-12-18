package com.darius.android_app.utils.camera

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.util.*

class Base64Utils {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun encodeImage(context: Context, uri: Uri): String {
            var encoded = ""
            try {
                val bytes = readBytes(context, uri)
                encoded = Base64.getEncoder().encodeToString(bytes)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return encoded
        }

        private fun readBytes(context: Context, uri: Uri): ByteArray? {
            var byteArray: ByteArray? = null
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                val baos: ByteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                byteArray = baos.toByteArray()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            return byteArray
        }
    }
}