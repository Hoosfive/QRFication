package ru.qrfication.QRFication.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import ru.qrfication.QRFication.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        passLineReg.inputType =
            PassBtnController.TEXT_PASS // костыль для нормального отображения скрытого пароля при вводе
        initListeners()
    }

    private fun initListeners() {
        passLayoutReg.endIconImageButton.setOnClickListener {
            PassBtnController.togglePasswordVisibility(passLineReg, passLayoutReg)
        }
        registrationTV.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        /*loginBtn.setOnClickListener {
            if (validateFields()) {
                NetworkService.auth(
                    loginLine.text.toString(),
                    passLine.text.toString(), {
                        saveData(it)
                        startActivity(Intent(this, MainActivity::class.java))
                        this.finish()
                    }, {
                        showError()
                    })
                setLoading(true)
            }
        }*/
    }
}
