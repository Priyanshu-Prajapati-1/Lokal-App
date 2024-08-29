package com.example.lokalapp.di

import android.content.Context
import androidx.room.Room
import com.example.lokalapp.data.LokalDAO
import com.example.lokalapp.data.LokalDatabase
import com.example.lokalapp.network.JobApiService
import com.example.lokalapp.repository.JobRepository
import com.example.lokalapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LokalModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideJobApiService(okHttpClient: OkHttpClient): JobApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JobApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideJobRepository(jobApiService: JobApiService): JobRepository {
        return JobRepository(jobApiService)
    }

    @Provides
    @Singleton
    fun provideLokaDAO(lokalDatabase: LokalDatabase): LokalDAO {
        return lokalDatabase.lokalDao()
    }

    @Provides
    @Singleton
    fun provideLokalDatabase(@ApplicationContext context: Context): LokalDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = LokalDatabase::class.java,
            name = "lokal_database"
        ).fallbackToDestructiveMigration().build()
    }
}