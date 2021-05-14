package com.example.android.manager.fragments.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codiyapa.socialnetwork.Utils
import com.example.android.manager.MainActivity
import com.example.android.manager.R
import com.example.android.manager.database.taskData
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.mycommits_items.view.*
import kotlinx.android.synthetic.main.task_items.view.*
import kotlinx.android.synthetic.main.task_items.view.senderImage
import kotlinx.android.synthetic.main.task_items.view.senderName
import kotlinx.android.synthetic.main.task_items.view.taskText
import kotlinx.android.synthetic.main.task_items.view.time

class Commits(recyclerViewOption: FirestoreRecyclerOptions<taskData>, mAuth: FirebaseAuth, mainActivity: MainActivity) : FirestoreRecyclerAdapter<taskData, Commits.CommitViewHolder>(recyclerViewOption){

    var currentUser = mAuth.currentUser
    class CommitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val senderImage: ImageView = view.senderImage
        val senderName: TextView = view.senderName
        val taskText: TextView = view.taskText
        val createdAt: TextView = view.time
        val assignedAt : TextView = view.assignedAt
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewHolder {
        return CommitViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.mycommits_items, parent, false))
    }

    override fun onBindViewHolder(holder: CommitViewHolder, position: Int, model: taskData) {
        if (model.assignedEmail == currentUser!!.email){
            holder.senderName.text = model.postedBy
            Glide.with(holder.senderImage.context).load(model.postedByImage).circleCrop().into(holder.senderImage)
            holder.taskText.text = model.taskText
            holder.createdAt.text = "Posted At: " + Utils.getTimeAgo(model.time)
            holder.assignedAt.text = "Assigned At: " + Utils.getTimeAgo(model.taskAssignedAt)
        }else{
            holder.itemView.visibility = View.GONE
        }
    }
}