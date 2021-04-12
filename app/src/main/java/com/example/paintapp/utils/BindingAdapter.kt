package com.example.paintapp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("setImageResource")
fun setImageResource(imageView: ImageView, id: Int){
    imageView.setImageResource(id)
}
