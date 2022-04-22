package com.example.etfood.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.etfood.CartFood
import com.example.etfood.ui.activity.ListAdminActivity
import com.example.etfood.databinding.RowCartListBinding

class AdapterCart :RecyclerView.Adapter<AdapterCart.HolderCategory>{

    private var context: Context
    public var cartFoodArrayList: ArrayList<CartFood>
    //private var filterList: ArrayList<ModelCategory>

    private lateinit var binding : RowCartListBinding

    constructor(context: Context, cartFoodArrayList: ArrayList<CartFood>) {
        this.context = context
        this.cartFoodArrayList = cartFoodArrayList
        //this.filterList = categoryArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
      binding = RowCartListBinding.inflate(LayoutInflater.from(context), parent,false)
        return HolderCategory(binding.root)
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
       val model = cartFoodArrayList[position]
        val id = model.id
        val name = model.name
        val price = model.price

        //set data
        holder.name.text = name
        holder.price.text = "%.2f".format(price)

    }

    override fun getItemCount(): Int {
       return cartFoodArrayList.size
    }

    inner class HolderCategory(itemView:View): RecyclerView.ViewHolder(itemView){

        var name:TextView = binding.titleEt
        var price:TextView = binding.priceEt

    }

}