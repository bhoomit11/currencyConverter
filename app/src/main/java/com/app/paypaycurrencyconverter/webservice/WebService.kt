package com.app.paypaycurrencyconverter.webservice

import com.app.paypaycurrencyconverter.BuildConfig
import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.GET

interface WebService {
    companion object{
        const val BASE_URL = BuildConfig.BASE_URL
    }

    @GET("currencies.json")
    suspend fun getCurrency():Response<JsonElement>
}