package com.app.paypaycurrencyconverter.utils

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.app.paypaycurrencyconverter.R
import com.app.paypaycurrencyconverter.models.CurrenciesResponse
import com.google.gson.GsonBuilder

/**
 * This class is used for storing and retrieving shared preference values.
 */
class AppPreference(context: Context) {

    private val keyGenParameterSpec = KeyGenParameterSpec.Builder(
        context.getString(R.string.app_name),
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .setKeySize(256)
        .build()

    private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    private val pref = EncryptedSharedPreferences.create(
        context.getString(R.string.app_name),
        mainKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )


    /**
     * To Store/Fetch local Currency Data
     */

    var currencyData: String
        get() = pref.getString("CurrenciesResponse", "") ?: ""
        set(value) = pref.edit().putString("CurrenciesResponse", value).apply()

}