package com.app.paypaycurrencyconverter.webservice

import com.app.paypaycurrencyconverter.BuildConfig
import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WebService {
    companion object{
        const val BASE_URL = BuildConfig.BASE_URL
    }

    @GET("currencies.json")
    suspend fun getCurrency():Response<JsonElement>

    @GET("convert/{value}/{from}/{to}")
    suspend fun convert(
        @Path("value") value: Double,
        @Path("from") from: String,
        @Path("to") to: String
    ): Response<JsonElement>
}