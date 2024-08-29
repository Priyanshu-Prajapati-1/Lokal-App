package com.example.lokalapp.mappers

import com.example.lokalapp.model.JobListModel
import com.example.lokalapp.model.Result

fun Result.jobDataModel(): JobListModel {
    return JobListModel(
        id = this.id,
        title = this.title,
        location = this.primary_details?.Place,
        jobRole = this.job_role,
        jobHours = this.job_hours,
        salary = this.primary_details?.Salary,
        company = this.company_name,
        imageUrl = this.creatives?.get(0)?.file,
        vacancies = this.openings_count.toString(),
        buttonText = this.button_text,
        whatsappNo = this.whatsapp_no,
        whatsappLink = this.contact_preference?.whatsapp_link,
        customLink = this.custom_link,
        experience = this.primary_details?.Experience,
        qualifications = this.primary_details?.Qualification,
        shift = this.shift_timing.toString()
    )
}