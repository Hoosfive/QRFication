package ru.qrfication.QRFication.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main_display.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import ru.qrfication.QRFication.R
import ru.qrfication.QRFication.model.FirebaseService
import ru.qrfication.QRFication.model.NetworkService
import ru.qrfication.QRFication.model.Preferences


class MainDisplayActivity : AppCompatActivity() {


    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_display)
        initListeners()
        initBottomSheet()
    }

    private fun initListeners() {
        signOutBtn.setOnClickListener {
            Preferences.editUIDPref(this, Preferences.DEF_VALUE)
            FirebaseService.signOut()
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        okBtn.setOnClickListener {
            bottomSheetBehavior.isHideable = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            qrCodeLayout.visibility = View.VISIBLE

            NetworkService.getBase64String(streetLine.text.toString(), houseLine.text.toString(), {
                val base64Image = (it.base64string).split(",")[1]
                val decodedString = Base64.decode(base64Image, Base64.DEFAULT)
                val decodedByte =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString!!.size)
                qrCodeImage.setImageBitmap(decodedByte)
                //println(decodedString.toString() + "  shit   ")
            }, {
                Toast.makeText(this, "POSHEL NAHUI EXCEPTION", Toast.LENGTH_LONG).show()
            })
        }
        closeQRBtn.setOnClickListener {
            qrCodeLayout.visibility = View.GONE
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from<ConstraintLayout>(bottomSheetLayout)
        bottomSheetBehavior.peekHeight = 120
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }
        })
    }


}


