package com.app.paypaycurrencyconverter.worker

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.AssistedInject

class BackgroundTask @AssistedInject constructor(
    context: Context,
    params: WorkerParameters,
) : Worker(context, params) {
    companion object{
        const val KEY_RESULT = "result"
    }

    override fun doWork(): Result {
        val output = workDataOf(KEY_RESULT to true)
        return Result.success(output)
    }
}