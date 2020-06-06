package ru.qrfication.QRFication.domain

import com.google.gson.annotations.SerializedName

data class AddressBody(
    @SerializedName("address") val street: String,
    @SerializedName("number") val houseNumber: String
)
