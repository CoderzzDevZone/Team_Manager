package com.example.android.manager.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User

class saveData{
    val db = FirebaseFirestore.getInstance()
    val chatsCollection = db.collection("chats")
    val tasks = db.collection("tasksssIts")
    val currentUser = FirebaseAuth.getInstance().currentUser

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

    fun addTask(taskData: taskData){
        tasks.document(taskData.time.toString()).set(taskData)
    }
}