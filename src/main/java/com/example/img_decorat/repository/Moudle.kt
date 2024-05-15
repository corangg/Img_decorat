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
    @Singleton
    @Provides
    fun provideImageDataRepository(): ImageDataRepository {
        return ImageDataRepository()
    }

    @Singleton
    @Provides
    fun provideLayerListRepository(@ApplicationContext context: Context): LayerListRepository {
        return LayerListRepository(context,ImageDataRepository())
    }

}
