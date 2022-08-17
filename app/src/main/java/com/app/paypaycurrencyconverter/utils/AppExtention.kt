package com.app.paypaycurrencyconverter.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.CheckResult
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.os.postDelayed
import androidx.core.widget.doAfterTextChanged
import androidx.work.WorkInfo

import androidx.work.WorkManager
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import java.util.concurrent.ExecutionException


fun Context.isWorkScheduled(tag: String): Boolean {
    val instance = WorkManager.getInstance(this)
    val statuses: ListenableFuture<List<WorkInfo>> = instance.getWorkInfosByTag(tag)
    return try {
        var running = false
        val workInfoList: List<WorkInfo> = statuses.get()
        for (workInfo in workInfoList) {
            val state = workInfo.state
            running = state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED
        }
        running
    } catch (e: ExecutionException) {
        false
    } catch (e: InterruptedException) {
        false
    }
}
