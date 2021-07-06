package com.example.android.politicalpreparedness.election


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    val id: Long,
    val division: Division,
    private val dataSource: ElectionDatabase
) : ViewModel() {
    private val repository = ElectionRepository(dataSource)
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String>
        get() = _errorMsg

    //: Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val _stateInfo = MutableLiveData<State>()
    val stateInfo: LiveData<State>
        get() = _stateInfo

    init {
        getVoterInfo()
        getSingleElectionFromDB()
    }

    //: Add var and methods to populate voter info
    private fun getVoterInfo() {
        viewModelScope.launch {
            repository.getVoterInfo(division.state, id)
                .catch {
                    _errorMsg.value = it.message
                }
                .collect { voterInfo ->
                    _voterInfo.value = voterInfo
                    _stateInfo.value = voterInfo.state?.get(0)
                }
        }
    }


    //TODO: Add var and methods to support loading URLs

    //: Add var and methods to save and remove elections to local database
    //: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
    private val _isElectionSaved = MutableLiveData<Boolean>()
    val isElectionSaved: LiveData<Boolean>
        get() = _isElectionSaved

    private fun getSingleElectionFromDB() {
        viewModelScope.launch {
            repository.getSingleElectionFromDB(id)
                .catch {
                    _errorMsg.value = it.message
                }
                .collect { election ->
                    _isElectionSaved.value = election != null
                }
        }
    }

    fun toggleButton() {
        if (_isElectionSaved.value == false) {
            saveElection()
        } else {
            deleteSingleElection()
        }
    }

    private fun saveElection() {
        val election = voterInfo.value!!.election
        viewModelScope.launch {
            repository.insertElection(election)
            _isElectionSaved.value = true
        }
    }

    private fun deleteSingleElection() {
        viewModelScope.launch {
            repository.deleteElection(id)
            _isElectionSaved.value = false
        }
    }
    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}