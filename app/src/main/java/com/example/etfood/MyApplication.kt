package com.example.etfood

import android.app.Application
import android.app.ProgressDialog
import android.text.format.DateFormat
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object{

        fun formatTimeStamp(timestamp:Long):String{
           val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timestamp

            return DateFormat.format("dd/mm/yy",cal).toString()

        }
        fun loadCategory(categoryId:String, categoryTv:TextView){

            val ref = FirebaseDatabase.getInstance().getReference("categories")
                   ref.child(categoryId)
                       .addListenerForSingleValueEvent(object:ValueEventListener{
                           override fun onDataChange(snapshot: DataSnapshot) {

                               val category = "${snapshot.child("category").value}"

                               categoryTv.text = category
                           }

                           override fun onCancelled(error: DatabaseError) {

                           }
                       })

        }


       /* fun deleteFood(context : Context, Id: String, price:Double, title:String){

            val TAG = "DELETE_BOOK_TAG"

            Log.d(TAG, "deleteBook:deleting.....")

            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("PLease wait")
            progressDialog.setMessage("Deleting $title...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            Log.d(TAG, "deleteFood:Deleting from storage...")
            val storageReference = FirebaseStorage.getInstance().getReference("foods")
            storageReference.delete()
                .addOnSuccessListener {
                    Log.d(TAG, "deleteFoods : Deleted from storage...")
                    Log.d(TAG, "deleteFoods : Deleted from db now...")

                    val ref = FirebaseDatabase.getInstance().getReference("foods")
                    ref.child(Id)
                        .removeValue()
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Log.d(TAG, "deleteFoods : Successful deleted")
                            Toast.makeText(context,"Successful deleted", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {e->
                            progressDialog.dismiss()
                            Log.d(TAG, "deleteFoods : Unable to delete due to ${e.message}")
                            Toast.makeText(context,"Unable to delete due to ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener{

                }


        }*/

    }

}