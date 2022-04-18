package com.example.etfood

class ModelFood {

    var uid:String =""
    var id:String =""
    var title:String =""
    var price:Double =0.0
    var categoryId:String =""
    var categoryTitle:String =""
    var timestamp:Long =0
    var viewCount:Long =0

    constructor()
    constructor(
        uid: String,
        id: String,
        title: String,
        price: Double,
        categoryId: String,
        categoryTitle: String,
        timestamp: Long,
        viewCount: Long
    ) {
        this.uid = uid
        this.id = id
        this.title = title
        this.price = price
        this.categoryId = categoryId
        this.categoryTitle = categoryTitle
        this.timestamp = timestamp
        this.viewCount = viewCount
    }


}