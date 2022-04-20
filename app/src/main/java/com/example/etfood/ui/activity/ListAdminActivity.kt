package com.example.etfood.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.etfood.databinding.ActivityListAdminBinding
import com.example.etfood.ui.adapter.AdapterFoodAdmin
import com.example.etfood.ui.model.ModelFood
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListAdminBinding

    private companion object{
        const val TAG = "FOOD_LIST_ADMIN_TAG" +""
    }

    private var categoryId = ""
    private var category = ""

    private lateinit var foodArrayList: ArrayList<ModelFood>
    private lateinit var adapterFoodAdmin: AdapterFoodAdmin


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        categoryId = intent.getStringExtra("categoryId")!!
        category = intent.getStringExtra("category")!!

        binding.subTitleTv.text = category
        loadList()

        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

    }

    private fun loadList() {

        foodArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("foods")
        ref.orderByChild("categoryId").equalTo(categoryId)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   foodArrayList.clear()
                    for(ds in snapshot.children){
                        val model = ds.getValue(ModelFood::class.java)

                        if (model != null) {
                            foodArrayList.add(model)
                            Log.d(TAG,"onDataChange: ${model.title} ${model.categoryId}")
                        }
                    }

                    adapterFoodAdmin= AdapterFoodAdmin(this@ListAdminActivity, foodArrayList)
                    binding.foodsRv.adapter = adapterFoodAdmin

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }



}