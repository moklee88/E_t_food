package com.example.etfood.ui.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.etfood.R
import com.example.etfood.databinding.ActivityLoginBinding
import com.example.etfood.databinding.ActivityProfileEditBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var imageUri: Uri?= null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog =  ProgressDialog(this)
        progressDialog.setTitle("PLease wait")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        loadUserInfo()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.profileTv.setOnClickListener {
            showImageAttachMenu()
        }

        binding.updateButton.setOnClickListener {
            validateData()
        }
    }
    private var name = ""
    private fun validateData() {
        //get data
        name = binding.nameEt.text.toString().trim()

        if(name.isEmpty()){
            Toast.makeText(this,"Please enter name", Toast.LENGTH_SHORT).show()
        }
        else{
         //name is entered
            if(imageUri == null){
                //uploaded without images
                updateProfile("")
            }
            else{
                //uploaded with images
                uploadedImage()
            }

        }
    }

    private fun uploadedImage() {
        progressDialog.setMessage("Uploading profile image")
        progressDialog.show()

        //image path and name , use uid to replace previous
        val filePathAndName = "profileImages/" + firebaseAuth.uid

        //storage reference 
        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(imageUri!!)
            .addOnSuccessListener {taskSnapshot->

                //images uploaded , get url of uploaded image
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while(!uriTask.isSuccessful);
                val uploadedImageUrl = "${uriTask.result}"

                updateProfile(uploadedImageUrl)
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this,"Failed uploaded image due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProfile(uploadedImageUrl: String) {

        progressDialog.setMessage("Updating profile....")
        //setup info to update to db
        val hashMap:HashMap<String, Any> = HashMap()
        hashMap["name"] = "$name"
        if (imageUri != null){
            hashMap["profileImage"] = uploadedImageUrl
        }

        //update to db
        val reference = FirebaseDatabase.getInstance().getReference("users")
        reference.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"profile uploaded", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to uploaded profile  due to ${e.message}", Toast.LENGTH_SHORT).show()

            }

    }

    private fun loadUserInfo() {

        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = "${snapshot.child("name").value}"
                    val profileImage = "${snapshot.child("profileImage").value}"
                    val timestamp = "${snapshot.child("timestamp").value}"

                    binding.nameEt.setText(name)

                    try{
                        Glide.with(this@ProfileEditActivity)
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

    private fun showImageAttachMenu(){
//Show option to pick picture
        val popupMenu = PopupMenu(this,binding.profileTv)
        popupMenu.menu.add(Menu.NONE, 0,0, "Camera")
        popupMenu.menu.add(Menu.NONE, 1,1, "Gallery")
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {item->

            val id = item.itemId
            if(id == 0){
                pickImageCamera()
            }
            else if (id == 1){
                pickImageGallery()
            }
            true
        }


    }

    private fun pickImageCamera(){
        //intent to pick image from camera
    val values  = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
    }
    //handle result of camera intent(new ways in replacement of start activity for results)
    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {result ->

            //get uri of image
            if (result.resultCode == Activity.RESULT_OK){
                val data = result.data
                // imageUri = data!!.data

                //set image view
                binding.profileTv.setImageURI(imageUri)
            //   Toast.makeText(this,"Successful capture", Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(this,"Cancelled", Toast.LENGTH_SHORT).show()
            }

        }
    )

    private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }


    //handle result of camera intent(new ways in replacement of start activity for results)
    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>{result ->

            //get uri of image
            if (result.resultCode == Activity.RESULT_OK){

                val data = result.data
                imageUri = data!!.data

                //set image view
                binding.profileTv.setImageURI(imageUri)
                //Toast.makeText(this,"Successful pick", Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(this,"Cancelled", Toast.LENGTH_SHORT).show()
            }

        }
    )
}