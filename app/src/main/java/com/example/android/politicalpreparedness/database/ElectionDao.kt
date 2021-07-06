package com.example.android.politicalpreparedness.database

import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {

    //: Add insert query
    @Insert
    suspend fun insertElection(election: Election)

    //: Add select all election query
    @Query("SELECT * FROM ELECTION_TABLE ORDER BY id ASC")
    fun getAllSavedElections(): List<Election>

    //: Add select single election query
    @Query("SELECT * FROM ELECTION_TABLE WHERE :electionId = id ")
    fun getSingleElection(electionId: Long): Election

    //: Add delete query
    @Query("DELETE FROM ELECTION_TABLE WHERE :electionId = id")
    suspend fun deleteElection(electionId: Long)

    //: Add clear query
    @Query("DELETE FROM ELECTION_TABLE")
    suspend fun clearAll()
}