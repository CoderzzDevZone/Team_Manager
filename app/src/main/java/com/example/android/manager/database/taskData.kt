package com.example.android.manager.database

import com.google.firebase.firestore.auth.User

data class taskData(val taskText : String = "",
                    val time : Long = 0L,
                    val postedBy : String = "",
                    val postedByImage: String = "",
                    val assignedTo : String = "",
                    val assignedImage : String = "",
                    val assignedEmail : String = "",
                    val taskAssignedAt : Long = 0L)