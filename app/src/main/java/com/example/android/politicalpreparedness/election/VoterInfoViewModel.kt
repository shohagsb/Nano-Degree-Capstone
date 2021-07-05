package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
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

    //: Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    init {
        getVoterInfo()
    }

    //TODO: Add var and methods to populate voter info
    private fun getVoterInfo() {
        viewModelScope.launch {
            repository.getVoterInfo(id, division.state)
                .catch {
                    Log.d("ElectionRepositoryTAG", "getVoterInfo: id $id ${it.message}")
                }
                .collect { voterInfo ->
                    _voterInfo.value = voterInfo
                }
        }
    }

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}