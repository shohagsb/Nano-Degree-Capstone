package com.example.android.politicalpreparedness.repository

import android.util.Log
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ElectionRepository(private val database: ElectionDatabase) {

    // Fetch upcoming Election data from Network
    val getUpcomingElections: Flow<ElectionResponse> = flow {
        val upcomingElections = CivicsApi.retrofitService.getElections()
        emit(upcomingElections)
    }.flowOn(Dispatchers.IO)

    // Fetch Voter Info data from Network
    fun getVoterInfo(id: Long, address: String): Flow<VoterInfoResponse> = flow {
        val voterInfo = CivicsApi.retrofitService.getVoterInfo(address, id)
        Log.d("ElectionRepositoryTAG", "getVoterInfo: $voterInfo")
        emit(voterInfo)
    }.flowOn(Dispatchers.IO)


}