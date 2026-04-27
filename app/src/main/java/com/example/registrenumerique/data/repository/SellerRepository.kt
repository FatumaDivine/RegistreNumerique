package com.example.registrenumerique.data.repository

import com.example.registrenumerique.data.model.Seller
import com.example.registrenumerique.data.remote.supabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class SellerRepository {

    suspend fun getAllSellers(): List<Seller> = withContext(Dispatchers.IO) {
        supabaseClient.from("sellers").select().decodeList<Seller>()
    }

    suspend fun insertSeller(seller: Seller) = withContext(Dispatchers.IO) {
        supabaseClient.from("sellers").insert(seller)
    }

    suspend fun updateSeller(id: Int, seller: Seller) = withContext(Dispatchers.IO) {
        supabaseClient.from("sellers").update(seller) {
            filter {
                eq("id", id)
            }
        }
    }

    suspend fun deleteSeller(id: Int) = withContext(Dispatchers.IO) {
        supabaseClient.from("sellers").delete {
            filter {
                eq("id", id)
            }
        }
    }

    suspend fun uploadImage(byteArray: ByteArray, fileName: String): String = withContext(Dispatchers.IO) {
        val bucket = supabaseClient.storage.from("seller-images")
        val path = "images/${UUID.randomUUID()}_$fileName"
        bucket.upload(path, byteArray)
        supabaseClient.storage.from("seller-images").publicUrl(path)
    }
}
