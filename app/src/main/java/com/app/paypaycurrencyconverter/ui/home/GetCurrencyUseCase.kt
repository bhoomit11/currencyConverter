package com.app.paypaycurrencyconverter.ui.home

import com.app.paypaycurrencyconverter.models.CurrenciesResponse
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetCurrencyUseCase @Inject constructor(
    private val mainCurrencyRepository: MainCurrencyRepository)
{

    suspend fun processCurrencyListUseCase(): List<CurrenciesResponse> {
        return mainCurrencyRepository.getCurrency()
    }
}