package ru.qrfication.QRFication.model.response

import com.google.gson.annotations.SerializedName

data class Base64ResponseBody(
    @SerializedName("content") val base64string: String
)