package com.example.img_decorat.di

import android.content.Context
import com.example.img_decorat.data.model.dataModels.LayerListUseCases
import com.example.img_decorat.data.repository.LayerListRepositoryImpl
import com.example.img_decorat.data.repository.TextViewRepositoryImpl
import com.example.img_decorat.domain.repository.LayerListRepository
import com.example.img_decorat.domain.usecase.layerlistusecase.CheckLastSelectImageUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.CheckedListUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.CheckedViewTypeUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.DeleteListUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.ImageAddListUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.ImageAddViewUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.LoadListUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.SelectLastImageUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.SelectLayerUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.SetLastTouchedImageUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.SplitImageChangeListUseCase
import com.example.img_decorat.domain.usecase.layerlistusecase.SwapImageViewUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LayerListMoudle {
    @Provides
    @Singleton
    fun provideLayerListRepository(
        context: Context,
        textViewRepositoryImpl: TextViewRepositoryImpl
    ): LayerListRepository {
        return LayerListRepositoryImpl(context, textViewRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideLayerListUseCase(layerListRepository: LayerListRepository): LayerListUseCases {
        return LayerListUseCases(
            checkedListUseCase = CheckedListUseCase(layerListRepository),
            checkedViewTypeUseCase = CheckedViewTypeUseCase(layerListRepository),
            checkLastSelectImageUseCase = CheckLastSelectImageUseCase(layerListRepository),
            deleteListUseCase = DeleteListUseCase(layerListRepository),
            imageAddListUseCase = ImageAddListUseCase(layerListRepository),
            loadListUseCase = LoadListUseCase(layerListRepository),
            selectLastImageUseCase = SelectLastImageUseCase(layerListRepository),
            selectLayerUseCase = SelectLayerUseCase(layerListRepository),
            setLastTouchedImageUseCase = SetLastTouchedImageUseCase(layerListRepository),
            splitImageChangeListUseCase = SplitImageChangeListUseCase(layerListRepository),
            swapImageViewUseCase = SwapImageViewUseCase(layerListRepository)
        )
    }

    @Provides
    @Singleton
    fun provideImageAddViewUseCase(layerListRepository: LayerListRepository): ImageAddViewUseCase{
        return ImageAddViewUseCase(layerListRepository)
    }

}