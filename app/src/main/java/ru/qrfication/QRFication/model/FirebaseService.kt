package ru.qrfication.QRFication.model

import android.content.Context
import android.os.Handler
import android.telecom.Call
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.security.auth.callback.CallbackHandler


class FirebaseService {
    companion object {
        private var firebaseUser: FirebaseUser? = null
        private lateinit var result: String

        fun auth(email: String, password: String, context: Context) {

            /*while (signIn() == " ")
                Thread.sleep(100)
                println("shit")
            return signIn()*/
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUser = task.result?.user
                        if (firebaseUser?.uid != null) {
                            println(firebaseUser!!.uid)
                            Toast.makeText(
                                context,
                                "You are successfully authenticated!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
        }



        fun reg(email: String, password: String, context: Context): String? {
            return FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUser = task.result?.user
                        if (firebaseUser != null) {
                            Toast.makeText(
                                context,
                                "Your registration is successfully!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }.result?.user?.uid
        }

        fun signOut() {
            FirebaseAuth.getInstance().signOut()
        }
    }
}