package com.ssafy.family.di

import com.ssafy.family.data.remote.api.AccountAPI
import com.ssafy.family.data.repository.AccountRepository
import com.ssafy.family.data.repository.AccountRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAccountRepository(
        apiAPI: AccountAPI,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher minDispatcher: CoroutineDispatcher
    ): AccountRepository
            = AccountRepositoryImpl(apiAPI,ioDispatcher,minDispatcher)

//    @Singleton
//    @Provides
//    fun provideRoomRepository(covidDao: CovidDao): RoomRepository = RoomRepositoryImpl(covidDao)

}