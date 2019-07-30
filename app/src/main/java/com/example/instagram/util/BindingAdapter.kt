package com.example.instagram.util

import android.net.Uri
import android.widget.ImageView
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

@BindingAdapter("imageUrl2")
fun imageUrl2(view: ImageView, uri: String){
    Glide.with(view)
        .load(uri)
        .placeholder(R.drawable.placeholder_24dp)
        .into(view)
}

@BindingAdapter("imageUrl3")
fun imageUrl3(view: CircleImageView, uri: String){
    Glide.with(view)
        .load(uri)
        .placeholder(R.drawable.placeholder_24dp)
        .into(view)
}

