package com.example.lokalapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lokalapp.model.JobListModel

@Database(entities = [JobListModel::class], version = 1, exportSchema = false)
abstract class LokalDatabase : RoomDatabase() {

    abstract fun lokalDao(): LokalDAO

}