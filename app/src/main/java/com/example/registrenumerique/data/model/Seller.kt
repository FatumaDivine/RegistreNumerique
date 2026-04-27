package com.example.registrenumerique.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Seller(
    val id: Int? = null,
    val name: String,
    val first_name: String = "",
    val table_number: String,
    val category: String,
    val contact: String = "",
    val image_url: String? = null,
    val created_at: String? = null
)
