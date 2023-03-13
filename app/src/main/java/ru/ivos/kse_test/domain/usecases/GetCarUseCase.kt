package ru.ivos.kse_test.domain.usecases

import ru.ivos.kse_test.domain.dao.AppDao
import javax.inject.Inject

class GetCarUseCase @Inject constructor(
    private val appDao: AppDao
) {

    suspend operator fun invoke(model: String) = appDao.getCar(model)
}