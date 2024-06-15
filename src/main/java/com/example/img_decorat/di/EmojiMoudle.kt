package com.example.img_decorat.di

import android.content.Context
import com.example.img_decorat.data.model.dataModels.EmojiUseCases
import com.example.img_decorat.data.repository.EmojiRepositoryImpl
import com.example.img_decorat.domain.repository.EmojiRepository
import com.example.img_decorat.domain.usecase.emojiusecase.EmojiAddLayerUseCase
import com.example.img_decorat.domain.usecase.emojiusecase.EmojiDBListClassificationUseCase
import com.example.img_decorat.domain.usecase.emojiusecase.EmojiDataStringToBitmapUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.ImageAddViewUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EmojiMoudle {
    @Provides
    @Singleton
    fun provideEmojiRepository(
        context: Context,
        imageAddViewUseCase: ImageAddViewUseCase
    ): EmojiRepository {
        return EmojiRepositoryImpl(context, imageAddViewUseCase)
    }

    @Provides
    @Singleton
    fun provideEmojiUseCase(emojiRepository: EmojiRepository): EmojiUseCases{
        return EmojiUseCases(
            emojiAddLayerUseCase = EmojiAddLayerUseCase(emojiRepository),
            emojiDataStringToBitmapUseCase = EmojiDataStringToBitmapUseCase(emojiRepository),
            emojiDBListClassificationUseCase = EmojiDBListClassificationUseCase(emojiRepository)
        )
    }
}