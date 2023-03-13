package ru.ivos.kse_test.presentation.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ivos.kse_test.R
import ru.ivos.kse_test.databinding.FragmentAddCarBinding
import ru.ivos.kse_test.domain.model.Car
import ru.ivos.kse_test.presentation.viewmodels.AddCarViewModel
import ru.ivos.kse_test.utils.showLongToast
import ru.ivos.kse_test.utils.showShortToast

@AndroidEntryPoint
class AddCarFragment : Fragment() {

    private var _binding: FragmentAddCarBinding? = null
    private val binding: FragmentAddCarBinding
        get() = _binding ?: throw RuntimeException("Binding is empty")

    private val viewModel by viewModels<AddCarViewModel>()

    private val ivCarPhoto: ImageView by lazy {
        binding.ivCarPhoto
    }

    private var bitmap: Bitmap? = null
    private var car: Car? = null

    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            lifecycleScope.launch {
                if (car == null) {
                    ivCarPhoto.setImageURI(it)
                } else {
                    withContext(Dispatchers.IO) {
                        val newCar = Car(
                            model = car!!.model,
                            manufacturer = car!!.manufacturer,
                            transmission = car!!.transmission,
                            price = car!!.price,
                            year = car!!.year,
                            image = MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver,
                                it
                            )
                        )
                        viewModel.insertCar(newCar)
                        car = newCar
                    }
                }
            }
            bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            car = it?.getParcelable("image")
            bitmap = car?.image
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        if (car != null) {
            fillFieldsFromArgs()
        }
    }

    private fun setUpClickListeners() {
        binding.btnAddPhoto.setOnClickListener {
            contract.launch("image/*")
        }
        binding.btnAddCar.setOnClickListener {
            addCar()
        }
    }

    private fun addCar() = with(binding) {
        val correct = checkFields()
        if (correct) {
            val car = Car(
                model = etModel.text.toString().trim(),
                manufacturer = etManufacturer.text.toString().trim(),
                price = etPrice.text.toString().trim().toDouble(),
                transmission = etTransmission.text.toString().trim(),
                year = etYear.text.toString().trim().toInt(),
                image = bitmap!!,
            )
            viewModel.insertCar(car)
            findNavController().navigate(R.id.action_addCarFragment_to_homeFragment)
            showLongToast("Car added successfully!")
        } else showLongToast("Fill aff fields")
    }

    private fun checkFields(): Boolean {
        var correct = true
        with(binding) {
            if (
                etModel.text.toString() == "" || etManufacturer.text.toString() == "" ||
                etPrice.text.toString() == "" || etTransmission.text.toString() == "" ||
                etYear.text.toString() == ""
            ) correct = false

            etModel.doOnTextChanged { text, start, before, count ->
                if (text!!.isEmpty()) {
                    tilModel.error = "Model does not be empty"
                    showShortToast("Model does not be empty")
                    correct = false
                } else {
                    tilModel.error = null
                }
            }
            etManufacturer.doOnTextChanged { text, start, before, count ->
                if (text!!.isEmpty()) {
                    tilManufacturer.error = "Manufacturer does not be empty"
                    showShortToast("Manufacturer does not be empty")
                    correct = false
                } else {
                    tilManufacturer.error = null
                }
            }
            etPrice.doOnTextChanged { text, start, before, count ->
                if (text!!.isEmpty()) {
                    tilPrice.error = "Price does not be empty"
                    showShortToast("Price does not be empty")
                    correct = false
                } else {
                    tilPrice.error = null
                }
            }
            etTransmission.doOnTextChanged { text, start, before, count ->
                if (text!!.isEmpty()) {
                    tilTransmission.error = "Transmission does not be empty"
                    showShortToast("Transmission does not be empty")
                    correct = false
                } else {
                    tilTransmission.error = null
                }
            }
            etYear.doOnTextChanged { text, start, before, count ->
                if (text!!.isEmpty()) {
                    tilYear.error = "Year does not be empty"
                    showShortToast("Year does not be empty")
                    correct = false
                } else {
                    tilYear.error = null
                }
            }
            if (bitmap == null) {
                correct = false
            }
        }
        return correct
    }

    private fun fillFieldsFromArgs() = with(binding) {
        etModel.text = Editable.Factory.getInstance().newEditable(car?.model)
        etManufacturer.text = Editable.Factory.getInstance().newEditable(car?.manufacturer)
        etPrice.text = Editable.Factory.getInstance().newEditable(car?.price.toString())
        etTransmission.text = Editable.Factory.getInstance().newEditable(car?.transmission)
        etYear.text = Editable.Factory.getInstance().newEditable(car?.year.toString())
        ivCarPhoto.setImageBitmap(car?.image)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}