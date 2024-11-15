package com.numberfacts.common.base.decoration

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import com.numberfacts.R

class CustomDividerItemDecoration(
    context: Context,
    orientation: Int
): DividerItemDecoration(context, orientation) {

    init {
        AppCompatResources
            .getDrawable(
                context,
                R.drawable.shape_divider_item
            )?.let { drawable ->
                setDrawable(drawable)
            }
    }
}
