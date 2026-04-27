package com.example.registrenumerique.data.repository

import com.example.registrenumerique.data.model.Seller
import com.example.registrenumerique.data.remote.supabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.annotations.SupabaseExperimental
import java.util.UUID

class SellerRepository {

    suspend fun getAllSellers(): List<Seller> = withContext(Dispatchers.IO) {
        supabaseClient.from("sellers").select().decodeList<Seller>()
    }

    @OptIn(SupabaseExperimental::class)
    fun getSellersFlow(): Flow<List<Seller>> {
        return supabaseClient.from("sellers").selectAsFlow(primaryKey = Seller::id)
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
