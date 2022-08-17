package com.app.paypaycurrencyconverter.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.app.paypaycurrencyconverter.databinding.ActivityMainBinding
import com.app.paypaycurrencyconverter.models.CurrenciesResponse
import com.app.paypaycurrencyconverter.models.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainCurrencyActivity : AppCompatActivity() {

    private var currencies: List<CurrenciesResponse>? = null
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var binding: ActivityMainBinding

    private val mainCurrencyViewModel: MainCurrencyViewModel by viewModels()
    private var currencyArr: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
        addObserver()
    }

    private fun addObserver() {
        mainCurrencyViewModel.currencyListResponse.observe(this) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    binding.loadingView.success()
                    currencies = response.data
                    currencies?.let {
                        currencyArr.addAll(it.map { "${it.currencyCode} - ${it.currency}" }
                            .sortedBy { it })
                        adapter.addAll(currencyArr)
                        adapter.notifyDataSetChanged()
                    }
                }
                Status.LOADING -> {
                    binding.loadingView.progress()
                }
                Status.ERROR -> {
                    binding.loadingView.error(response.message)
                }
            }
        }

        mainCurrencyViewModel.currencyConvertResponse.observe(this) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    binding.loadingView.success()
                    Log.e("Convert Response", response.data.toString())
                }
                Status.LOADING -> {
                    binding.loadingView.progress()
                }
                Status.ERROR -> {
                    binding.loadingView.error(response.message)
                }
            }
        }
    }

    private fun initUi() {
        binding.loadingView.parentView = binding.clParent

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            currencyArr
        )
        binding.actSelectCurrency.setAdapter(adapter)

        binding.actSelectCurrency.setOnItemClickListener { adapterView, view, position, id ->
            binding.actSelectCurrency.tag = position
            callConvertLogic()
        }

        binding.etCurrencyValue.addTextChangedListener {
            if (binding.actSelectCurrency.text.toString().isNotEmpty()) {
                callConvertLogic()
            }
        }

        binding.etCurrencyValue.setOnDebounceTextWatcher(lifecycle) {
            callConvertLogic()
        }

    }

    private fun callConvertLogic() {
        if (binding.actSelectCurrency.text.toString()
                .isNotEmpty() && binding.etCurrencyValue.text.toString().isNotEmpty()
        ) {
            val inputCurrency = binding.actSelectCurrency.tag?.toString()?.toInt()
                ?.let { it1 -> currencies?.get(it1) }
            mainCurrencyViewModel.convert(
                value = binding.etCurrencyValue.text.toString().toDouble(),
                from = inputCurrency?.currencyCode.toString(),
                to = currencies?.random()?.currencyCode.toString()
            )

        }

    }
}