package com.example.img_decorat.repository

import android.content.Context
import com.example.img_decorat.ui.view.BTNAnimation
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

    @Provides
    @Singleton
    fun provideLayerAnimation(context: Context, imageDataRepository: ImageDataRepository): BTNAnimation {
        return BTNAnimation(context)
    }

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
