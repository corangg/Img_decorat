package com.example.img_decorat.di

import android.content.Context
import com.example.img_decorat.data.repository.HueRepositoryImpl
import com.example.img_decorat.data.repository.SaveDataRepositoryImpl
import com.example.img_decorat.domain.repository.HueRepository
import com.example.img_decorat.domain.repository.SaveDataRepository
import com.example.img_decorat.domain.usecase.saveusecase.SaveSetLoadTitleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SaveDataMoudle {
    @Provides
    @Singleton
    fun provideSaveDataRepository(context: Context): SaveDataRepository {
        return SaveDataRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideSaveDataUseCase(saveDataRepository: SaveDataRepository): SaveSetLoadTitleUseCase {
        return SaveSetLoadTitleUseCase(saveDataRepository)
    }
}