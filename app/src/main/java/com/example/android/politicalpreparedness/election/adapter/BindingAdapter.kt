package com.example.android.politicalpreparedness.utils

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?) {
    data?.let {
        val adapter = recyclerView.adapter as ElectionListAdapter
        adapter.submitList(data)
    }
}


@BindingAdapter("toggleText")
fun bindTextViewToggleText(buttonView: Button, isElectionSaved: Boolean) {
    val context = buttonView.context
    if (isElectionSaved) {
        buttonView.text = context.getString(R.string.unfollow_election)
    } else {
        buttonView.text = context.getString(R.string.follow_election)
    }
}

@BindingAdapter("apiStatus")
fun bindAPILoadingStatus(statusImage: ImageView, status: Constants.ApiStatus?) {
    when (status) {
        Constants.ApiStatus.LOADING -> {
            statusImage.visibility = View.VISIBLE
            statusImage.setImageResource(R.drawable.loading_animation)
        }
        Constants.ApiStatus.ERROR -> {
            statusImage.visibility = View.VISIBLE
            statusImage.setImageResource(R.drawable.ic_connection_error)
        }
        Constants.ApiStatus.DONE -> {
            statusImage.visibility = View.GONE
        }
    }
}

