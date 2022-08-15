package com.app.paypaycurrencyconverter.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import com.app.paypaycurrencyconverter.R
import com.app.paypaycurrencyconverter.databinding.InflateLoadingViewBinding


internal class LoadingView : LinearLayout {


    var binding: InflateLoadingViewBinding? = null
    var parentView: View? = null

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        inflate(context, R.layout.inflate_loading_view, this)
        binding = InflateLoadingViewBinding.bind(getChildAt(0))
    }

    fun success() {
        parentView?.visibility = View.VISIBLE
        binding?.tvLoadingTitle?.visibility = View.GONE
        binding?.tvLoadingMessage?.visibility = View.GONE
        binding?.pbLoading?.visibility = View.GONE
    }

    fun progress(message: String? = null, title: String? = null) {
        parentView?.visibility = View.GONE
        if (!message.isNullOrBlank()) {
            binding?.tvLoadingMessage?.visibility = View.VISIBLE
            binding?.tvLoadingMessage?.setText(message)
        } else {
            binding?.tvLoadingMessage?.visibility = View.GONE

        }

        if (!title.isNullOrBlank()) {
            binding?.tvLoadingTitle?.visibility = View.VISIBLE
            binding?.tvLoadingTitle?.setText(title)
        } else {
            binding?.tvLoadingTitle?.visibility = View.GONE
        }


        binding?.pbLoading?.visibility = View.VISIBLE
    }

    fun error(message: String?, drawableRes: Int = 0, title: String? = null) {
        parentView?.visibility = View.GONE
        binding?.pbLoading?.visibility = View.GONE
        message?.let {
            binding?.tvLoadingMessage?.visibility = View.VISIBLE
            binding?.tvLoadingMessage?.setText(it)
        }

        if (!title.isNullOrBlank()) {
            binding?.tvLoadingTitle?.visibility = View.VISIBLE
            binding?.tvLoadingTitle?.setText(title)
        } else {
            binding?.tvLoadingTitle?.visibility = View.GONE
        }

    }
}