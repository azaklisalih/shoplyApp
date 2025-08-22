package com.example.cartapp.presentation.common.binding

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController

@BindingAdapter("appBarTitle")
fun setAppBarTitle(textView: TextView, title: String?) {
    title?.let {
        textView.text = it
    }
}

@BindingAdapter("showBackButton")
fun setBackButtonVisibility(imageView: ImageView, show: Boolean) {
    imageView.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("onBackClick")
fun setBackButtonClickListener(imageView: ImageView, navController: NavController?) {
    navController?.let {
        imageView.setOnClickListener {
            navController.popBackStack()
        }
    }
}

@BindingAdapter("showCustomContent")
fun setCustomContentVisibility(linearLayout: LinearLayout, show: Boolean) {
    linearLayout.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("rightActionIcon")
fun setRightActionIcon(imageView: ImageView, iconRes: Int?) {
    iconRes?.let {
        imageView.setImageResource(it)
        imageView.visibility = View.VISIBLE
    } ?: run {
        imageView.visibility = View.GONE
    }
} 