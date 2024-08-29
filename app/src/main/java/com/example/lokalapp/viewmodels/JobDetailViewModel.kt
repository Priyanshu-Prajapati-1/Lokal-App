package com.example.lokalapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lokalapp.mappers.jobDataModel
import com.example.lokalapp.model.JobResponse
import com.example.lokalapp.repository.JobRepository
import com.example.lokalapp.repository.LokalDataBaseRepository
import com.example.lokalapp.response.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobDetailViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val lokalDataBaseRepository: LokalDataBaseRepository,
) : ViewModel() {


    private var _jobs = MutableStateFlow<Resource<JobResponse>>(Resource.Loading())
    val jobs: StateFlow<Resource<JobResponse>> = _jobs.asStateFlow()

    fun getJobs() {
        viewModelScope.launch {
            jobRepository.getJobs("1")
                .flowOn(Dispatchers.IO)
                .catch { exception ->
                    emit(Resource.Error(exception.message ?: "An error occurred"))
                }
                .collect { resource ->
                    _jobs.value = resource
                }
        }
    }

    fun insertJob(id: Int) {
        viewModelScope.launch {
            val resource = jobs.value
            if (resource is Resource.Success) {
                val job = resource.data.results.find { it.id == id }?.jobDataModel()
                job?.let { lokalDataBaseRepository.insertJob(it) }
            }
        }
    }
}