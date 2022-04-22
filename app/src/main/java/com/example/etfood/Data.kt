package com.example.etfood

import com.google.firebase.firestore.DocumentId

data class CartFood(
    @DocumentId
    var id: String ="",
    var name: String ="",
    var price: Double =0.00,
    var status: String ="",
    var categoryId: String ="",
)