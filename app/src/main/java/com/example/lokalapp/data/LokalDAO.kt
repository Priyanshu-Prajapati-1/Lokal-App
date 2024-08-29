package com.example.lokalapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lokalapp.model.JobListModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LokalDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(jobListModel: JobListModel)

    @Query("DELETE  FROM jobs_table WHERE id = :id")
    suspend fun deleteJob(id: Int)

    @Query("SELECT * FROM jobs_table")
    fun getJobList(): Flow<List<JobListModel>>

    @Query("SELECT * FROM jobs_table WHERE id = :id")
    suspend fun getJobById(id: Int): JobListModel

    @Query("DELETE FROM jobs_table")
    suspend fun deleteAllJobs()

}