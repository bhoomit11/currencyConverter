package com.app.paypaycurrencyconverter.models
data class ResponseModel<out T>(
    val status: Status,
    val data: T?,
    val message:String?
){
    companion object{

        fun <T> success(data:T?): ResponseModel<T>{
            return ResponseModel(Status.SUCCESS, data, null)
        }

        fun <T> error(msg:String, data:T?): ResponseModel<T>{
            return ResponseModel(Status.ERROR, data, msg)
        }

        fun <T> loading(data:T?): ResponseModel<T>{
            return ResponseModel(Status.LOADING, data, null)
        }

    }
}