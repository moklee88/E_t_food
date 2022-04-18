package com.example.etfood

import android.widget.Filter

class FilterFood: Filter {

      var filterList : ArrayList<ModelFood>

      var adapterFoodAdmin: AdapterFoodAdmin

    constructor(filterList: ArrayList<ModelFood>, adapterFoodAdmin: AdapterFoodAdmin){
        this.filterList = filterList
        this.adapterFoodAdmin = adapterFoodAdmin
    }


    override fun performFiltering(constraint: CharSequence?): FilterResults {
        //var constraint = CharSequence? = constraint
        val result = FilterResults()
        if(constraint !=null && constraint.isNotEmpty()){
           // constraint =constraint.toString().lowercase()
           // val filteredModels:ArrayList<ModelFood>()
            for (i in filterList.indices){
                if(filterList[i].title.lowercase().contains(constraint)){
                //   filteredModels.add(filterList[i])
                }
            }
           // result.count = filteredModels.size
           // result.values = filteredModels
        }
        else{
            result.count = filterList.size
            result.values = filterList
        }
        return result
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapterFoodAdmin.foodArrayList = results.values as ArrayList<ModelFood>

        adapterFoodAdmin.notifyDataSetChanged()
    }
}