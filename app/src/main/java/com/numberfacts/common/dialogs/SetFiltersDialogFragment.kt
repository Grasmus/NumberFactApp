package com.numberfacts.common.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.numberfacts.R
import com.numberfacts.common.models.Filter
import com.numberfacts.databinding.FragmentSetFilterDialogBinding

class SetFiltersDialogFragment: DialogFragment() {

    private val binding: FragmentSetFilterDialogBinding by viewBinding(CreateMethod.INFLATE)

    private var applyFilterCallback: ((Int, Int) -> Unit)? = null
    private var filter: Filter? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setUiLogic()

        return AlertDialog.Builder(
            requireContext()
        ).setView(binding.root).create()
    }

    fun setApplyFilterCallback(callback: (Int, Int) -> Unit) {
        applyFilterCallback = callback
    }

    fun setFilterValues(from: Int?, till: Int?) {
        filter = Filter(from, till)
    }

    private fun setUiLogic() {
        binding.setFilterFromTextInputEditText.apply {
            doAfterTextChanged {
                binding.setFilterFromTextInputLayout.isErrorEnabled = false
                binding.setFilterFromTextInputLayout.error = null

                checkButtonEnabled()
            }

            setText(filter?.from?.toString())
        }

        binding.setFilterTillTextInputEditText.apply {
            doAfterTextChanged {
                binding.setFilterTillTextInputLayout.isErrorEnabled = false
                binding.setFilterTillTextInputLayout.error = null

                checkButtonEnabled()
            }

            setText(filter?.till?.toString())
        }

        binding.setFilterApplyButton.setOnClickListener {
            applyFilter()
        }
    }

    private fun applyFilter() {
        val from = binding.setFilterFromTextInputEditText.text.toString().toIntOrNull()
        val till = binding.setFilterTillTextInputEditText.text.toString().toIntOrNull()

        if (from == null) {
            Log.wtf("SetFiltersDialogFragment::applyFilter",
                "From filter number was null")

            binding.setFilterFromTextInputLayout.isErrorEnabled = true
            binding.setFilterFromTextInputLayout.error =
                getString(R.string.number_text_input_error)

            return
        }

        if (till == null) {
            Log.wtf("SetFiltersDialogFragment::applyFilter",
                "Till filter number was null")

            binding.setFilterTillTextInputLayout.isErrorEnabled = true
            binding.setFilterTillTextInputLayout.error =
                getString(R.string.number_text_input_error)

            return
        }

        if (from >= till) {
            binding.setFilterFromTextInputLayout.isErrorEnabled = true
            binding.setFilterFromTextInputLayout.error =
                getString(R.string.filter_from_error_message)

            return
        }

        applyFilterCallback?.invoke(from, till)
        dismiss()
    }

    private fun checkButtonEnabled() {
        binding.setFilterApplyButton.isEnabled =
            !binding.setFilterFromTextInputEditText.text.isNullOrEmpty() &&
                    !binding.setFilterTillTextInputEditText.text.isNullOrEmpty()
    }
}
