package com.numberfacts.feature.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.numberfacts.common.constants.DATE_FORMAT_PATTERN
import com.numberfacts.databinding.ViewItemNumberFactBinding
import com.numberfacts.domain.entities.numberfact.NumberFact
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NumberFactsRecyclerViewAdapter(
    private val numberFacts: MutableList<NumberFact>,
    private val navigateToDetailsCallback: (Int, String) -> Unit
): RecyclerView.Adapter<NumberFactsRecyclerViewAdapter.ViewHolder>() {

    override fun getItemCount() = numberFacts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            numberFacts[position].id?.let { id ->
                setNumberFactId(id)
            }

            numberFacts[position].number?.let { number ->
                numberFacts[position].fact?.let { fact ->
                    numberFacts[position].dateTime?.let { dateTime ->
                        setData(
                            navigateToDetailsCallback,
                            number,
                            fact,
                            dateTime
                        )
                    } ?: Log.wtf("NumberFactsRecyclerViewAdapter::onBindViewHolder",
                        "DateTime was null!")
                } ?: Log.wtf("NumberFactsRecyclerViewAdapter::onBindViewHolder",
                    "Fact was null!")
            } ?: Log.wtf("NumberFactsRecyclerViewAdapter::onBindViewHolder",
                "Number was null!")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewItemNumberFactBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    fun removeItem(position: Int): Int {
        numberFacts.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, getItemCount())

        return numberFacts.size
    }

    class ViewHolder(
        private val binding: ViewItemNumberFactBinding
    ): RecyclerView.ViewHolder(binding.root), NumberFactHolder {

        private var numberFactId: Int? = null

        override fun getNumberFactId() = numberFactId

        fun setNumberFactId(id: Int) {
            numberFactId = id
        }

        fun setData(
            callback: (Int, String) -> Unit,
            number: Int,
            fact: String,
            dateTime: Date) {

            binding.viewItemLinearLayout.setOnClickListener {
                callback(number, fact)
            }

            setFact(fact)
            setDateTime(dateTime)
        }

        private fun setFact(fact: String) {
            binding.viewItemNumberFactTextView.text = fact
        }

        private fun setDateTime(dateTime: Date) {
            val formatter = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())
            val dateString = formatter.format(dateTime)

            binding.viewItemDateTextInput.text = dateString
        }
    }
}
