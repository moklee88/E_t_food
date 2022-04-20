package com.example.etfood.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.etfood.R
import com.example.etfood.databinding.ActivityLoginBinding
import com.example.etfood.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class profileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        loadUserInfo()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }


        binding.profileEditBtn.setOnClickListener {
         startActivity(Intent(this,ProfileEditActivity::class.java))
        }
    }

    private fun loadUserInfo() {
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                 val email = "${snapshot.child("email").value}"
                 val name = "${snapshot.child("name").value}"
                 val profileImage = "${snapshot.child("profileImage").value}"
                 val timestamp = "${snapshot.child("timestamp").value}"
                 val uid = "${snapshot.child("uid").value}"
                 val userType = "${snapshot.child("userType").value}"

                    binding.nameTv.text = name
                    binding.AccountTypeTv.text = userType
                    binding.emailTv.text = email

                    try{
                        Glide.with(this@profileActivity)
                            .load(profileImage)
                            .placeholder(R.drawable.ic_person_gray)
                            .into(binding.profileTv)
                    }
                    catch (e: Exception){

                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}