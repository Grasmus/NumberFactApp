package com.numberfacts.feature.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.numberfacts.common.base.view.BaseFragment
import com.numberfacts.databinding.FragmentDetailsBinding
import javax.inject.Inject

class DetailsFragment: BaseFragment() {

    @Inject
    lateinit var viewModel: DetailsViewModel

    private val binding: FragmentDetailsBinding by viewBinding(CreateMethod.INFLATE)
    private val navArgs: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getAppComponent().inject(this)
        setData()
        observeData()
        setUiLogic()

        return binding.root
    }

    private fun setData() {
        viewModel.setNumber(navArgs.number)
        viewModel.setFact(navArgs.fact)
    }

    private fun observeData() {
        viewModel.fact.observe(viewLifecycleOwner, ::onUpdateFact)
        viewModel.number.observe(viewLifecycleOwner, ::onUpdateNumber)
    }

    private fun setUiLogic() {
        binding.detailsNumberMaterialToolbar.setNavigationOnClickListener {
            navigateBack()
        }
    }

    private fun onUpdateFact(fact: String) {
        binding.detailsNumberFactTextView.text = fact
    }

    private fun onUpdateNumber(number: Int) {
        binding.detailsNumberMaterialToolbar.title = number.toString()
    }

    private fun navigateBack() {
        findNavController().popBackStack()
    }
}
