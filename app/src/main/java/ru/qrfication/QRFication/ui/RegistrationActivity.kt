package ru.qrfication.QRFication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.passLineReg
import kotlinx.android.synthetic.main.activity_registration.*
import ru.qrfication.QRFication.R

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        // костыль для нормального отображения скрытого пароля при вводе ↓
        passLineReg.inputType = PassBtnController.TEXT_PASS
        repeatPassLine.inputType = PassBtnController.TEXT_PASS
        initListeners()
    }

    private fun initListeners() {
        passLayoutReg.endIconImageButton.setOnClickListener {
            PassBtnController.togglePasswordVisibility(passLineReg, passLayoutReg)
        }
        repeatPassLayout.endIconImageButton.setOnClickListener {
            PassBtnController.togglePasswordVisibility(repeatPassLine, repeatPassLayout)
        }
    }
}
