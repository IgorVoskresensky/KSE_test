package ru.ivos.kse_test.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ivos.kse_test.domain.model.Car
import ru.ivos.kse_test.domain.usecases.DeleteCarUseCase
import ru.ivos.kse_test.domain.usecases.InsertCarUserCase
import javax.inject.Inject

@HiltViewModel
class AddCarViewModel @Inject constructor(
    private val insertCarUserCase: InsertCarUserCase,
    private val getCarUseCase: DeleteCarUseCase
) : ViewModel() {

    fun insertCar(car: Car) = viewModelScope.launch {
        insertCarUserCase.invoke(car)
    }

    fun getCar(model: String) = viewModelScope.launch {
        getCarUseCase.invoke(model)
    }
}