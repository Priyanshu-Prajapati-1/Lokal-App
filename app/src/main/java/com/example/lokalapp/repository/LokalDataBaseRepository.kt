package com.example.lokalapp.repository

import com.example.lokalapp.data.LokalDAO
import com.example.lokalapp.model.JobListModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LokalDataBaseRepository @Inject constructor(
    private val lokalDAO: LokalDAO,
) {

    fun getJobs(): Flow<List<JobListModel>> = lokalDAO.getJobList()

    suspend fun insertJob(jobListModel: JobListModel) = lokalDAO.insertJob(jobListModel)

    suspend fun deleteJob(id: Int) = lokalDAO.deleteJob(id)

    suspend fun getJobById(id: Int): JobListModel = lokalDAO.getJobById(id)

    suspend fun deleteAllJobs() = lokalDAO.deleteAllJobs()

}