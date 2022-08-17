package com.app.paypaycurrencyconverter.ui.home

import com.app.paypaycurrencyconverter.models.CurrenciesResponse
import com.app.paypaycurrencyconverter.utils.AppPreference
import com.app.paypaycurrencyconverter.webservice.WebService
import com.google.gson.Gson
import com.google.gson.JsonElement
import javax.inject.Inject

class MainCurrencyRepository @Inject constructor(
    private val webService: WebService,
    private val gson: Gson,
    private val appPreference: AppPreference
) {

    suspend fun getCurrency() = processCurrencyFetchLogic()

    private suspend fun processCurrencyFetchLogic(): List<CurrenciesResponse> {
        if (appPreference.currencyData.isBlank()) {
            val res = webService.getCurrency()
            appPreference.currencyData = res.body()?.asJsonObject.toString()
        }
        val map =
            gson.fromJson(appPreference.currencyData, HashMap<String, String>().javaClass)
        return map.entries.map { CurrenciesResponse(it.key, it.value) }
    }

    suspend fun convert(
        value: Double,
        from: String,
        to: String
    ) = processCurrencyConvertLogic(value,from,to)

     private suspend fun processCurrencyConvertLogic(value: Double, from: String, to: String): JsonElement? {
        return webService.convert(value, from, to).body()
    }
}