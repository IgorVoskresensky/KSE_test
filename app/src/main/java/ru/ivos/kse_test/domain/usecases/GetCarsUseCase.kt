package ru.ivos.kse_test.domain.usecases

import ru.ivos.kse_test.domain.dao.AppDao
import javax.inject.Inject

class GetCarsUseCase @Inject constructor(
    private val appDao: AppDao
) {

    suspend operator fun invoke() = appDao.getCars()
}