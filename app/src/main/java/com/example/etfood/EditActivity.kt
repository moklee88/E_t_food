
package com.example.etfood

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.etfood.databinding.ActivityEditBinding
import com.example.etfood.databinding.RowAdminBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditActivity : AppCompatActivity() {

    private lateinit var binding:ActivityEditBinding

    private companion object{
        private const val TAG = "EDIT_TAG"
    }

    private var Id = ""

    private lateinit var progressDialog: ProgressDialog

    private lateinit var categoryTitleArrayList: ArrayList<String>

    private lateinit var categoryIdArrayList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Id = intent.getStringExtra("Id")!!

        progressDialog =  ProgressDialog(this)
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)

        loadCategories()
        LoadInfo()

        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

        binding.categoryTv.setOnClickListener {
            categoryDialog()
        }

        binding.submitBtn.setOnClickListener {
         validateData()
        }
    }

    private fun LoadInfo() {
        Log.d(TAG, "loadFoodInfo: laoding food info...")

        val ref = FirebaseDatabase.getInstance().getReference("foods")
        ref.child(Id)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    selectedCategoryId = snapshot.child("categoryId").value.toString()
                    val price = snapshot.child("price").value.toString()
                    val title = snapshot.child("title").value.toString()

                    Log.d(TAG, "onDataChange: Loading foods category info")
                    val refFoodCategory = FirebaseDatabase.getInstance().getReference("categories")
                    refFoodCategory.child(selectedCategoryId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val category = snapshot.child("category").value
                                binding.categoryTv.text = category.toString()
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                            
                        })

                }

                override fun onCancelled(error: DatabaseError) {

                }


            })

    }

    private var title = ""
    private var price = 0.0
    private fun validateData() {
        title = binding.titleEt.text.toString().trim()
        price = binding.priceEt.text.toString().toDoubleOrNull()?:0.0

        if(title.isEmpty()){
            Toast.makeText(this,"Enter title...", Toast.LENGTH_SHORT).show()
        }
        else if (price<=0.0){
            Toast.makeText(this,"Enter price...", Toast.LENGTH_SHORT).show()
        }
        else if (selectedCategoryId.isEmpty()){
            Toast.makeText(this,"Pick Category...", Toast.LENGTH_SHORT).show()
        }
        else{
            update()
        }

    }

    private fun update() {
        Log.d(TAG, "Update: Starting update information...")

        progressDialog.setMessage("uploading food info...")
        progressDialog.show()

        val hashMap = HashMap<String, Any>()
        hashMap["title"] = "$title"
        hashMap["price"] =price
        hashMap["categoryId"] = "$selectedCategoryId"

        //Start updating
        val ref = FirebaseDatabase.getInstance().getReference("foods")
        ref.child(Id)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Log.d(TAG, "Update:Successful update information")

                Toast.makeText(this,"Successful update information...", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                Log.d(TAG, "Update: Fail to update information due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this,"Fail to update information due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

    private var selectedCategoryId = ""
    private var selectedCategoryTitle = ""

    private fun categoryDialog() {
        val categoriesArray = arrayOfNulls<String>(categoryTitleArrayList.size)
        for(i in categoryTitleArrayList.indices){
            categoriesArray[i] = categoryTitleArrayList[i]
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Category")
            .setItems(categoriesArray){dialog, position->

                selectedCategoryId = categoryIdArrayList[position]
                selectedCategoryTitle = categoryTitleArrayList[position]

                binding.categoryTv.text = selectedCategoryTitle

            }
            .show()

    }



    private fun loadCategories() {
        Log.d(TAG, "loadCategories: Loading categories...")

        categoryTitleArrayList = ArrayList()
        categoryIdArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("categories")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryTitleArrayList.clear()
                categoryIdArrayList.clear()

                for (ds in snapshot.children){
                    val id = "${ds.child("id").value}"
                    val category = "${ds.child("category").value}"

                    categoryTitleArrayList.add(category)
                    categoryIdArrayList.add(id)

                    Log.d(TAG, "onDataChange: Category ID $id")
                    Log.d(TAG, "onDataChange: Category  $category")
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}