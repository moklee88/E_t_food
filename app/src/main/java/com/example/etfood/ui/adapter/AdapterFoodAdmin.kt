package com.example.etfood.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.etfood.CartFood
import com.example.etfood.databinding.RowAdminBinding
import com.example.etfood.ui.activity.ListAdminActivity
import com.example.etfood.ui.model.ModelFood
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AdapterFoodAdmin :RecyclerView.Adapter<AdapterFoodAdmin.HolderFood>{
    private var context: Context

    public var foodArrayList: ArrayList<ModelFood>
    private val filterList:ArrayList<ModelFood>

    private lateinit var binding: RowAdminBinding


    constructor(context: Context, foodArrayList: ArrayList<ModelFood>) :super(){
        this.context = context
        this.foodArrayList = foodArrayList
        this.filterList = foodArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderFood {
        binding = RowAdminBinding.inflate(LayoutInflater.from(context), parent,false)
        return HolderFood(binding.root)
    }

    override fun onBindViewHolder(holder: HolderFood, position: Int) {
        val model = foodArrayList[position]
        val id = model.id
        val categoryTitle = model.categoryTitle
        val title = model.title
        val price = model.price
        val timestamp = model.timestamp

        //TOAST
        val text = "$title has been added!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(context, text, duration)

        //recycleView set Data
        holder.foodName.text = title
        holder.price.text = "%.2f".format(price)

        //Find Collection size
        var count = 0;
        Firebase.firestore
            .collection("cart")
            .get()
            .addOnSuccessListener { snap->
                count = snap.size()
            }
        //AddToCart
        holder.addToCart.setOnClickListener{
            val addItem = CartFood(id,title,price, "pending", categoryTitle[0].toString())

            Firebase.firestore
                .collection("cart")
                .document("I${"%.2f".format(++count)}")
                .set(addItem)
                .addOnSuccessListener { toast.show() }
        }
    }
    /*
    private fun deleteCategory(model: ModelFood, holder: HolderFood) {
        val id = model.id

        val ref = FirebaseDatabase.getInstance().getReference("foods")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context,"Deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Toast.makeText(context,"Unable to delete due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

     */


    override fun getItemCount(): Int {
        return foodArrayList.size
    }


    inner class HolderFood(itemView:View): RecyclerView.ViewHolder(itemView){

        val foodName = binding.titleEt
        val price = binding.priceEt
        var addToCart:ImageButton = binding.addToCartBtn

    }

}