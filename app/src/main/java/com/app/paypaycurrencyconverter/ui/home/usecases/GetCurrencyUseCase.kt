package com.app.paypaycurrencyconverter.ui.home.usecases

import com.app.paypaycurrencyconverter.models.CurrenciesResponse
import com.app.paypaycurrencyconverter.ui.home.MainCurrencyRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetCurrencyUseCase @Inject constructor(
    private val mainCurrencyRepository: MainCurrencyRepository
)
{

    suspend fun processCurrencyListUseCase(): List<CurrenciesResponse> {
        return mainCurrencyRepository.getCurrency()
    }
}