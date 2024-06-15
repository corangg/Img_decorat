package com.example.img_decorat.di

import android.content.Context
import com.example.img_decorat.data.repository.BackgroundRepositoryImpl
import com.example.img_decorat.data.repository.DBRepository
import com.example.img_decorat.data.repository.EmojiRepositoryImpl
import com.example.img_decorat.data.repository.HueRepositoryImpl
import com.example.img_decorat.data.repository.SaveDataRepository
import com.example.img_decorat.data.repository.SplitRepository
import com.example.img_decorat.data.repository.SplitStackRepository
import com.example.img_decorat.data.repository.TextViewRepositoryImpl
import com.example.img_decorat.data.source.local.DB.EmojiDB
import com.example.img_decorat.data.source.local.DB.ViewDB
import com.example.img_decorat.data.source.local.Dao.EmojiDao
import com.example.img_decorat.data.source.local.Dao.ViewDao
import com.example.img_decorat.domain.repository.BackgroundRepository
import com.example.img_decorat.domain.usecase.backgroundusecase.SetBackgroundScaleUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.ImageAddViewUseCase
import com.example.img_decorat.presentation.ui.view.BTNAnimation
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
    fun provideLayerAnimation(context: Context): BTNAnimation {
        return BTNAnimation(provideContext(context))
    }

    @Singleton
    @Provides
    fun provideBackgroundRepository(): BackgroundRepository {
        return BackgroundRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideSetBackgroundScaleUseCase(backgroundRepository: BackgroundRepository): SetBackgroundScaleUseCase {
        return SetBackgroundScaleUseCase(backgroundRepository)
    }


    @Provides
    @Singleton
    fun provideHueRepository(): HueRepositoryImpl {
        return HueRepositoryImpl()
    }



    @Provides
    @Singleton
    fun provideTextViewRepository(context: Context): TextViewRepositoryImpl {
        return TextViewRepositoryImpl(context)
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

    @Provides
    @Singleton
    fun provideSaveDataRepository(context: Context): SaveDataRepository {
        return SaveDataRepository(context)
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
