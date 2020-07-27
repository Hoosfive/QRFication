package ru.qrfication.QRFication.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class FirebaseService {
    companion object {
        private var firebaseUser: FirebaseUser? = null
        private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

        fun auth(email: String, password: String) {
            GlobalScope.async {
                firebaseAuth
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            firebaseUser = task.result?.user
                        }
                    }.result?.user?.uid
            }
        }

        fun reg(email: String, password: String) {
            GlobalScope.async {
                firebaseAuth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            firebaseUser = task.result?.user
                        }
                    }.result?.user?.uid
            }
        }

        fun signOut() {
            FirebaseAuth.getInstance().signOut()
        }
    }
}