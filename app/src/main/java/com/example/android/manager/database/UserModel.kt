package com.example.android.manager.database

import android.provider.ContactsContract
import com.google.firebase.firestore.FirebaseFirestore

data class UserModel (val name: String = "",
                      val email: String = "",
                      val imageUrl : String = "",
                      val phoneNo : String? = null)

