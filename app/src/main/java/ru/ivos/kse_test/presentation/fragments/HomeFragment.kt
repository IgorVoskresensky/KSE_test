package ru.ivos.kse_test.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import ru.ivos.kse_test.R
import ru.ivos.kse_test.databinding.FragmentHomeBinding
import ru.ivos.kse_test.domain.model.Car
import ru.ivos.kse_test.presentation.adapters.HomeAdapter
import ru.ivos.kse_test.presentation.adapters.SwipeHelper
import ru.ivos.kse_test.presentation.viewmodels.HomeViewModel
import ru.ivos.kse_test.utils.UIStates
import ru.ivos.kse_test.utils.gone
import ru.ivos.kse_test.utils.showLongToast
import ru.ivos.kse_test.utils.visible

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding ?: throw RuntimeException("Binding is empty")

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var adapter: HomeAdapter

    private var currentList = emptyList<Car>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HomeAdapter()
        viewModel.getCars()
        observeViewModel()
        setupClickListeners()
        setUpRecyclerView()
    }

    private fun observeViewModel() {
        viewModel.homeState.observe(viewLifecycleOwner) { data ->
            when (data) {
                is UIStates.LOADING -> {
                    binding.pbHome.visible()
                }
                is UIStates.SUCCESS -> {
                    if (data.carsList.isEmpty()) {
                        Log.d("mytag", "${data.carsList}")
                        binding.tvNoCars.visible()
                        binding.pbHome.gone()
                    }
                    binding.pbHome.gone()
                    binding.tvNoCars.gone()
                    binding.rvHome.adapter = adapter
                    currentList = data.carsList.sortedBy { it.model }
                    adapter.differ.submitList(currentList)

                }
                is UIStates.FAILURE -> {
                    binding.tvNoCars.text = "Error"
                    binding.tvNoCars.visible()
                    binding.pbHome.gone()
                }
            }
        }
    }

    private fun setupClickListeners() {
        adapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_imageFragment,
                bundleOf(Pair("image", it))
            )
        }

        binding.etFilter.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val list = adapter.differ.currentList
                val newList = list.filter {
                    it.manufacturer.equals(
                        binding.etFilter.text.toString().trim(),
                        ignoreCase = true
                    )
                }
                if (list.isNotEmpty()) {
                    adapter.differ.submitList(newList)
                } else showLongToast("No items were founded")
                if (binding.etFilter.text.isEmpty()) {
                    adapter.differ.submitList(list)
                }
            }
            true
        }

        binding.etFilter.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                adapter.differ.submitList(currentList)
            }
        }
    }

    private fun setUpRecyclerView() {
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.rvHome) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                val deleteButton = deleteButton(position)
                val archiveButton = editButton(position)
                return listOf(deleteButton, archiveButton)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvHome)
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.delete),
            14.0f,
            android.R.color.holo_orange_dark,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    viewModel.deleteCar(adapter.differ.currentList[position].model)
                    viewModel.getCars()
                }
            })
    }

    private fun editButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.edit),
            14.0f,
            android.R.color.holo_orange_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    findNavController().navigate(
                        R.id.action_homeFragment_to_addCarFragment,
                        bundleOf(Pair("image", adapter.differ.currentList[position]))
                    )
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}