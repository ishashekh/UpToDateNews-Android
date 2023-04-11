package com.coding_titans.uptodate

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class Country(val name: String, val code: String)

class CountryBasedNewsFragment : Fragment() {
    private lateinit var countryRecyclerView: RecyclerView
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var countrySpinner: Spinner

    private lateinit var progressBar: ProgressBar


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.currentsapi.services/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(CurrentsApiService::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_country_based_news, container, false)
        countrySpinner = view.findViewById(R.id.countrySpinner)
        newsRecyclerView = view.findViewById(R.id.newsRecyclerView)

        progressBar = view.findViewById(R.id.progressBar)

        val countryNames = fetchCountryData().map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countrySpinner.adapter = adapter
        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val countryCode = fetchCountryData()[position].code
                fetchNews(countryCode)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // do nothing
            }
        }

        return view
    }


    private fun fetchCountryData(): List<Country> {
        val countries = listOf(
            Country("Argentina", "ar"),
            Country("Australia", "au"),
            Country("Brazil", "br"),
            Country("Canada", "ca"),
            Country("China", "cn"),
            Country("France", "fr"),
            Country("Germany", "de"),
            Country("India", "in"),
            Country("Russia", "ru"),
            Country("United States", "us"),
            Country("United Kingdom", "gb")
            // add more countries as needed
        )
        return countries
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun fetchNews(countryCode: String) {
        val apiKey = "WPTVRN2UoZYACLhBejlWMTNkEOQ4r-lYpBymUItdnBw4s5gh"

        progressBar.visibility = View.VISIBLE // Show the progress bar

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val newsApiResponse = apiService.getNewsForCountry(countryCode, apiKey)
                val newsList = newsApiResponse.news
                val newsAdapter = NewsAdapter(newsList) { newsUrl: String ->
                    // Create a new dialog with a WebView
                    val dialog = Dialog(requireContext())
                    dialog.setContentView(R.layout.webview_country)

                    // Set up the WebView and ProgressBar
                    val webView = dialog.findViewById<WebView>(R.id.webview_country)
                    val progressBar = dialog.findViewById<ProgressBar>(R.id.progressBar2)
                    webView.settings.javaScriptEnabled = true

                    // Load the web page and show the dialog
                    webView.loadUrl(newsUrl)
                    webView.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView, url: String) {
                            progressBar.visibility = View.GONE
                            webView.visibility = View.VISIBLE
                        }
                    }
                    dialog.show()
                }
                newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                newsRecyclerView.adapter = newsAdapter
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Error fetching news: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
