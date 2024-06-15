package com.example.img_decorat.di

import android.content.Context
import com.example.img_decorat.data.model.dataModels.ImageManagementUseCases
import com.example.img_decorat.data.repository.ImageManagementRepositoryImpl
import com.example.img_decorat.domain.repository.ImageManagementRepository
import com.example.img_decorat.domain.usecase.imagemanagementusecase.EditViewUseCase
import com.example.img_decorat.domain.usecase.imagemanagementusecase.SaveViewUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageManagementMoudle {
    @Provides
    @Singleton
    fun provideImageManagementRepository(context: Context): ImageManagementRepository {
        return ImageManagementRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideImageManagementUseCase(imageManagementRepository: ImageManagementRepository): ImageManagementUseCases {
        return ImageManagementUseCases(
            editViewUseCase = EditViewUseCase(imageManagementRepository),
            saveViewUseCase = SaveViewUseCase(imageManagementRepository)
        )
    }
}