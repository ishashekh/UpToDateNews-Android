package com.coding_titans.uptodate


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class SearchResultFragment : Fragment() {

    private lateinit var resultsAdapter: ResultsAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search_result, container, false)

        val query = arguments?.getString("query")
        resultsAdapter = ResultsAdapter(listOf())
        val recyclerView = view.findViewById<RecyclerView>(R.id.search_results_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = resultsAdapter

        progressBar = view.findViewById(R.id.search_progress_bar)

        fetchSearchResults(query, requireContext())

        return view
    }


    private fun fetchSearchResults(query: String?, context: Context) {
        progressBar.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.currentsapi.services/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val currentsApi = retrofit.create(CurrentsApi::class.java)
        val call = currentsApi.getNewsBySearch(query)

        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val newsList = response.body()?.newsList ?: emptyList()
                    if (newsList.isEmpty()) {
                        val toast = Toast.makeText(context, "No news found for your query", Toast.LENGTH_LONG)
                        toast.show()
                    }
                    resultsAdapter.updateResults(newsList)
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                // Handle API call failure
            }
        })
    }

    private inner class ResultsAdapter(private var results: List<News>) : RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.search_result_item, parent, false)
            return ResultViewHolder(view)
        }

        override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
            holder.bind(getResults()[position])
        }

        override fun getItemCount(): Int {
            return getResults().size
        }

        fun updateResults(newResults: List<News>) {
            results = newResults
            notifyDataSetChanged()
        }

        private fun getResults(): List<News> {
            return results
        }

        inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val titleTextView: TextView = itemView.findViewById(R.id.result_title_text_view)
            private val descriptionTextView: TextView = itemView.findViewById(R.id.result_description_text_view)

            init {
                itemView.setOnClickListener {
                    val news = getResults()[adapterPosition]
                    val url = news.url

                    val bundle = Bundle().apply {
                        putString("url", url)
                    }
                    itemView.findNavController().navigate(R.id.action_searchResultFragment_to_webViewFragment, bundle)
                }
            }


            fun bind(news: News) {
                titleTextView.text = news.title
                descriptionTextView.text = news.description
            }
        }
    }

    private interface CurrentsApi {
        @GET("search")
        fun getNewsBySearch(
            @Query("keywords") keywords: String?,
            @Query("apiKey") apiKey: String = "WPTVRN2UoZYACLhBejlWMTNkEOQ4r-lYpBymUItdnBw4s5gh"
        ): Call<NewsResponse>
    }

    private data class NewsResponse(
        @SerializedName("news") val newsList: List<News>
    )

    private data class News(
        @SerializedName("title") val title: String,
        @SerializedName("description") val description: String,
        @SerializedName("image") val image: String,
        @SerializedName("url") val url: String
    )
}
