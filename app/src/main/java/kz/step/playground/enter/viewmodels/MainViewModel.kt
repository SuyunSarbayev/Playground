package kz.step.playground.enter.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.io.InputStreamReader
import java.lang.Runnable
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.*

class MainViewModel : ViewModel() {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.IO + job)
    var bitmapLiveData: MutableLiveData<Bitmap> = MutableLiveData()

    var getRequestLiveData: MutableLiveData<String> = MutableLiveData()

    fun initiateLaunch() {
        uiScope.launch {
            var result =
                initiateDownloadFile("https://upload.wikimedia.org/wikipedia/commons/9/96/Google_web_search.png")
            withContext(Dispatchers.Main) {
                bitmapLiveData.value = result
            }
        }
    }

    fun initiateLaunchCoroutine(passedFunction: () -> String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                passedFunction.invoke()
            }
        }
    }

    fun initializeQueryParams(url: String, queryParams: HashMap<String, String>): String {
        var dynamicUrl = url

        if (queryParams.size > 0) {
            dynamicUrl = dynamicUrl + "?"
        }
        queryParams.forEach {
            dynamicUrl = dynamicUrl + it.key + "=" + it.value + "&"
        }

        return dynamicUrl
    }

    fun initializeThreadPoolExecutor(): Executor{
        return ThreadPoolExecutor(
            2, 2,
            0, TimeUnit.MILLISECONDS,
            LinkedBlockingDeque<Runnable>()
        )
    }

    fun initiateGetRequest(passedUrl: String, queryParams: HashMap<String, String>) {
        initializeThreadPoolExecutor().execute(
            kotlinx.coroutines.Runnable {
                var url = URL(initializeQueryParams(passedUrl, queryParams))

                var urlConnection = (url.openConnection() as HttpURLConnection)

                urlConnection.requestMethod = "GET"

                urlConnection.addRequestProperty("Accept", "application/json")
                urlConnection.addRequestProperty("Content-Type", "text/html")

                urlConnection.connect()

                var inputStream = urlConnection.inputStream

                var inputStreamReader = InputStreamReader(inputStream)

                var line: String? = null

                inputStreamReader.forEachLine {
                    line = line + it + "\n"
                }

                getRequestLiveData.postValue(line)
            }
        )
    }

    fun initiateDownloadFile(url: String): Bitmap {
        var url = URL(url)

        var httpURLConnection = (url.openConnection() as HttpURLConnection)

        httpURLConnection.doInput = true

        httpURLConnection.connect()

        var bitmap: Bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)

        return bitmap
    }
}