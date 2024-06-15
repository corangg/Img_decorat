package com.example.img_decorat.di

import android.content.Context
import com.example.img_decorat.data.model.dataModels.TextViewUseCases
import com.example.img_decorat.data.repository.TextViewRepositoryImpl
import com.example.img_decorat.domain.repository.TextViewRepository
import com.example.img_decorat.domain.usecase.textviewusecase.AddEditTextViewViewListUseCase
import com.example.img_decorat.domain.usecase.textviewusecase.CheckEditableTextViewUseCase
import com.example.img_decorat.domain.usecase.textviewusecase.EditTextViewAddListUseCase
import com.example.img_decorat.domain.usecase.textviewusecase.EditTextViewAddViewListUseCase
import com.example.img_decorat.domain.usecase.textviewusecase.EditTextViewSetBackgroundColorUseCase
import com.example.img_decorat.domain.usecase.textviewusecase.EditTextViewSetFontUseCase
import com.example.img_decorat.domain.usecase.textviewusecase.EditTextViewSetTextColorUseCase
import com.example.img_decorat.domain.usecase.textviewusecase.EditTextViewSetTextSizeUseCase
import com.example.img_decorat.domain.usecase.textviewusecase.LayerTextViewSetTextBackgroundColorUseCase
import com.example.img_decorat.domain.usecase.textviewusecase.LayerTextViewSetTextColorUseCase
import com.example.img_decorat.domain.usecase.textviewusecase.LayerTextViewSetTextFontUseCase
import com.example.img_decorat.domain.usecase.textviewusecase.LayerViewUpdateTextUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TextViewMoudle {
    @Provides
    @Singleton
    fun provideTextViewRepository(context: Context): TextViewRepository {
        return TextViewRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideTextViewUseCase(textViewRepository: TextViewRepository): TextViewUseCases {
        return TextViewUseCases(
            checkEditableTextViewUseCase = CheckEditableTextViewUseCase(textViewRepository),
            editTextViewAddListUseCase = EditTextViewAddListUseCase(textViewRepository),
            editTextViewSetTextColorUseCase = EditTextViewSetTextColorUseCase(textViewRepository),
            editTextViewSetBackgroundColorUseCase = EditTextViewSetBackgroundColorUseCase(
                textViewRepository
            ),
            editTextViewSetTextSizeUseCase = EditTextViewSetTextSizeUseCase(textViewRepository),
            editTextViewSetFontUseCase = EditTextViewSetFontUseCase(textViewRepository),
            layerViewUpdateTextUseCase = LayerViewUpdateTextUseCase(textViewRepository),
            addEditTextViewViewListUseCase = AddEditTextViewViewListUseCase(textViewRepository),
            editTextViewAddViewListUseCase = EditTextViewAddViewListUseCase(textViewRepository),
            layerTextViewSetTextColorUseCase = LayerTextViewSetTextColorUseCase(textViewRepository),
            layerTextViewSetTextBackgroundColorUseCase = LayerTextViewSetTextBackgroundColorUseCase(
                textViewRepository
            ),
            layerTextViewSetTextFontUseCase = LayerTextViewSetTextFontUseCase(textViewRepository)
        )
    }
}