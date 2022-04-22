package com.example.etfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.etfood.databinding.ActivityCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects

class CartActivity : AppCompatActivity() {

    private lateinit var binding:ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadCartItems()
    }


    private fun loadCartItems()
    {
        Firebase.firestore
            .collection("cart")
            .get()
            .addOnSuccessListener { items ->
                val list = items.toObjects<CartFood>()
                var result = ""

                list.forEach { f ->
                    result += "${f.id} ${f.name} ${f.price}\n"
                    //binding display
                }
            }
    }
}