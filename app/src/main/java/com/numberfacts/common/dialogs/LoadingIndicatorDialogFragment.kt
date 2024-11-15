package com.numberfacts.common.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.numberfacts.R
import com.numberfacts.databinding.FragmentDialogLoadingDialogBinding

class LoadingIndicatorDialogFragment: DialogFragment() {

    private val binding: FragmentDialogLoadingDialogBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(
            requireContext(),
            R.style.NumberFacts_LoadingDialogTheme
        ).apply {
            setView(binding.root)
            isCancelable = false
        }.create()
    }
}
