package com.example.etfood.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.etfood.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginBtn.setOnClickListener{
         startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.SkipBtn.setOnClickListener{
            startActivity(Intent(this, MenuActivity::class.java))
        }

      //  binding.btnStart.setOnClickListener{ goMenu()}
    //}

    //private fun goMenu() {
      //  val intent = Intent(this, Menu::class.java)

        //startActivity(intent)
    }
}