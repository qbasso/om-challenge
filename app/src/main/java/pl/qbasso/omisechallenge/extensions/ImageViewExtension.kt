package pl.qbasso.omisechallenge.extensions

import android.widget.ImageView
import com.squareup.picasso.Picasso
import pl.qbasso.omisechallenge.R

fun ImageView.loadImage(url: String) {
    Picasso.with(this.context).load(url).placeholder(R.mipmap.ic_launcher).into(this)
}