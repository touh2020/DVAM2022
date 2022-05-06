package eu.epfc.pocketmovie

import android.content.Context
import android.os.Handler
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


class HttpRequestTask(
    private val url: String,
    private val applicationContext: Context,
    private var choice :Int = 1
): Runnable

{
    override fun run() {
            // create http client
        val okHttpClient = OkHttpClient()
            // build  a request
        val request = Request.Builder().url(url).build()
          // send the request
        try {
            val response =okHttpClient.newCall(request).execute()
           // extract the response from the data
            val responseString: String? = response.body?.string()
            if(responseString != null)
            {
                // suitch the main thread
            val mainHandler = Handler(applicationContext.getMainLooper())
                mainHandler.post{
                    MovieRepository.instance.onReceiveHttpResponse(responseString, true,choice)
                }
            }
        }
        catch ( exception :IOException )
        {
            // SWITCH the main thread
            val mainHandler = Handler(applicationContext.getMainLooper())
            mainHandler.post{
                MovieRepository.instance.onReceiveHttpResponse(null, false,choice)

            }
        }
    }

}