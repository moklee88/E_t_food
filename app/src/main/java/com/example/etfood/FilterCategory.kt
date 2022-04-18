package com.example.etfood

import android.widget.Filter
import com.example.etfood.databinding.RowCategoryBinding

class FilterCategory:Filter {

    private var filterList: ArrayList<ModelCategory>

    private lateinit var adapterCategory: AdapterCategory

    constructor(filterList: ArrayList<ModelCategory>, adapterCategory: AdapterCategory):super(){
        this.filterList = filterList
        this.adapterCategory = adapterCategory


}

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val result = FilterResults()

        if(constraint !=null && constraint.isNotEmpty()){
            constraint =constraint.toString().uppercase()
            val filteredModel:ArrayList<ModelCategory> = ArrayList()
            for (i in 0 until filterList.size){
                if(filterList[i].category.uppercase().contains(constraint)){
                    filteredModel.add(filterList[i])
                }
            }
         result.count = filteredModel.size
            result.values = filteredModel
        }
        else{
            result.count = filterList.size
            result.values = filterList
        }
        return result
    }

    override fun publishResults(filterList: CharSequence?, results: FilterResults) {
     adapterCategory.categoryArrayList = results.values as ArrayList<ModelCategory>

        adapterCategory.notifyDataSetChanged()
    }
}