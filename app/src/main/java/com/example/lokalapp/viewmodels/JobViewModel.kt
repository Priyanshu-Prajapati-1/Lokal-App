package com.example.lokalapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lokalapp.mappers.jobDataModel
import com.example.lokalapp.model.JobListModel
import com.example.lokalapp.repository.JobRepository
import com.example.lokalapp.response.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val jobRepository: JobRepository,
) : ViewModel() {

    private var _jobs = MutableStateFlow<Resource<List<JobListModel>>>(Resource.Loading())
    val jobs: StateFlow<Resource<List<JobListModel>>> = _jobs.asStateFlow()

    init {
        getJobs()
    }

    fun getJobs() {
        viewModelScope.launch {
            jobRepository.getJobs()
                .map { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data.results.let { jobList ->
                                Resource.Success(jobList.map { it.jobDataModel() })
                            }
                        }

                        is Resource.Error -> Resource.Error(resource.message ?: "An error occurred")
                        is Resource.Loading -> Resource.Loading()
                    }
                }
                .flowOn(Dispatchers.IO)
                .catch { exception ->
                    emit(Resource.Error(exception.message ?: "An error occurred"))
                }
                .collect { resource ->
                    _jobs.value = resource
                }
        }
    }

}


