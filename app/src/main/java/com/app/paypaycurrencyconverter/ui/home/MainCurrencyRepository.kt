package com.app.paypaycurrencyconverter.ui.home

import com.app.paypaycurrencyconverter.models.CurrenciesResponse
import com.app.paypaycurrencyconverter.models.ResponseModel
import com.app.paypaycurrencyconverter.utils.AppPreference
import com.app.paypaycurrencyconverter.webservice.WebService
import com.google.gson.Gson
import javax.inject.Inject

class MainCurrencyRepository @Inject constructor(
    private val webService: WebService,
    private val gson: Gson,
    private val appPreference: AppPreference
) {

    suspend fun getCurrency(): List<CurrenciesResponse> {
        return processDataFetchLogic()
    }

    private suspend fun processDataFetchLogic(): List<CurrenciesResponse> {
        if (appPreference.currencyData.isBlank()) {
            val res = webService.getCurrency()
            appPreference.currencyData = res.body()?.asJsonObject.toString()
        }
        return parseResponse()
    }

    private fun parseResponse(): List<CurrenciesResponse> {
        val map =
            gson.fromJson(appPreference.currencyData, HashMap<String, String>().javaClass)
        return map.entries.map { CurrenciesResponse(it.key, it.value) }
    }
}