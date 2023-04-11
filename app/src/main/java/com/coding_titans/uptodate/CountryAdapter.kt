package com.coding_titans.uptodate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CountryAdapter(
    private val countries: List<Country>,
    private val onCountrySelected: (String) -> Unit,
    private val spinner: Spinner
) : RecyclerView.Adapter<CountryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_recycler, parent, false)
        return ViewHolder(view, onCountrySelected)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countries[position]
        holder.countryName.text = country.name
        holder.countryCode = country.code
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    inner class ViewHolder(itemView: View, private val onCountrySelected: ((String) -> Unit)?) :
        RecyclerView.ViewHolder(itemView) {
        val countryName: TextView = itemView.findViewById(R.id.countryButton)
        lateinit var countryCode: String

        init {
            itemView.setOnClickListener {
                onCountrySelected?.invoke(countryCode)
            }
        }
    }

    fun setSpinnerAdapter() {
        val countryNames = countries.map { it.name }
        val adapter = ArrayAdapter(spinner.context, android.R.layout.simple_spinner_item, countryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                onCountrySelected(countries[position].code)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        })
    }
}
