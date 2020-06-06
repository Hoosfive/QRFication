package ru.qrfication.QRFication.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.qrfication.QRFication.domain.AddressBody
import ru.qrfication.QRFication.model.api.Base64API
import ru.qrfication.QRFication.model.response.Base64ResponseBody


class NetworkService private constructor() {
    companion object {
        private const val BASE_URL = "https://us-central1-qrfication.cloudfunctions.net"

        private val interceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


        private val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()

        private val MemeApi = retrofit.create(Base64API::class.java)

        fun getBase64String(
            address: String,
            houseNumber: String,
            onSuccess: (Base64ResponseBody) -> Unit,
            onError: (Throwable) -> Unit = { }
        ) {
            MemeApi
                .qr(AddressBody(address, houseNumber))
                .enqueue(
                    RetrofitCallback(
                        { data -> onSuccess(data) },
                        { error -> onError(error) }
                    )
                )
        }
    }
}