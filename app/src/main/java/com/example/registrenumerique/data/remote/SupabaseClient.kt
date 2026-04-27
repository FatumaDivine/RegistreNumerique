package com.example.registrenumerique.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.SessionSource
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

val supabaseClient = createSupabaseClient(
    supabaseUrl = SupabaseConfig.URL,
    supabaseKey = SupabaseConfig.ANON_KEY
) {
    install(Postgrest)
    install(Storage)
}
