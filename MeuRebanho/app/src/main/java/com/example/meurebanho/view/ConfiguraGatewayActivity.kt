package com.example.meurebanho.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class ConfiguraGatewayActivity : AppCompatActivity() {
    private lateinit var myWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Inicializando o WebView */
        myWebView = WebView(this)

        /* Configurando um WebViewClient personalizado */
        myWebView.webViewClient = MyWebViewClient()

        /* Habilitando o javascript da pagina de configuracao wifi */
        myWebView.settings.javaScriptEnabled = true

        /* Carregando a página da web na WebView */
        myWebView.loadUrl("http://192.168.4.1/wifi?")

        /* Definindo o WebView como o conteúdo da atividade */
        setContentView(myWebView)
    }

    private class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (Uri.parse(url).host == "192.168.4.1/wifi?") {
                // O URL pertence ao seu aplicativo,
                // permita que o WebView carregue a página
                return false
            }
            /* Caso contrário, o link não pertence ao seu aplicativo, inicie uma Activity para lidar com URLs. */
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            view?.context?.startActivity(intent)
            return true
        }
    }
}