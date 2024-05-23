package com.example.img_decorat.di

import android.content.Context
import com.example.img_decorat.data.repository.BackgroundRepository
import com.example.img_decorat.data.repository.DBRepository
import com.example.img_decorat.data.repository.ImageDataRepository
import com.example.img_decorat.data.repository.ImageManagementRepository
import com.example.img_decorat.data.repository.LayerListRepository
import com.example.img_decorat.data.repository.SplitRepository
import com.example.img_decorat.data.repository.SplitStackRepository
import com.example.img_decorat.data.source.local.DB.EmojiDB
import com.example.img_decorat.data.source.local.DB.ViewDB
import com.example.img_decorat.data.source.local.Dao.EmojiDao
import com.example.img_decorat.data.source.local.Dao.ViewDao
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

    @Provides
    @Singleton
    fun provideImageManagementRepository(context: Context): ImageManagementRepository {
        return ImageManagementRepository(context)
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

    @Provides
    @Singleton
    fun provideSplitStackRepository(): SplitStackRepository {
        return SplitStackRepository()
    }

    @Provides
    @Singleton
    fun provideSplitRepository(context: Context): SplitRepository {
        return SplitRepository(context)
    }

    @Provides
    @Singleton
    fun provideDBRepository(emojiDao: EmojiDao, viewDao: ViewDao): DBRepository {
        return DBRepository(emojiDao, viewDao)
    }

    @Singleton
    @Provides
    fun provideEmojiDB(@ApplicationContext context: Context): EmojiDB {
        return EmojiDB.getDatabase(context)
    }

    @Provides
    fun provideEmojiDao(emojiDB: EmojiDB): EmojiDao {
        return emojiDB.emojiDao()
    }

    @Singleton
    @Provides
    fun provideViewDB(@ApplicationContext context: Context): ViewDB {
        return ViewDB.getDatabase(context)
    }

    @Provides
    fun provideViewDao(viewDB: ViewDB): ViewDao {
        return viewDB.viewDao()
    }
}
