package com.example.itunesapi

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("search")
    suspend fun search(
        @Query("term") term: String,
        @Query("country") country: String,
        @Query("media") media: String,
        @Query("entity") entity: String,
        @Query("limit") limit: Int
    ): Response<MusicList>

    companion object {
        fun create(): Api {
            return Retrofit.Builder()
                .baseUrl("https://itunes.apple.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }
    }
}

data class MusicList(@SerializedName("results") val results: List<Music>)

data class Music(
    @SerializedName("trackId") val id: Int,
    @SerializedName("collectionName") val collectionName: String,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artworkUrl100") val imageUrl: String,
    @SerializedName("previewUrl") val previewUrl: String)