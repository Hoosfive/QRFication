package ru.qrfication.QRFication.model.response

import com.google.gson.annotations.SerializedName

data class UserInfoResponseBody(
    @SerializedName("uid") var uid: String,
    @SerializedName("email") var email: String,
    @SerializedName("firstName") var firstName: String,
    @SerializedName("lastName") var lastName: String
)