package com.example.photouploader

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_story.*
import android.provider.MediaStore
import android.media.ExifInterface
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class StoryFragment : Fragment() {

    companion object {

        const val PICK_IMAGE_REQUEST = 1

        fun newInstance(): StoryFragment {
            return StoryFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = container?.inflate(R.layout.fragment_story)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upload_photo_btn.setOnClickListener {
            openFileChooser()
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

            data.data?.let { returnUri ->

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val uriInputStream: InputStream = context!!.contentResolver.openInputStream(returnUri)!!
                    val exif = ExifInterface(uriInputStream)

                    val dateTime: String = exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)

                    val date: Date = SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(dateTime)

                    val calendar = Calendar.getInstance()
                    val timeMillis = date.time
                    calendar.timeInMillis = timeMillis

                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    val month = calendar.get(Calendar.MONTH)
                    val year = calendar.get(Calendar.YEAR)

                    var loadedBitmap: Bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, returnUri)
                    loadedBitmap = updateOrientation(loadedBitmap, exif)

                    upload_image.setImageBitmap(loadedBitmap)
                } else {
                    activity!!.showToast("Android SDK 23 & below not supported")
                }

            }

        }
    }

    private fun updateOrientation(bitmap: Bitmap, exif: ExifInterface): Bitmap{
        val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        var loadedBitmap: Bitmap = bitmap
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> loadedBitmap = rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> loadedBitmap = rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> loadedBitmap = rotateBitmap(bitmap, 270f)
        }

        return loadedBitmap
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}