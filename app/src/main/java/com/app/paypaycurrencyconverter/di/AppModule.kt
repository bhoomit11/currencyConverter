package com.app.paypaycurrencyconverter.di

import android.content.Context
import com.app.paypaycurrencyconverter.BuildConfig
import com.app.paypaycurrencyconverter.utils.AppPreference
import com.app.paypaycurrencyconverter.webservice.WebService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  fun provideOkHttpClient() = if (BuildConfig.DEBUG){
    val loggingInterceptor =HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
      .addInterceptor(loggingInterceptor)
      .build()
  }else{
    OkHttpClient
      .Builder()
      .build()
  }

  @Provides
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(WebService.BASE_URL)
    .client(okHttpClient)
    .build()

  @Provides
  fun provideApiService(retrofit: Retrofit): WebService = retrofit.create(WebService::class.java)

  @Provides
  fun provideGson(): Gson = Gson()

  @Provides
  fun providePref(@ApplicationContext appContext: Context): AppPreference = AppPreference(appContext)

}