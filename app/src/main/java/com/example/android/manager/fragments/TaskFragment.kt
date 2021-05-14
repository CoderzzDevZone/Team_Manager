package com.example.android.manager.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.manager.MainActivity
import com.example.android.manager.R
import com.example.android.manager.database.UserModel
import com.example.android.manager.database.saveData
import com.example.android.manager.database.taskData
import com.example.android.manager.fragments.adapter.TaskAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_task.view.*


class TaskFragment : Fragment() {
    val mAuth = FirebaseAuth.getInstance()
    val users = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    private lateinit var data : saveData
    lateinit var adapter : TaskAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTaskAdapter(view)
        view.sendImage.setOnClickListener{
            if (TextUtils.isEmpty(view.taskField.text.toString())){
                view.taskField.error = "Input field is Empty"
            }else{
                val user = mAuth.currentUser
                val taskData = taskData(view.taskField.text.toString(), System.currentTimeMillis(), user!!.displayName!!, user.photoUrl!!.toString(), "", "", "", 0L)
                saveData().addTask(taskData)
                view.taskField.text?.clear()
            }
        }
    }

    private  fun setTaskAdapter(view : View){
        data = saveData()
        val tasks = data.tasks
        val query = tasks.orderBy("time", Query.Direction.ASCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<taskData>().setQuery(
                query,
                taskData::class.java
        ).build()
        adapter = TaskAdapter(recyclerViewOption, mAuth, activity as MainActivity)
        view.recyclerViewTask.adapter = adapter
        view.recyclerViewTask.layoutManager = LinearLayoutManager(activity)
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