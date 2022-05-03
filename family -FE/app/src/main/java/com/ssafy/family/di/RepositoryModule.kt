package com.ssafy.family.di

import com.google.firebase.database.FirebaseDatabase
import com.ssafy.family.data.remote.api.AccountAPI
import com.ssafy.family.data.remote.api.CalendarAPI
import com.ssafy.family.data.repository.*
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
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): AccountRepository
            = AccountRepositoryImpl(apiAPI,ioDispatcher,mainDispatcher)

    @Singleton
    @Provides
    fun provideChatRepository(
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): ChatRepository
            = ChatRepositoryImpl(ioDispatcher,mainDispatcher)

    @Singleton
    @Provides
    fun provideCalendarRepository(
        apiAPI: CalendarAPI,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): CalendarRepository
            = CalendarRepositoryImpl(apiAPI,ioDispatcher,mainDispatcher)

//    @Singleton
//    @Provides
//    fun provideRoomRepository(covidDao: CovidDao): RoomRepository = RoomRepositoryImpl(covidDao)

}