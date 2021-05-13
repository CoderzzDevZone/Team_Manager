package com.example.android.manager.fragments

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.manager.MainActivity
import com.example.android.manager.R
import com.example.android.manager.database.UserModel
import com.example.android.manager.database.chatData
import com.example.android.manager.database.saveData
import com.example.android.manager.fragments.adapter.ChatAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_discussion.*
import kotlinx.android.synthetic.main.fragment_discussion.view.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.util.*


@Suppress("DEPRECATION")
class DiscussionFragment : Fragment() {

    var PICK_IMAGE_REQUEST = 22
    private lateinit var filepath : Uri
    var imageSelected = 0
    var storage = FirebaseStorage.getInstance()
    private lateinit var data : saveData
    private lateinit var adapter : ChatAdapter

    val mAuth = FirebaseAuth.getInstance()
    val user = mAuth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discussion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.selectImage.setOnClickListener {
            imageSelector()
        }
        view.sendImage.setOnClickListener{
            if (TextUtils.isEmpty(view.chatField.text.toString())){
                if (imageSelected == 0){
                Toast.makeText(activity, "All fields are empty", Toast.LENGTH_SHORT).show()
                }else{
                    ImageUpload(null, 1, view)
                }
            }else{
                if (imageSelected == 0){
                    ImageUpload(view.chatField.text.toString(), 0, view)
                }else{
                    ImageUpload(view.chatField.text.toString(), 1, view)
                }
            }
        }

        setAdapter(view)
    }
    private fun imageSelector() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."),
                PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null){
            filepath = data.data!!
            imageSelected = 1
        }
    }
    private fun ImageUpload(text : String?, int: Int, view : View) {
        val userModel = UserModel(user!!.displayName!!, user.email!!.toString(), user.photoUrl!!.toString(), null)
        if (int != 0) {
            val progressDialog = ProgressDialog(activity)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            val storageRef = storage.reference
            val name = user.email!!.toString() + UUID.randomUUID().toString()
            val ref = storageRef.child("images/$name")
            val uploadTask = ref.putFile(filepath)
                    .addOnSuccessListener {
                        Toast.makeText(activity, "Image Uploaded!!", Toast.LENGTH_SHORT).show()
                        storageRef.child("images/$name").downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            val chat = chatData(text, imageUrl, System.currentTimeMillis(), userModel)
                            imageSelected = 0
                            saveData().addChats(chat)
                            progressDialog.dismiss()
                            Toast.makeText(activity, "Uploaded", Toast.LENGTH_SHORT).show()
                            view.chatField.text?.clear()
                        }.addOnFailureListener {
                            Toast.makeText(activity, "${it.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(activity, "Failed " + e.message, Toast.LENGTH_SHORT).show()
                    }
        }else{
            val chat = chatData(text, null, System.currentTimeMillis(), userModel)
            saveData().addChats(chat)
            view.chatField.text?.clear()
        }
    }
    private fun setAdapter(view : View) {
        data = saveData()
        val chats = data.chatsCollection
        val query = chats.orderBy("time", Query.Direction.ASCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<chatData>().setQuery(
                query,
                chatData::class.java
        ).build()
        adapter = ChatAdapter(recyclerViewOption, mAuth, activity as MainActivity)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }
    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}