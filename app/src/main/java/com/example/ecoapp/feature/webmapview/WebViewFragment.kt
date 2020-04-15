package com.example.ecoapp.feature.webmapview

import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.ecoapp.R
import com.example.ecoapp.feature.main.MainActivity
import kotlinx.android.synthetic.main.fragment_webview.*


class WebViewFragment  : Fragment() {

    private var webViewBundle: Bundle? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_webview, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true
        val webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                val regex = Regex(pattern = "https://recyclemap\\.ru*")
                val matched = regex.containsMatchIn(input = url)
                if (matched)
                view.loadUrl(url)
                else{
                    val openlink = Intent(Intent.ACTION_VIEW, url.toUri())
                    startActivity(Intent.createChooser(openlink, "Open in"))
                }
                return true
            }

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val regex = Regex(pattern = "https://recyclemap\\.ru*")
                val matched = regex.containsMatchIn(input = request.url.toString())
                if (matched)
                view.loadUrl(request.url.toString())
                else{
                    val openlink = Intent(Intent.ACTION_VIEW, request.url)
                    startActivity(Intent.createChooser(openlink, "Browser"))
                }
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                Toast.makeText(activity?.applicationContext, "Страница загружена!", Toast.LENGTH_SHORT)
                    .show()
                map_loading.visibility = View.GONE
                webView.visibility = View.VISIBLE
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url,favicon)
                Toast.makeText(
                    activity?.getApplicationContext(),
                    "Начата загрузка страницы",
                    Toast.LENGTH_SHORT
                )
                    .show()
                map_loading.visibility = View.VISIBLE
                webView.visibility = View.GONE
            }
        }
        webView.webViewClient = webViewClient
        webView.settings.javaScriptEnabled = true
        if (webViewBundle == null) {
            webView.loadUrl("https://recyclemap.ru/kazan")
        } else {
            webView.restoreState(webViewBundle);
        }
        (activity as MainActivity).supportActionBar?.hide()
    }

    override fun onPause() {
        super.onPause()
        webViewBundle = Bundle()
        webView.saveState(webViewBundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView.webViewClient = null
    }
}