package com.example.tripy.module

import android.content.Context
import androidx.room.Room
import com.example.tripy.domain.ExpenseRepo
import com.example.tripy.domain.TripRepo
import com.example.tripy.room.AppDatabase
import com.example.tripy.room.ExpenseDao
import com.example.tripy.room.TripDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "TRIP_DATABASE"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTripDao(appDatabase: AppDatabase): TripDao {
        return appDatabase.tripDao()
    }

    @Provides
    @Singleton
    fun provideTripRepo(tripDao: TripDao):TripRepo{
        return TripRepo(tripDao)
    }

    @Provides
    @Singleton
    fun provideExpenseDao(appDatabase: AppDatabase): ExpenseDao {

        return appDatabase.expenseDao()
    }

    @Provides
    @Singleton
    fun provideExpenseRepo(expenseDao: ExpenseDao): ExpenseRepo {
        return ExpenseRepo(expenseDao)
    }

}