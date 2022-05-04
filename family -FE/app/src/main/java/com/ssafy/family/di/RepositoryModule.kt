package com.ssafy.family.di

import com.google.firebase.database.FirebaseDatabase
import com.ssafy.family.data.remote.api.AccountAPI
import com.ssafy.family.data.remote.api.FamilyAPI
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
    fun provideFamilyRepository(
        apiAPI: FamilyAPI,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): FamilyRepository
            = FamilyRepositoryImpl(apiAPI, ioDispatcher,mainDispatcher)

    @Singleton
    @Provides
    fun provideChatRepository(
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DispatcherModule.MainDispatcher mainDispatcher: CoroutineDispatcher
    ): ChatRepository
            = ChatRepositoryImpl(ioDispatcher,mainDispatcher)

//    @Singleton
//    @Provides
//    fun provideRoomRepository(covidDao: CovidDao): RoomRepository = RoomRepositoryImpl(covidDao)

}