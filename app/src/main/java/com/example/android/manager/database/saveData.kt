package com.example.android.manager.database

import com.google.firebase.firestore.FirebaseFirestore

class saveData{
    val db = FirebaseFirestore.getInstance()
    val chatsCollection = db.collection("chats")

    fun addUser(userModel: UserModel?){
        val userCollection = db.collection("user")
        if (userModel != null) {
            userCollection.document(userModel.email).set(userModel)
        }
    }

    fun addChats(chatData: chatData){
        val chats = db.collection("chats")
        chats.document(chatData.time.toString()).set(chatData)
    }
}