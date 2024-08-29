package com.example.lokalapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lokalapp.mappers.jobDataModel
import com.example.lokalapp.model.JobListModel
import com.example.lokalapp.repository.LokalDataBaseRepository
import com.example.lokalapp.response.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookMarkViewModel @Inject constructor(
    private val lokalDataBaseRepository: LokalDataBaseRepository,
) : ViewModel() {

    private var _isStoreInDB = MutableStateFlow(false)
    val isStoreInDB: StateFlow<Boolean> = _isStoreInDB.asStateFlow()

    private var _jobList = MutableStateFlow<Resource<List<JobListModel>>>(Resource.Loading())
    val jobList: StateFlow<Resource<List<JobListModel>>> = _jobList.asStateFlow()

    fun getStoreJob() {
        viewModelScope.launch {
            lokalDataBaseRepository.getJobs()
                .flowOn(Dispatchers.IO)
                .catch {
                    emit(emptyList())
                }
                .collect { data ->
                    _jobList.value = Resource.Success(data)
                }
        }
    }

    fun deleteJob(id: Int) {
        viewModelScope.launch {
            lokalDataBaseRepository.deleteJob(id)
        }
    }

    fun getJobById(id: Int) {
        viewModelScope.launch {
            val data = lokalDataBaseRepository.getJobById(id)
            if (data != null) {
                _isStoreInDB.value = true
            } else {
                _isStoreInDB.value = false
            }
        }
    }
}