package com.example.etfood.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.etfood.CartActivity
import com.example.etfood.databinding.ActivityMenuBinding
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    //TOAST
    val text = "Not Available"
    val duration = Toast.LENGTH_SHORT
    val toast = Toast.makeText(applicationContext, text, duration)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.orderBtn.setOnClickListener{
            startActivity(Intent(this, DashboardAdminActivity::class.java))
        }
        binding.cartBtn.setOnClickListener{
            startActivity(Intent(this, CartActivity::class.java))
        }
        binding.historyBtn.setOnClickListener{
            toast.show()
        }
        binding.profileBtn.setOnClickListener{
            startActivity(Intent(this, profileActivity::class.java))
        }


    }

}