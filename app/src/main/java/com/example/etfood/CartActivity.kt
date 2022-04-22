package com.example.etfood

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.etfood.databinding.ActivityCartBinding
import com.example.etfood.ui.adapter.AdapterCart
import com.example.etfood.ui.adapter.AdapterCategory
import com.example.etfood.ui.model.ModelCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects

class CartActivity : AppCompatActivity() {

    private lateinit var binding:ActivityCartBinding

    private lateinit var cartFoodArrayList:ArrayList<CartFood>

    private lateinit var adapterCart: AdapterCart

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadCartItems()

        binding.totalPrice.text = "RM${getTotal()}"

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.placeOrderBtn.setOnClickListener{
            placeOrder()
        }
    }

    private fun placeOrder()
    {
        var list: List<CartFood>? = null
        Firebase.firestore
            .collection("cart")
            .get()
            .addOnSuccessListener { items ->
                list = items.toObjects()
            }
        //Find Collection size
        var count = 0;
        Firebase.firestore
            .collection("foods")
            .get()
            .addOnSuccessListener { snap->
                count = snap.size()
            }

        for(food in list!!)
        {
            Firebase.firestore
                .collection("foods")
                .document("F${"%.2f".format(++count)}")
                .set(food)
        }

        //TOAST
        val text = "Order been placed"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)

        toast.show()

    }

    private fun loadCartItems()
    {
        cartFoodArrayList = ArrayList()

        Firebase.firestore
            .collection("cart")
            .get()
            .addOnSuccessListener { items ->
                val list = items.toObjects<CartFood>()

                list.forEach { f ->
                    val food = CartFood(f.id, f.name, f.price, f.status, f.categoryId)

                    cartFoodArrayList.add(food)
                }

                adapterCart = AdapterCart(this@CartActivity, cartFoodArrayList)
                binding.cartList.adapter = adapterCart
            }
    }

    private fun getTotal(): Double {
        var total = 0.0;
        Firebase.firestore
            .collection("cart")
            .get()
            .addOnSuccessListener { items ->
                val list = items.toObjects<CartFood>()

                list.forEach { f -> total += f.price }
            }

        return total;
    }
}