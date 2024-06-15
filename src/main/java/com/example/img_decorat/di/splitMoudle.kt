package com.example.img_decorat.di

import android.content.Context
import com.example.img_decorat.data.model.dataModels.SplitUseCases
import com.example.img_decorat.data.repository.SplitRepositoryImpl
import com.example.img_decorat.domain.repository.SplitRepository
import com.example.img_decorat.domain.usecase.splitusecase.CircleSplitViewUseCase
import com.example.img_decorat.domain.usecase.splitusecase.CropCircleImageUseCase
import com.example.img_decorat.domain.usecase.splitusecase.CropPolygonImageUseCase
import com.example.img_decorat.domain.usecase.splitusecase.CropSquareImageUseCase
import com.example.img_decorat.domain.usecase.splitusecase.GetIntentBitmapUseCase
import com.example.img_decorat.domain.usecase.splitusecase.PolygoneSplitViewUseCase
import com.example.img_decorat.domain.usecase.splitusecase.SetIntentUriUseCase
import com.example.img_decorat.domain.usecase.splitusecase.SquareSplitViewUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object splitMoudle {

    @Provides
    @Singleton
    fun provideSplitRepository(context: Context): SplitRepository {
        return SplitRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideSplitUseCase(splitRepository: SplitRepository): SplitUseCases {
        return SplitUseCases(
            getIntentBitmapUseCase = GetIntentBitmapUseCase(splitRepository),
            setIntentUriUseCase = SetIntentUriUseCase(splitRepository),
            squareSplitViewUseCase = SquareSplitViewUseCase(splitRepository),
            circleSplitViewUseCase = CircleSplitViewUseCase(splitRepository),
            polygoneSplitViewUseCase = PolygoneSplitViewUseCase(splitRepository),
            cropSquareImageUseCase = CropSquareImageUseCase(splitRepository),
            cropCircleImageUseCase = CropCircleImageUseCase(splitRepository),
            cropPolygonImageUseCase = CropPolygonImageUseCase(splitRepository),
        )
    }
}