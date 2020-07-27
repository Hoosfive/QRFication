package ru.qrfication.QRFication.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import ru.qrfication.QRFication.R
import ru.qrfication.QRFication.model.FirebaseService
import ru.qrfication.QRFication.model.Preferences

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        initListeners()
        // костыль для нормального отображения скрытого пароля при вводе ↓
        passLineReg.inputType = PassBtnController.TEXT_PASS
        repeatPassLine.inputType = PassBtnController.TEXT_PASS
    }

    private fun initListeners() {
        passLayoutReg.endIconImageButton.setOnClickListener {
            PassBtnController.togglePasswordVisibility(passLineReg, passLayoutReg)
        }
        repeatPassLayout.endIconImageButton.setOnClickListener {
            PassBtnController.togglePasswordVisibility(repeatPassLine, repeatPassLayout)
        }
        signInBtn.setOnClickListener {
            val uid = runBlocking {
                GlobalScope.async {
                    setLoading(true)
                    FirebaseService.reg(
                        emailLineReg.text.toString(),
                        passLineReg.text.toString()
                    )
                }.await().toString()
            }
            saveData(uid)
            setLoading(false)
            intent = Intent(this, MainDisplayActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    private fun saveData(uid: String) {
        Preferences.editUserInfoPrefs(
            this, uid,
            emailLineReg.text.toString(),
            lastNameLine.text.toString(),
            firstNameLine.text.toString()
        )
    }

    private fun setLoading(isLoading: Boolean) {
        signInBtn.isEnabled = !isLoading
        if (isLoading) {
            signInBtn.text = ""
            progressBarReg.visibility = View.VISIBLE
        } else {
            signInBtn.text = resources.getString(R.string.signInBtn)
            progressBarReg.visibility = View.INVISIBLE
        }
    }
}
