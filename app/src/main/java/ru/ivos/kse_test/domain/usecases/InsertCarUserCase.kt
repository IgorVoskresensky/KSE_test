package ru.ivos.kse_test.domain.usecases

import ru.ivos.kse_test.domain.dao.AppDao
import ru.ivos.kse_test.domain.model.Car
import javax.inject.Inject

class InsertCarUserCase @Inject constructor(
    private val appDao: AppDao
) {

    suspend operator fun invoke(car: Car) = appDao.insertCar(car)
}