package com.numberfacts.common.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
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
                checkButtonEnabled()
            }

            setText(filter?.till?.toString())
        }

        binding.setFilterApplyButton.setOnClickListener {
            applyFilter()
        }
    }

    private fun applyFilter() {
        val from = binding.setFilterFromTextInputEditText.text.toString().toInt()
        val till = binding.setFilterTillTextInputEditText.text.toString().toInt()

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
