package ru.qrfication.QRFication.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
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
            setLoading(true)
            val uid = runBlocking {
                GlobalScope.async {
                    FirebaseService.auth(
                        loginLine.text.toString(),
                        passLine.text.toString()
                    )
                }.await().toString()
            }
            println(uid)
            saveData(uid)
            setLoading(false)
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

    private fun setLoading(isLoading: Boolean) {
        loginBtn.isEnabled = !isLoading
        if (isLoading) {
            loginBtn.text = ""
            progressBar.visibility = View.VISIBLE
        } else {
            loginBtn.text = resources.getString(R.string.loginBtn)
            progressBar.visibility = View.INVISIBLE
        }
    }
}
