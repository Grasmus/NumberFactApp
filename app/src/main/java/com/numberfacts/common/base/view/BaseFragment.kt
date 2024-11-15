package com.numberfacts.common.base.view

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.numberfacts.R
import com.numberfacts.common.base.viewmodel.BaseViewModel
import com.numberfacts.common.constants.LOADING_DIALOG_TAG
import com.numberfacts.common.dialogs.LoadingIndicatorDialogFragment
import com.numberfacts.feature.NumberFactsApp

abstract class BaseFragment: Fragment() {

    private val loadingDialog = LoadingIndicatorDialogFragment()
    private var isLoadingDialogActive = false

    override fun onPause() {
        super.onPause()

        showLoadingDialog(false)
    }

    protected fun showLoadingDialog(isLoading: Boolean) {
        if (!isLoadingDialogActive && isLoading) {
            loadingDialog.show(childFragmentManager, LOADING_DIALOG_TAG)

            isLoadingDialogActive = true
        } else if (isLoadingDialogActive && !isLoading) {
            loadingDialog.dismiss()

            isLoadingDialogActive = false
        }
    }

    protected fun onErrorMessage(errorMessage: String) {
        showMessageDialog(
            getString(R.string.error_occurred),
            errorMessage
        )
    }

    protected fun getAppComponent() =
        (requireContext().applicationContext as NumberFactsApp).appComponent

    protected fun onConnectionError(view: View, viewModel: BaseViewModel) {
        Snackbar.make(view,
            getString(R.string.check_network),
            Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.button_text_try_again)) {
            viewModel.tryAgain()
        }.show()
    }

    protected fun onUnexpectedError(view: View) {
        Snackbar.make(view,
            getString(R.string.unknown_error),
            Snackbar.LENGTH_LONG
        ).show()
    }

    protected fun hideKeyboard() {
        requireActivity().currentFocus?.let { focus ->
            val manager = requireContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager?

            manager?.hideSoftInputFromWindow(
                focus.windowToken,
                0
            )
        }
    }

    private fun showMessageDialog(title: String,
                                  message: String,
                                  callback: (() -> Unit)? = null) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            setOnDismissListener {
                callback?.invoke()
            }
        }.show()
    }
}
