package com.example.android.manager.database

data class chatData(val text : String? = null,
                    val imageUrl: String? = null,
                    val time : Long = 0L,
                    val user : UserModel? = null)