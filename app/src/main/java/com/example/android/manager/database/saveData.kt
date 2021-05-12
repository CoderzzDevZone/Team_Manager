package com.example.android.manager.database

import com.google.firebase.firestore.FirebaseFirestore

class saveData{
    val db = FirebaseFirestore.getInstance()

    fun addUser(userModel: UserModel?){
        val userCollection = db.collection("user")
        if (userModel != null) {
            userCollection.document(userModel.email).set(userModel)
        }
    }
}