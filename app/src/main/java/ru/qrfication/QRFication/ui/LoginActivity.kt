package ru.qrfication.QRFication.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import ru.qrfication.QRFication.R
import ru.qrfication.QRFication.model.FirebaseService
import ru.qrfication.QRFication.model.Preferences


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIsAlreadyLogIn()
        setContentView(R.layout.activity_login)
        passLine.inputType =
            PassBtnController.TEXT_PASS // костыль для нормального отображения скрытого пароля при вводе
        initListeners()
    }

    private fun initListeners() {
        passLayout.endIconImageButton.setOnClickListener {
            PassBtnController.togglePasswordVisibility(passLine, passLayout)
        }
        registrationTV.setOnClickListener {
            intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        loginBtn.setOnClickListener {
            val uid = FirebaseService.auth(
                loginLine.text.toString(),
                passLine.text.toString(),
                this
            )
            println(uid)
            saveData(uid!!)
            intent = Intent(this, MainDisplayActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    private fun checkIsAlreadyLogIn() {
        if (Preferences.getUIDPref(this) != Preferences.DEF_VALUE) {
            intent = Intent(this, MainDisplayActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    private fun saveData(uid: String) {
        Preferences.editUserInfoPrefs(
            this, uid,
            loginLine.text.toString(),
            Preferences.DEF_VALUE,
            Preferences.DEF_VALUE
        )
    }
}
