package com.cubix.firebaseforumdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cubix.firebaseforumdemo.data.PostData
import com.cubix.firebaseforumdemo.databinding.PostRowBinding
import com.google.firebase.firestore.FirebaseFirestore

class PostsAdapter : RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    var context: Context
    var postsList = mutableListOf<PostData>()
    var postKeys = mutableListOf<String>()

    var currentUid: String

    constructor(context: Context, uid: String) : super() {
        this.context = context
        this.currentUid = uid
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapter.ViewHolder {
        val binding = PostRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostsAdapter.ViewHolder, position: Int) {
        var post = postsList.get(holder.adapterPosition)

        holder.tvAuthor.text = post.author
        holder.tvTitle.text = post.title
        holder.tvBody.text = post.body

        if (currentUid == post.uid) {
            holder.btnDelete.visibility = View.VISIBLE
        } else {
            holder.btnDelete.visibility = View.GONE
        }

        holder.btnDelete.setOnClickListener {
            removePost(holder.adapterPosition)
        }

        if (post.imgUrl.isNotEmpty()){
            holder.ivPhoto.visibility = View.VISIBLE
            Glide.with(context).load(post.imgUrl).into(holder.ivPhoto)
        } else {
            holder.ivPhoto.visibility = View.GONE
        }
    }

    fun addPost(post: PostData, key: String) {
        postsList.add(post)
        postKeys.add(key)
        //notifyDataSetChanged()
        notifyItemInserted(postsList.lastIndex)
    }

    // when I remove the post object
    private fun removePost(index: Int) {
        FirebaseFirestore.getInstance().collection("posts").document(
            postKeys[index]
        ).delete()

        postsList.removeAt(index)
        postKeys.removeAt(index)
        notifyItemRemoved(index)
    }

    // when somebody else removes an object
    fun removePostByKey(key: String) {
        val index = postKeys.indexOf(key)
        if (index != -1) {
            postsList.removeAt(index)
            postKeys.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    inner class ViewHolder(private val binding: PostRowBinding) : RecyclerView.ViewHolder(binding.root){
        var tvAuthor = binding.tvAuthor
        var tvTitle = binding.tvTitle
        var tvBody = binding.tvBody
        var btnDelete = binding.btnDelete
        var ivPhoto = binding.ivPhoto
    }
}