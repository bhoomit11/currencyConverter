package com.app.paypaycurrencyconverter.ui.home.usecases

import com.app.paypaycurrencyconverter.models.CurrenciesResponse
import com.app.paypaycurrencyconverter.ui.home.MainCurrencyRepository
import com.google.gson.JsonElement
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class ConvertCurrencyUseCase @Inject constructor(
    private val mainCurrencyRepository: MainCurrencyRepository
) {
    suspend fun processConvertCurrencyUseCase(
        value: Double,
        from: String,
        to: String
    ): JsonElement? {
        return mainCurrencyRepository.convert(value,from,to)
    }
}