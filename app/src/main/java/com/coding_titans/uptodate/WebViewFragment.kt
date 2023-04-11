package com.coding_titans.uptodate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController


class WebViewFragment : Fragment() {

    private lateinit var webView: WebView
    private lateinit var backButton: ImageButton

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_web_view, container, false)

        webView = view.findViewById(R.id.web_view_frag)
        backButton = view.findViewById(R.id.back_button)

        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        val url = arguments?.getString("url")
        if (url != null) {
            webView.loadUrl(url)
        }

        backButton.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                view.findNavController().navigateUp()
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView.stopLoading()
        webView.loadUrl("about:blank")
        webView.webChromeClient = null
        webView.clearHistory()
        webView.removeAllViews()
        webView.destroy()
    }
}

