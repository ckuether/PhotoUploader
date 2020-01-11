package com.example.photouploader

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_chat.*
import java.io.ByteArrayOutputStream

class ChatFragment : Fragment() {

    private var savedBitmap: Bitmap? = null

    companion object {
        fun newInstance(): ChatFragment{
            return ChatFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = container?.inflate(R.layout.fragment_chat)
        return view
    }

    override fun onResume() {
        super.onResume()

        savedBitmap = (context as MainActivity).savedBitmap
        if(savedBitmap != null) {
            saved_image.setImageBitmap(savedBitmap)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        save_image.setOnClickListener {
            if(savedBitmap != null){
                saveImageToFirebase()
            }
        }
    }

    private fun saveImageToFirebase(){
        val uploadImagePath: StorageReference = FirebaseStorage.getInstance().reference.child("TEST")

        val baos = ByteArrayOutputStream()
        savedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val dataToUpload: ByteArray = baos.toByteArray()
        val uploadTask: UploadTask = uploadImagePath.putBytes(dataToUpload)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            val imageUrl = taskSnapshot.storage.downloadUrl

            activity!!.showToast(imageUrl.toString())
        }
    }
}