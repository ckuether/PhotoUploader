package com.example.photouploader.loginRegistration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import android.widget.Toast
import com.example.photouploader.MainActivity
import com.example.photouploader.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseAuthStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        firebaseAuthStateListener = FirebaseAuth.AuthStateListener {firebaseAuth ->
            val user = FirebaseAuth.getInstance().currentUser
            if(user != null){
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
                return@AuthStateListener
            }
        }

        mAuth = FirebaseAuth.getInstance()

        registration.setOnClickListener {
            val name = name.text.toString()
            val email = email.text.toString()
            val password = password.text.toString()
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task ->
                if(!task.isSuccessful){
                    Toast.makeText(this, "Sign in ERROR", Toast.LENGTH_LONG).show()
                }else{
                    val userId = mAuth.currentUser!!.uid
                    val currentUserDb = FirebaseDatabase.getInstance().reference.child("users").child(userId)

                    val userInfo = HashMap<String, Any>()
                    userInfo["email"] = email
                    userInfo["name"] = name
                    userInfo["profileImageUrl"] = "default"

                    currentUserDb.updateChildren(userInfo)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(firebaseAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(firebaseAuthStateListener)
    }
}