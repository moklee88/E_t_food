 package com.example.etfood.ui.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.etfood.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

 class RegisterActivity : AppCompatActivity() {
     //view binding
    private lateinit var binding: ActivityRegisterBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog =  ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.backBtn.setOnClickListener{
          onBackPressed()
        }

        binding.registerBtn.setOnClickListener {
            validateData()
        }


    }

     private var name = ""
     private var email = ""
     private var password = ""

     private fun validateData() {
         name = binding.nameEt.text.toString().trim()
         email = binding.emailEt.text.toString().trim()
         password = binding.passwordEt.text.toString().trim()
         val cPassword = binding.cPasswordEt.text.toString().trim()

         if(name.isEmpty()){
             Toast.makeText(this,"Enter your name....", Toast.LENGTH_SHORT).show()
         }
         else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
             Toast.makeText(this,"Invalid email pattern....", Toast.LENGTH_SHORT).show()
         }
         else if(password.isEmpty()){
             Toast.makeText(this,"Enter your password....", Toast.LENGTH_SHORT).show()
         }
         else if(cPassword.isEmpty()){
             Toast.makeText(this,"Confirm password....", Toast.LENGTH_SHORT).show()
         }
         else if (password != cPassword){
             Toast.makeText(this,"Password doesn't match....", Toast.LENGTH_SHORT).show()
         }
         else {
             createUserAccount()
         }
     }

     private fun createUserAccount() {
         progressDialog.setMessage("Creating Account...")
         progressDialog.show()

         firebaseAuth.createUserWithEmailAndPassword(email, password)
             .addOnSuccessListener {
               updateUserInfo()
             }
             .addOnFailureListener{ e->
                 progressDialog.dismiss()
                 Toast.makeText(this,"Fail to create an account due to ${e.message}", Toast.LENGTH_SHORT).show()
             }
     }

     private fun updateUserInfo() {
         progressDialog.setMessage("Saving user info...")

         val timestamp = System.currentTimeMillis()

         val uid = firebaseAuth.uid

         val hashMap: HashMap<String, Any?> = HashMap()
         hashMap["uid"] = uid
         hashMap["email"] = email
         hashMap["name"] = name
         hashMap["profileImage"] =""
         hashMap["userType"] ="user"
         hashMap["timestamp"] =timestamp

         val ref = FirebaseDatabase.getInstance().getReference("users")
         ref.child(uid!!)
             .setValue(hashMap)
             .addOnSuccessListener {
              progressDialog.dismiss()
                 Toast.makeText(this,"Account created", Toast.LENGTH_SHORT).show()
                 startActivity(Intent(this@RegisterActivity, MenuActivity::class.java))
                 finish()
             }
             .addOnFailureListener{ e->
                 progressDialog.dismiss()
                 Toast.makeText(this,"Fail saving user info due to ${e.message}", Toast.LENGTH_SHORT).show()
             }
     }
 }