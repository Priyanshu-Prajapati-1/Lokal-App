package com.example.lokalapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs_table")
data class JobListModel(
    @PrimaryKey
    val id: Int? = 0,
    val title: String? = "",
    val location: String? = "",
    val jobRole: String? = "",
    val jobHours: String? = "",
    val salary: String? = "",
    val company: String? = "",
    val imageUrl: String? = "",
    val vacancies: String? = "",
    val buttonText: String? = "",
    val whatsappNo: String? = "",
    val whatsappLink: String? = "",
    val customLink: String? = "",
    val experience: String? = "",
    val qualifications: String? = "",
    val shift: String? = "",
)