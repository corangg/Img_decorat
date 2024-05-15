package com.example.img_decorat.repository

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Moudle {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    fun provideImageDataRepository(): ImageDataRepository {
        return ImageDataRepository()
    }

    @Provides
    @Singleton
    fun provideLayerListRepository(context: Context, imageDataRepository: ImageDataRepository): LayerListRepository {
        return LayerListRepository(context, imageDataRepository)
    }

    @Singleton
    @Provides
    fun provideBackgroundRepository(): BackgroundRepository {
        return BackgroundRepository()
    }



}
