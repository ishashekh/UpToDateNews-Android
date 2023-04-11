package com.coding_titans.uptodate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.findNavController
import android.view.inputmethod.EditorInfo
import android.widget.Button
import androidx.core.os.bundleOf


class SearchFragment : Fragment() {

    private lateinit var searchText: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        searchText = view.findViewById(R.id.input_search)
        val searchButton = view.findViewById<Button>(R.id.search_button)

        searchText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }

        searchButton.setOnClickListener {
            performSearch()
        }

        return view
    }

    private fun performSearch() {
        val query = searchText.text.toString()
        val bundle = bundleOf("query" to query)
        val action = SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(query).apply {
            setArguments(bundle)
        }
        view?.findNavController()?.navigate(action)
    }


}
