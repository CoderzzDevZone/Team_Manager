package com.example.android.manager.fragments.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.codiyapa.socialnetwork.Utils
import com.example.android.manager.MainActivity
import com.example.android.manager.R
import com.example.android.manager.database.chatData
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.chat_item.view.*

class ChatAdapter(options: FirestoreRecyclerOptions<chatData>, private var mAuth: FirebaseAuth, val listener: MainActivity) :
    FirestoreRecyclerAdapter<chatData, ChatAdapter.ChatViewHolder>(options) {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val senderImage: ImageView = view.senderImager
        val senderName: TextView = view.sendername
        val time: TextView = view.time
        val chatText: TextView = view.chatText
        val chattImage : ImageView = view.chatImage
        val progressBar : ProgressBar = view.progressBar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false))
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int, model: chatData) {
        if (model.text != null){
            holder.chatText.text = model.text
        }else{
            holder.chatText.visibility = View.VISIBLE
        }
        holder.senderName.text = model.user!!.name
        Glide.with(holder.senderImage.context).load(model.user.imageUrl).circleCrop().into(holder.senderImage)
        if (model.imageUrl != null){
            holder.progressBar.visibility = View.VISIBLE
            Glide.with(holder.chattImage.context).load(model.imageUrl).listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    holder.progressBar.visibility = View.GONE
                    holder.chattImage.setImageResource(R.drawable.error)
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    holder.progressBar.visibility = View.GONE
                    return false
                }

            }).into(holder.chattImage)
        }else{
            holder.chattImage.visibility = View.GONE
        }
        holder.time.text = Utils.getTimeAgo(model.time)

    }


}