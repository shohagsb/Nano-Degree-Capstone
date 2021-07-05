package com.example.android.politicalpreparedness.election

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository

//: Construct ViewModel and provide election datasource
class ElectionsViewModel(val database: ElectionDatabase): ViewModel() {
    private val repository = ElectionRepository(database)
    //TODO: Create live data val for upcoming elections
    val upcomingElections = MutableLiveData<Election>()

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

}