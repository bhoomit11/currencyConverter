package com.app.paypaycurrencyconverter.ui.home

import com.app.paypaycurrencyconverter.utils.AppPreference
import com.app.paypaycurrencyconverter.webservice.WebService
import javax.inject.Inject

class MainCurrencyRepository @Inject constructor(
    private val webService: WebService,
    private val appPreference: AppPreference
){
    suspend fun getCurrency() = webService.getCurrency()
}