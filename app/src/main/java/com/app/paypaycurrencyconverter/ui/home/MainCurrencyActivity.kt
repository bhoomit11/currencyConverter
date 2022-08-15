package com.app.paypaycurrencyconverter.ui.home

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.paypaycurrencyconverter.databinding.ActivityMainBinding
import com.app.paypaycurrencyconverter.models.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainCurrencyActivity : AppCompatActivity() {

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
        mainCurrencyViewModel.response.observe(this) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    binding.loadingView.success()
                    response.data?.let {
                        currencyArr.addAll(it.map { "${it.currencyCode} - ${it.currency}" }.sortedBy { it })
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
    }

    private fun initUi() {
        binding.loadingView.parentView = binding.clParent

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            currencyArr
        )
        binding.actSelectCurrency.setAdapter(adapter)
    }
}