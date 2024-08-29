package com.example.lokalapp.repository

import android.util.Log
import com.example.lokalapp.model.JobResponse
import com.example.lokalapp.network.JobApiService
import com.example.lokalapp.response.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class JobRepository @Inject constructor(
    private val jobApiService: JobApiService,
) {
    suspend fun getJobs(page: String = "1"): Flow<Resource<JobResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = jobApiService.getJobsDetails(page)
            if (response.isSuccessful) {
                response.body()?.let { jobResponse ->
                    emit(Resource.Success(data = jobResponse))
                } ?: emit(Resource.Error("Response body is null"))
            } else {
                emit(Resource.Error("Failed to fetch jobs: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }
}