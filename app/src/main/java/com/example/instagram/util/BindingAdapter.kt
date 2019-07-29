package com.example.instagram.util

import android.net.Uri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.instagram.R
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("imageUrl")
fun imageUrl(view: CircleImageView, uri: Uri){
    Glide.with(view)
        .load(uri)
        .placeholder(R.drawable.placeholder_24dp)
        .into(view)
}