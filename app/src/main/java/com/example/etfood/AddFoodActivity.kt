package com.example.etfood

import android.app.AlertDialog
import android.app.Application
import android.app.ProgressDialog
import android.content.Intent
import android.icu.text.CaseMap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.etfood.databinding.ActivityAddFoodBinding
import com.example.etfood.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class AddFoodActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddFoodBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    private lateinit var categoryArrayList: ArrayList<ModelCategory>

    private var pdfUri : Uri? =null

    private val TAG = "ADD_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFoodBinding    .inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        loadCategories()

        progressDialog =  ProgressDialog(this)
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

        //handle click, show category pick dialog
        binding.categoryTv.setOnClickListener {
            categoryPickDialog()
        }

        //binding.attachBtn.setOnClickListener{
          //  pdfPickIntent()
        //}

        binding.submitBtn.setOnClickListener{

            validateData()
        }
    }

    private var title=""
    private var price=0.0
    private var category=""

    private fun validateData() {
        Log.d(TAG, "validateData: validating data")

        title = binding.titleEt.text.toString().trim()
        price = binding.priceEt.text.toString().toDoubleOrNull()?:0.0
        category = binding.categoryTv.text.toString().trim()

        if(title.isEmpty()){
            Toast.makeText(this,"Enter title...", Toast.LENGTH_SHORT).show()
        }
        else if (price<=0.0){
            Toast.makeText(this,"Enter price...", Toast.LENGTH_SHORT).show()
        }
        else if (category.isEmpty()){
            Toast.makeText(this,"Select Category...", Toast.LENGTH_SHORT).show()
        }
        //else if(pdfUri == null){
           // Toast.makeText(this,"Select pdf...", Toast.LENGTH_SHORT).show()
       // }

        else{
            //data been upload
            progressDialog.show()

            val timestamp = System.currentTimeMillis()

            val hashMap = HashMap<String, Any>()
            hashMap["uid"] = "${firebaseAuth.uid}"
            hashMap["id"] = "$timestamp"
            hashMap["title"] = "$title"
            hashMap["price"] = price
            hashMap["categoryId"] = "$SelectedCategoryId"
            hashMap["categoryTitle"] = "$SelectedCategoryTitle"
            hashMap["timestamp"] = timestamp
            hashMap["viewCount"] = 0




            val ref = FirebaseDatabase.getInstance().getReference("foods")
            ref.child("$timestamp")
                .setValue(hashMap)
                .addOnSuccessListener {
                    Log.d(TAG, "uploaded")
                    progressDialog.dismiss()
                    Toast.makeText(this,"successful uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{e->
                    Log.d(TAG, "UploadData: fail due to ${e.message}")
                    progressDialog.dismiss()
                    Toast.makeText(this,"fail due to ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }
    //private fun UploadData() {
     //  Log.d(TAG, "UploadData: Uploading data to storage...")

      // progressDialog.setMessage("uploading...")
       // progressDialog.show()

     //   val timestamp = System.currentTimeMillis()
      //  val filePathAndName = "Food/$timestamp"
     //   val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
      //  storageReference.putFile(pdfUri!!)
       //     .addOnSuccessListener {tasksnapshot->
        //        Log.d(TAG, "UploadData: pdf uploading")
       //         val uriTask:Task<Uri> = tasksnapshot.storage.downloadUrl
         //       while (!uriTask.isSuccessful);
          //      val uploadedPdfUrl = "${uriTask.result}"

        //        uploadIndoDb(uploadedPdfUrl,timestamp)

         //   }
        //    .addOnFailureListener {e->
           //     Log.d(TAG, "UploadData: fail due to ${e.message}")
             //   progressDialog.dismiss()
             //   Toast.makeText(this,"fail due to ${e.message}", Toast.LENGTH_SHORT).show()
           // }

  //  }

    //private fun uploadIndoDb() {
       // progressDialog.show()

       // val timestamp = System.currentTimeMillis()

      //  val hashMap = HashMap<String, Any>()
      //  hashMap["uid"] = "${firebaseAuth.uid}"
      //  hashMap["id"] = "$timestamp"
       // hashMap["title"] = "$title"
      //  hashMap["price"] = "$price"
       // hashMap["categoryId"] = "$SelectedCategoryId"
       // hashMap["categoryTitle"] = "$SelectedCategoryTitle"
      // hashMap["timestamp"] = timestamp
       // hashMap["viewCount"] = 0




       // val ref = FirebaseDatabase.getInstance().getReference("Foods")
       // ref.child("$timestamp")
        //    .setValue(hashMap)
       //     .addOnSuccessListener {
         //       Log.d(TAG, "uploaded")
         //       progressDialog.dismiss()
           //     Toast.makeText(this,"successful uploaded", Toast.LENGTH_SHORT).show()
          //  }
           // .addOnFailureListener{e->
           //     Log.d(TAG, "UploadData: fail due to ${e.message}")
           //     progressDialog.dismiss()
           //     Toast.makeText(this,"fail due to ${e.message}", Toast.LENGTH_SHORT).show()
          //  }


   // }

    private fun loadCategories() {
        Log.d(TAG, "loadCategories: Loading categories")

        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("categories")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for(ds in snapshot.children){
                    val model = ds.getValue(ModelCategory::class.java)
                    categoryArrayList.add(model!!)
                    Log.d(TAG, "onDataChange: ${model.category}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "uploadInfoToDb: uploading")
                progressDialog.setMessage("uploading pdf info...")

                val uid = firebaseAuth.uid

                val hashMap: HashMap<String, Any?> = HashMap()
                hashMap["uid"] = "$uid"
                hashMap["title"] = "$title"
                hashMap["categoryId"] ="$SelectedCategoryId"
                hashMap["viewsCount"] =0

                val ref = FirebaseDatabase.getInstance().getReference("foods")
                ref.child("$uid")
                    .setValue(hashMap)
                    .addOnSuccessListener {
                        Log.d(TAG, "uploadInfoToDb: uploading")
                        progressDialog.dismiss()
                    }
                    .addOnFailureListener {e->
                        Log.d(TAG, "uploadInfoToDb: fail due to ${e.message}")
                        progressDialog.dismiss()
                    }

            }

        })
    }
    private var SelectedCategoryId = ""
    private var SelectedCategoryTitle = ""

    private fun categoryPickDialog(){
        Log.d(TAG, "categoriesPickDialog: Showing pdf category pick dialog")
         val categoriesArray = arrayOfNulls<String>(categoryArrayList.size)
        for(i in categoryArrayList.indices){
            categoriesArray[i] = categoryArrayList[i].category
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Category")
            .setItems(categoriesArray) { dialog, which ->
                //handle click item
                SelectedCategoryTitle = categoryArrayList[which].category
                SelectedCategoryId = categoryArrayList[which].id

                binding.categoryTv.text = SelectedCategoryTitle

                Log.d(TAG, "categoriesPickDialog: Selected Category ID: $SelectedCategoryId")
                Log.d(TAG, "categoriesPickDialog: Selected Category Titled: $SelectedCategoryTitle")
            }
            .show()

    }
 private fun  pdfPickIntent(){
     Log.d(TAG, "pdfPickIntent: starting pick")

     val intent = Intent()
     intent.type = "application/dpf"
     intent.action = Intent.ACTION_GET_CONTENT
     pdfActivityResultLauncher.launch(intent)
 }
    val pdfActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>{ result->

            if(result.resultCode == RESULT_OK){
                Log.d(TAG, "PDF Picked:")
                pdfUri = result.data!!.data
            }
            else{
                Log.d(TAG, "PDF Pick cancelled  : ")
                Toast.makeText(this,"Cancelled", Toast.LENGTH_SHORT).show()

            }

        }
    )


}