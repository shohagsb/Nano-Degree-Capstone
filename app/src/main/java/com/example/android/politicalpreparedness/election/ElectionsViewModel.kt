package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.utils.Constants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//: Construct ViewModel and provide election datasource
class ElectionsViewModel(val database: ElectionDatabase) : ViewModel() {
    private val repository = ElectionRepository(database)

    //: Create live data val for upcoming elections
    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    //: Create live data val for saved elections
    private val _savedElections = MutableLiveData<List<Election>?>()
    val savedElections: LiveData<List<Election>?>
        get() = _savedElections

    private val _status = MutableLiveData<Constants.ApiStatus>()
    val status: LiveData<Constants.ApiStatus>
        get() = _status

    init {
        getUpcomingElectionsFromNetwork()
    }

    //: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    private fun getUpcomingElectionsFromNetwork() {
        viewModelScope.launch {
            _status.value = Constants.ApiStatus.LOADING
            repository.getUpcomingElections
                .catch {
                    _status.value = Constants.ApiStatus.ERROR
                }
                .collect { elections ->
                    _status.value = Constants.ApiStatus.DONE
                    _upcomingElections.value = elections.elections
                }
        }
    }

    fun getAllSavedElectionsFromDB() {
        viewModelScope.launch {
            repository.getAllSavedElectionsFromDB()
                .catch {
                }
                .collect { savedElections ->
                    _savedElections.value = savedElections
                }
        }
    }

    //: Create functions to navigate to saved or upcoming election voter info
    private val _navigateToVoterInfoFragment = MutableLiveData<Election?>()
    val navigateToVoterInfoFragment: MutableLiveData<Election?>
        get() = _navigateToVoterInfoFragment

    fun onElectionClicked(election: Election) {
        _navigateToVoterInfoFragment.value = election
    }

    fun onNavigationComplete() {
        _navigateToVoterInfoFragment.value = null
    }

}