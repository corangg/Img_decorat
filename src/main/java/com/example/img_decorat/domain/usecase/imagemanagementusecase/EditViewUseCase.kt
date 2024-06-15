package com.example.img_decorat.domain.usecase.imagemanagementusecase

import android.view.View
import com.example.img_decorat.domain.repository.ImageManagementRepository

class EditViewUseCase(private val imageManagementRepository: ImageManagementRepository) {
    fun execute(view: View, fileName: String) {
        return imageManagementRepository.editViewSave(view, fileName)
    }
}