package com.example.android.politicalpreparedness.utils

import android.widget.Button
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

