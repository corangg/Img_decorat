package com.example.img_decorat.di

import com.example.img_decorat.data.model.dataModels.HueUseCases
import com.example.img_decorat.data.repository.HueRepositoryImpl
import com.example.img_decorat.domain.repository.HueRepository
import com.example.img_decorat.domain.usecase.hueusecase.HueCheckUseCase
import com.example.img_decorat.domain.usecase.hueusecase.HueEditViewBrightnessUseCase
import com.example.img_decorat.domain.usecase.hueusecase.HueEditViewSaturationUseCase
import com.example.img_decorat.domain.usecase.hueusecase.HueEditViewTransparencyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HueMoudle {
    @Provides
    @Singleton
    fun provideHueRepository(): HueRepository {
        return HueRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideHueUsecase(hueRepository: HueRepository): HueUseCases {
        return HueUseCases(
            hueCheckUseCase = HueCheckUseCase(hueRepository),
            hueEditViewBrightnessUseCase = HueEditViewBrightnessUseCase(hueRepository),
            hueEditViewSaturationUseCase = HueEditViewSaturationUseCase(hueRepository),
            hueEditViewTransparencyUseCase = HueEditViewTransparencyUseCase(hueRepository)
        )
    }
}