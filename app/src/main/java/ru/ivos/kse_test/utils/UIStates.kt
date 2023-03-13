package ru.ivos.kse_test.utils

import ru.ivos.kse_test.domain.model.Car

sealed class UIStates {
    object LOADING : UIStates()
    class FAILURE(val message: String) : UIStates()
    class SUCCESS(val carsList: List<Car>) : UIStates()
}