package ru.qrfication.QRFication.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main_display.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import ru.dublgis.dgismobile.mapsdk.LonLat
import ru.dublgis.dgismobile.mapsdk.Map
import ru.qrfication.QRFication.R
import ru.qrfication.QRFication.domain.ApiKey
import ru.qrfication.QRFication.domain.LocationBody
import ru.qrfication.QRFication.model.FirebaseService
import ru.qrfication.QRFication.model.NetworkService
import ru.qrfication.QRFication.model.Preferences
import ru.dublgis.dgismobile.mapsdk.MapFragment as DGisMapFragment


class MainDisplayActivity : AppCompatActivity() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var map: Map
    private var location: Location? = null


    companion object {
        const val LOC_PERM = Manifest.permission.ACCESS_FINE_LOCATION
        const val LOCATION_PERMISSION_REQUEST_ID: Int = 777
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_display)
        initListeners()
        initBottomSheet()
        mapInit()
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
            it.rootView.clearFocus()
            slideUpBottomSheet.isEnabled = false
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            qrCodeLayout.visibility = View.VISIBLE
            getQRCode()
        }
        closeQRBtn.setOnClickListener {
            qrCodeLayout.visibility = View.GONE
            slideUpBottomSheet.isEnabled = true
            initBottomSheet() // for working bottom sheet
            // for hide keyboard
        }
        slideUpBottomSheet.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun getQRCode() {
        NetworkService.getBase64String(streetLine.text.toString(), houseLine.text.toString(), {
            val base64Image = (it.base64string).split(",")[1]
            val decodedString = Base64.decode(base64Image, Base64.DEFAULT)
            val decodedByte =
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString!!.size)
            qrCodeImage.setImageBitmap(decodedByte)
            //println(decodedString.toString() + "  shit   ")
        }, {
            Toast.makeText(this, "QR Code Generation Exception", Toast.LENGTH_LONG).show()
        })
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        //bottomSheetBehavior.peekHeight = 120
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }
        })
    }


    private fun mapInit() {
        locationProvider = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment)
                as DGisMapFragment

        val apiKey = ApiKey.DGIS_API_KEY

        val lastLocation: LocationBody = Preferences.getLastLocationPref(this)

        mapFragment.mapReadyCallback = this::onDGisMapReady
        mapFragment.setup(
            apiKey = apiKey,
            center = LonLat(lastLocation.longitude, lastLocation.latitude),
            zoom = 17.0
        )

        val grant = ContextCompat.checkSelfPermission(this, LOC_PERM)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(LOC_PERM),
                LOCATION_PERMISSION_REQUEST_ID
            )
        } else {
            requestLocation()
        }

        mapOf(
            R.id.zoom_in to this::zoomInMap,
            R.id.zoom_out to this::zoomOutMap,
            R.id.location to this::centerMap

        ).forEach {
            val btn = findViewById<ImageButton>(it.key)
            btn.setOnClickListener(it.value)
        }
        requestLocation()
    }

    private fun onDGisMapReady(controller: Map?) {
        if (controller != null) {
            map = controller
        }
        onDGisMapReady()
    }

    protected fun onDGisMapReady() {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_ID -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocation()
                }
            }
        }
    }

    private fun requestLocation() {
        val grant = ContextCompat.checkSelfPermission(this, LOC_PERM)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val request = LocationRequest.create()
        request.interval = 60000
        request.fastestInterval = 20000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val listener = object : LocationCallback() {
            override fun onLocationResult(res: LocationResult?) {
                res?.lastLocation?.let {
                    location = it
                    Preferences.saveLastLocationPref(
                        this@MainDisplayActivity,
                        location!!.longitude,
                        location!!.latitude
                    )
                }
            }
        }
        locationProvider.requestLocationUpdates(request, listener, null)

    }

    private fun centerMap(@Suppress("UNUSED_PARAMETER") view: View?) {
        if (map == null || location == null) {
            return
        }

        map.run {
            location?.let {
                center = LonLat(it.longitude, it.latitude)
                zoom = 17.0
            }
        }
    }

    private fun zoomInMap(@Suppress("UNUSED_PARAMETER") view: View?) {
        map.run {
            zoom = zoom.inc()
        }
    }

    private fun zoomOutMap(@Suppress("UNUSED_PARAMETER") view: View?) {
        map.run {
            zoom = zoom.dec()
        }
    }
}




