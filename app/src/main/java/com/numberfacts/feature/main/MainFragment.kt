package com.numberfacts.feature.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.numberfacts.R
import com.numberfacts.common.base.decoration.CustomDividerItemDecoration
import com.numberfacts.common.base.view.BaseFragment
import com.numberfacts.common.constants.SET_FILTER_DIALOG_TAG
import com.numberfacts.common.dialogs.SetFiltersDialogFragment
import com.numberfacts.common.models.Filter
import com.numberfacts.common.utils.SwipeToDeleteCallback
import com.numberfacts.databinding.FragmentMainBinding
import com.numberfacts.domain.constants.OrderBy
import com.numberfacts.domain.constants.OrderDirection
import com.numberfacts.domain.entities.numberfact.NumberFact
import javax.inject.Inject
import kotlin.properties.Delegates

class MainFragment: BaseFragment() {

    @Inject
    lateinit var viewModel: MainViewModel

    private val binding: FragmentMainBinding by viewBinding(CreateMethod.INFLATE)

    private val orderBySelection = arrayOf(OrderBy.Date, OrderBy.Number)
    private val orderDirectionSelection = arrayOf(
        OrderDirection.Descending,
        OrderDirection.Ascending
    )

    private lateinit var recyclerViewDivider: DividerItemDecoration
    private var deleteBackgroundColor by Delegates.notNull<Int>()
    private var deleteDrawable: Drawable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getAppComponent().inject(this)
        observeData()
        setUiLogic()

        return binding.root
    }

    private fun observeData() {
        viewModel.errorMessage.observe(viewLifecycleOwner, ::onErrorMessage)
        viewModel.isLoading.observe(viewLifecycleOwner, ::showLoadingDialog)
        viewModel.uiEvent.observe(viewLifecycleOwner, ::onUiEvent)
        viewModel.numberFacts.observe(viewLifecycleOwner, ::onUpdateNumberFacts)
        viewModel.filter.observe(viewLifecycleOwner, ::onUpdateFilter)
    }

    private fun onUiEvent(uiEvent: MainViewModel.UiEvent) {
        when (uiEvent) {
            MainViewModel.UiEvent.OnConnectionError -> {
                onConnectionError(binding.root, viewModel)
            }

            MainViewModel.UiEvent.OnUnexpectedError -> {
                onUnexpectedError(binding.root)
            }
        }
    }

    private fun setUiLogic() {
        recyclerViewDivider = CustomDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )

        deleteBackgroundColor = requireContext().getColor(R.color.red)
        deleteDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.icon_delete)

        binding.mainNumberTextInput.doAfterTextChanged { editable ->
            binding.mainNumberTextInputLayout.isErrorEnabled = false
            binding.mainNumberTextInputLayout.error = null
            binding.mainGetNumberFactButton.isEnabled = editable.toString().isNotEmpty()
        }

        binding.mainGetNumberFactButton.setOnClickListener {
            getNumberFact(
                binding.mainNumberTextInput.editableText.toString().toIntOrNull()
            )
        }

        binding.mainGetRandomNumberFactButton.setOnClickListener {
            viewModel.getRandomNumberFact()
        }

        binding.mainFilterButton.setOnClickListener {
            showApplyFilterDialog()
        }

        binding.mainRemoveFilterButton.setOnClickListener {
            viewModel.removeFilters()
        }

        setSpinnerLogic()
    }

    private fun onUpdateNumberFacts(numberFacts: List<NumberFact>) {
        val numberFactsAdapter = NumberFactsRecyclerViewAdapter(
            numberFacts.toMutableList(),
            ::navigateToDetails
        )

        binding.mainFactsHistoryRecyclerView.apply {
            adapter = numberFactsAdapter
            addItemDecoration(recyclerViewDivider)
        }

        val swipeToDeleteCallback = object: SwipeToDeleteCallback(
            deleteBackgroundColor,
            deleteDrawable
        ) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (viewHolder as NumberFactHolder).getNumberFactId()?.let { id ->
                    viewModel.deleteNumberFact(id)
                } ?: Log.wtf(
                    "MainFragment::onUpdateNumberFacts",
                    "Number fact id was null!"
                )

                numberFactsAdapter.removeItem(viewHolder.adapterPosition)
            }
        }

        ItemTouchHelper(swipeToDeleteCallback)
            .attachToRecyclerView(binding.mainFactsHistoryRecyclerView)
    }

    private fun onUpdateFilter(filter: Filter) {
        if (filter.from == null || filter.till == null) {
            binding.mainRemoveFilterButton.visibility = View.GONE
            binding.mainFilterButton.text = getText(R.string.button_filter_text)
        } else {
            val buttonFilterText = "${filter.from} - ${filter.till}"

            binding.mainRemoveFilterButton.visibility = View.VISIBLE
            binding.mainFilterButton.text = buttonFilterText
        }
    }

    private fun setSpinnerLogic() {

        val orderByTexts = arrayOf(
            getString(R.string.order_by_text_date),
            getString(R.string.order_by_text_number)
        )

        val orderDirectionTexts = arrayOf(
            getString(R.string.order_direction_text_descending),
            getString(R.string.order_direction_text_ascending)
        )

        binding.mainSortBySpinner.apply {
            adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, orderByTexts).apply {
                setDropDownViewResource(R.layout.spinner_dropdown_item)
            }

            onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.getNumberFactsByOrder(
                        orderBySelection[position]
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        binding.mainSortDirectionSpinner.apply {
            adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, orderDirectionTexts).apply {
                setDropDownViewResource(R.layout.spinner_dropdown_item)
            }

            onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.getNumberFactsByDirection(
                        orderDirectionSelection[position]
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun showApplyFilterDialog() {
        SetFiltersDialogFragment().apply {
            setApplyFilterCallback { from, till ->
                viewModel.getNumberFactsWithFilter(from, till)
            }

            setFilterValues(
                viewModel.getFilter()?.from,
                viewModel.getFilter()?.till
            )
        }.show(parentFragmentManager, SET_FILTER_DIALOG_TAG)
    }

    private fun getNumberFact(inputNumber: Int?) {
        inputNumber?.let { number ->
            viewModel.getNumberFact(number)

            binding.mainNumberTextInput.editableText.clear()
            hideKeyboard()
        } ?: run {
            Log.wtf("MainFragment::getNumberFact",
                "Input number was null")

            binding.mainNumberTextInputLayout.isErrorEnabled = true
            binding.mainNumberTextInputLayout.error = getString(R.string.number_text_input_error)
        }
    }

    private fun navigateToDetails(number: Int, fact: String) {
        val action = MainFragmentDirections
            .actionMainFragmentToDetailsFragment(number, fact)

        findNavController().navigate(action)
    }
}
