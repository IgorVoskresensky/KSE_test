package ru.ivos.kse_test.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ivos.kse_test.domain.usecases.DeleteCarUseCase
import ru.ivos.kse_test.domain.usecases.GetCarsUseCase
import ru.ivos.kse_test.utils.UIStates
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCarsUseCase: GetCarsUseCase,
    private val deleteCarUseCase: DeleteCarUseCase
) : ViewModel() {

    private var _homeState = MutableLiveData<UIStates>()
    val homeState: LiveData<UIStates> get() = _homeState

    fun getCars() = viewModelScope.launch {
        _homeState.value = UIStates.LOADING
        kotlin.runCatching {
            getCarsUseCase.invoke()
        }.onSuccess {
            _homeState.value = UIStates.SUCCESS(it)
        }.onFailure {
            _homeState.value = UIStates.FAILURE("Error")
        }
    }

    fun deleteCar(model: String) = viewModelScope.launch {
        deleteCarUseCase.invoke(model)
    }
}