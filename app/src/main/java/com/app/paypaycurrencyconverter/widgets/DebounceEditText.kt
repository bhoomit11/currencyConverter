package com.app.paypaycurrencyconverter.widgets

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class DebounceEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attributeSet, defStyleAttr) {

    private val debouncePeriod = 600L
    private var searchJob: Job? = null

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun setOnDebounceTextWatcher(lifecycle: Lifecycle, onDebounceAction: (String) -> Unit) {
        searchJob?.cancel()
        searchJob = onDebounceTextChanged()
            .debounce(debouncePeriod)
            .onEach { onDebounceAction(it) }
            .launchIn(lifecycle.coroutineScope)
    }

    fun removeOnDebounceTextWatcher() {
        searchJob?.cancel()
    }

    @ExperimentalCoroutinesApi
    private fun onDebounceTextChanged(): Flow<String> = channelFlow {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                trySend(p0.toString()).isSuccess
            }
        }

        addTextChangedListener(textWatcher)

        awaitClose {
            removeTextChangedListener(textWatcher)
        }
    }
}