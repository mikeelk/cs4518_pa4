package com.wpics.baseballcompass.data
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitProvider{


    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()


    private val logging = HttpLoggingInterceptor().apply{
        level= HttpLoggingInterceptor.Level.BODY
    }


    private val client = OkHttpClient.Builder().addInterceptor(logging).build()


    private val retrofit = Retrofit.Builder().baseUrl("https://statsapi.mlb.com/api/v1/").client(client).addConverterFactory(
        MoshiConverterFactory.create(moshi)).build()


    val api: MLBAPI = retrofit.create(MLBAPI::class.java)

}