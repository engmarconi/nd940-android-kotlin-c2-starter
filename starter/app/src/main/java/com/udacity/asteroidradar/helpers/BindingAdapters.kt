package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.udacity.asteroidradar.api.NasaApiStatus
import com.udacity.asteroidradar.screens.main.AsteroidRecycleViewAdapter
import com.udacity.asteroidradar.models.Asteroid

@BindingAdapter("listData")
fun bindListAsteroidsData(recyclerView: RecyclerView, list: List<Asteroid>?) {
    list?.let {
        val adapter = recyclerView.adapter as AsteroidRecycleViewAdapter
        adapter.submitList(list)
    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("imgSrcUrl")
fun imageSrcUrl(imageView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val uri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(uri)
            .apply(RequestOptions().placeholder(R.drawable.placeholder_picture_of_day)
                .error(R.drawable.abc_vector_test))
            .into(imageView)
    }
}
@BindingAdapter("nasaStatus")
fun bindNasaApiStatus(progressBar: ProgressBar, status: NasaApiStatus?){
    status?.let {
        when(status){
            NasaApiStatus.LOADING -> progressBar.visibility = View.VISIBLE
            else -> progressBar.visibility = View.GONE
        }
    }
}