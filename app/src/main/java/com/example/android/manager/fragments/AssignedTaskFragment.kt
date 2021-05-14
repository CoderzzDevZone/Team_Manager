package com.example.android.manager.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.android.manager.MainActivity
import com.example.android.manager.R
import com.example.android.manager.SignInActivity
import com.example.android.manager.database.saveData
import com.example.android.manager.database.taskData
import com.example.android.manager.fragments.adapter.Commits
import com.example.android.manager.fragments.adapter.TaskAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_assignedtask.view.*


class AssignedTaskFragment : Fragment() {
    val mAuth = FirebaseAuth.getInstance()
    val user = mAuth.currentUser
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var adapter : Commits
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assignedtask, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }!!
        view.userName.text = user!!.displayName
        Glide.with(view.userImage.context).load(user.photoUrl).circleCrop().into(view.userImage)
        view.userEmail.text = user.email
        setAdapter(view)
        view.logOut.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }
    }

    private fun setAdapter(view: View) {
        val tasks = saveData().tasks
        val query = tasks.orderBy("time", Query.Direction.DESCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<taskData>().setQuery(
                query,
                taskData::class.java
        ).build()
        adapter = Commits(recyclerViewOption, mAuth, activity as MainActivity)
        view.myCommits.adapter = adapter
        view.myCommits.layoutManager = LinearLayoutManager(activity)
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