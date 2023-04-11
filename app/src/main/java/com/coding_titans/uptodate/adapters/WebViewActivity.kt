package com.coding_titans.uptodate.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.coding_titans.uptodate.R

class WebViewActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var backButton: ImageView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        // Initialize the progress bar and back button
        progressBar = findViewById(R.id.progress_bar)
        backButton = findViewById(R.id.back_button)

        backButton.setOnClickListener {
            onBackPressed()
        }

        val url = intent.getStringExtra("url")
        val webView = findViewById<WebView>(R.id.web_view)
        webView.settings.javaScriptEnabled = true
        if (url != null) {
            webView.loadUrl(url)
        }

        // Set up a WebViewClient for the WebView and override its onPageFinished method
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                // Hide the progress bar once the web page is loaded
                progressBar.visibility = View.GONE
            }
        }

        // Show the progress bar while the web page is loading
        progressBar.visibility = View.VISIBLE

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

