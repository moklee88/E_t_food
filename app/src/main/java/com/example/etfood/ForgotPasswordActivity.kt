package com.example.etfood

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.etfood.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    private lateinit var  firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog =  ProgressDialog(this)
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.backBtn.setOnClickListener{
            onBackPressed()
        }


        binding.submitBtn.setOnClickListener{
            validateData()
        }
    }

    private var email = ""
    private fun validateData() {
        email = binding.emailEt.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid email format...", Toast.LENGTH_SHORT).show()
        }
        else if (email.isEmpty()){
            Toast.makeText(this,"Enter Email...", Toast.LENGTH_SHORT).show()
        }
        else{
            recoverPassword()
        }

    }

    private fun recoverPassword() {
        progressDialog.setMessage("Sending password reset instruction to $email")
        progressDialog.show()
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Instruction have been send to \n $email", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this,"Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}