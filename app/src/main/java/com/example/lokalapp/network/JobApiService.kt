package com.example.lokalapp.network

import com.example.lokalapp.model.JobResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JobApiService {

    @GET("common/jobs")
    suspend fun getJobsDetails(
        @Query("page") page: String,
    ): Response<JobResponse>

}