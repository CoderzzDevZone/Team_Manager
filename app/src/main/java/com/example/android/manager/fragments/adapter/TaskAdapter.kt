package com.example.android.manager.fragments.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codiyapa.socialnetwork.Utils
import com.example.android.manager.MainActivity
import com.example.android.manager.R
import com.example.android.manager.database.UserModel
import com.example.android.manager.database.saveData
import com.example.android.manager.database.taskData
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.task_items.view.*

class TaskAdapter(options: FirestoreRecyclerOptions<taskData>, private var mAuth: FirebaseAuth, val listener: MainActivity) : FirestoreRecyclerAdapter<taskData, TaskAdapter.TaskViewHolder>(options) {
    private val currentUser: FirebaseUser = mAuth.currentUser!!
//    var db = FirebaseFirestore.getInstance()
//    var tasks = db.collection("tasksss")
    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val senderImage: ImageView = view.senderImage
        val senderName: TextView = view.senderName
        val taskText: TextView = view.taskText
        val createdAt: TextView = view.time
        val ifAssigned: TextView = view.ifAssigned
        val assign: Button = view.assign
        val assignedImage: ImageView = view.assignedImage
        val assignedName: TextView = view.assignedName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_items, parent, false))
    }

    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int, model: taskData) {
        holder.senderName.text = model.postedBy
        holder.createdAt.text = Utils.getTimeAgo(model.time)
        holder.taskText.text = model.taskText
        Glide.with(holder.senderImage.context).load(model.postedByImage).circleCrop().into(holder.senderImage)

        if (model.assignedTo == ""){
            holder.assign.visibility  = View.VISIBLE
        }else{
            holder.assign.visibility = View.GONE
            holder.ifAssigned.visibility = View.VISIBLE
            holder.assignedName.visibility = View.VISIBLE
            holder.assignedImage.visibility = View.VISIBLE
            holder.assignedName.text = model.assignedTo
            Glide.with(holder.assignedImage.context).load(model.assignedImage).circleCrop().into(holder.assignedImage)
        }
        holder.assign.setOnClickListener {
            holder.assign.visibility = View.GONE
            val user = UserModel(currentUser.displayName!!, currentUser.email!!, currentUser.photoUrl!!.toString(), null)
            val taskData = taskData(model.taskText, model.time, model.postedBy, model.postedByImage, currentUser.displayName!!, currentUser.photoUrl!!.toString(), currentUser.email!!, System.currentTimeMillis())
            saveData().tasks.document(taskData.time.toString()).set(taskData)
        }

    }
}