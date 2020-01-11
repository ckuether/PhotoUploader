package com.example.photouploader

import android.graphics.Bitmap

interface PhotoUploaderCallback {
    fun updateSavedBitmap(bitmap: Bitmap?)
}