
package com.coding_titans.uptodate

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coding_titans.uptodate.adapters.RecyclerAdapter
import com.coding_titans.uptodate.api.New
import com.coding_titans.uptodate.api.NewsApiJSON
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



const val BASE_URL = "https://api.currentsapi.services"


class HomeFragment : Fragment() {

    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()
    private var linksList = mutableListOf<String>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val adView = view.findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        makeAPIRequest()
        return view
    }

    private fun setUpRecyclerView(){

        val tx = view?.findViewById<RecyclerView>(R.id.rv_recyclerView)
        if (tx != null) {
            tx.layoutManager = LinearLayoutManager(context)
        }
        if (tx != null) {
            tx.adapter = RecyclerAdapter(titlesList, descList, imagesList, linksList)
        }

    }

    private fun addToList(title: String,description:String,image:String,link:String){
        titlesList.add(title)
        descList.add(description)
        imagesList.add(image)
        linksList.add(link)
    }

    private fun makeAPIRequest(){

        val api:APIRequest = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)

        GlobalScope.launch(Dispatchers.IO){
            try {
                val response: NewsApiJSON = api.getNews()

                for (article: New in response.news){
                    Log.i("MainActivity","Result=$article")
                    addToList(article.title,article.description,article.image,article.url)
                }

                withContext(Dispatchers.Main){
                    setUpRecyclerView()
                }
            }
            catch (e:Exception){
                Log.e("MainActivity",e.toString())
            }

        }

    }


}