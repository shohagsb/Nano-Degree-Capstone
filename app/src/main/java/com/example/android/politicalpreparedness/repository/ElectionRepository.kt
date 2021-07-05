package com.example.android.politicalpreparedness.repository

import android.util.Log
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(private val database: ElectionDatabase) {

    // Fetch data from Network and insert to DB
    suspend fun refreshUpcomingElectionFromNetwork() {
        withContext(Dispatchers.IO) {
            try {
                val asteroidsResult = CivicsApi.retrofitService.getElections()
                Log.d("ElectionRepoTAG", "refreshUpcomingElectionFromNetwork: $asteroidsResult")
                //database.asteroidDao.insertAll(*asteroidsResult.toTypedArray())
            } catch (e: Exception) {
                Log.d("ElectionRepoTAG", "refreshUpcomingElectionFromNetwork: ${e.message}")

            }
        }
    }
}