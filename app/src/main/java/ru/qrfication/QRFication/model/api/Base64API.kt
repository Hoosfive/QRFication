package ru.qrfication.QRFication.model.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.qrfication.QRFication.domain.AddressBody
import ru.qrfication.QRFication.model.response.Base64ResponseBody

interface Base64API {

    @POST("/generateQr")
    fun qr(@Body auth: AddressBody): Call<Base64ResponseBody>
}
