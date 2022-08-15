package com.app.paypaycurrencyconverter.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.app.paypaycurrencyconverter.models.CurrenciesResponse
import com.app.paypaycurrencyconverter.models.ResponseModel
import com.app.paypaycurrencyconverter.utils.AppPreference
import com.app.paypaycurrencyconverter.utils.isWorkScheduled
import com.app.paypaycurrencyconverter.worker.BackgroundTask
import com.app.paypaycurrencyconverter.worker.BackgroundTask.Companion.KEY_RESULT
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainCurrencyViewModel @Inject constructor(
    @ApplicationContext appContext: Context,
    private val mainCurrencyRepository: MainCurrencyRepository,
    private val gson: Gson,
    private val appPreference: AppPreference
) : ViewModel() {


    companion object {
        const val TAG_API_WORK = "myAPIwork";
    }

    private val responseLiveData = MutableLiveData<ResponseModel<List<CurrenciesResponse>>>()

    val response: LiveData<ResponseModel<List<CurrenciesResponse>>>
        get() = responseLiveData

    init {
        if (!appContext.isWorkScheduled(TAG_API_WORK)) { // check if your work is not already scheduled
            scheduleWorker(appContext) // schedule your work
            // To call API for the very first time
            if (appPreference.currencyData.isNotBlank()) {
                getEmployees()
            }
        }
    }

    /**
     * Scheduling periodic worker on every 30 min
     */
    private fun scheduleWorker(appContext: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED).build()

        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(BackgroundTask::class.java, 30, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(appContext).apply {
            enqueueUniquePeriodicWork(
                TAG_API_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )

            getWorkInfoByIdLiveData(periodicWorkRequest.id).observeForever {
                if (it != null && it.state.isFinished) {
                    val myResult = it.outputData.getBoolean(
                        KEY_RESULT,
                        false
                    )

                    if (myResult) getEmployees()
                }
            }
        }
    }

    /**
     * Get currency list from API
     */
    private fun getEmployees() = viewModelScope.launch {
        responseLiveData.postValue(ResponseModel.loading(null))
        if (appPreference.currencyData.isNotBlank()) {
            responseLiveData.postValue(ResponseModel.success(parseResponse()))
        } else {
            mainCurrencyRepository.getCurrency().let { res ->
                appPreference.currencyData =
                    res.body()?.asJsonObject.toString() //Stored data to local
                if (res.isSuccessful) {
                    responseLiveData.postValue(ResponseModel.success(parseResponse()))
                } else {
                    responseLiveData.postValue(
                        ResponseModel.error(
                            res.errorBody().toString(),
                            null
                        )
                    )
                }
            }
        }
    }

    private fun parseResponse(): List<CurrenciesResponse> {
        val map =
            gson.fromJson(appPreference.currencyData, HashMap<String, String>().javaClass)
        return map.entries.map { CurrenciesResponse(it.key, it.value) }
    }
}