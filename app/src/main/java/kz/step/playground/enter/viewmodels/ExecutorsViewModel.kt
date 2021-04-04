package kz.step.playground.enter.viewmodels

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.step.playground.second.activity.SecondActivity
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ExecutorsViewModel(var context: Context) : ViewModel() {

    var resultLiveData: MutableLiveData<StringBuilder> = MutableLiveData()

    var bitmapLiveData: MutableLiveData<Bitmap> = MutableLiveData()

    fun initializeExecutors(): Executor{
        return ThreadPoolExecutor(
            2,
            3,
            0,
            TimeUnit.MILLISECONDS,
            LinkedBlockingDeque<Runnable>()
        )
    }

    fun initializeQueryParams(url: String, data: HashMap<String, String>): String{
        var mutableUrl = url

        if(data.size > 0){
            mutableUrl = mutableUrl + "?"
        }

        data.forEach {
            mutableUrl = mutableUrl + it.key + "=" + it.value + "&"
        }

        return mutableUrl
    }

    fun initiateGetRequest(url: String, data: HashMap<String, String>){
        initializeExecutors().execute(Runnable {
            var result = StringBuilder()

            var executeUrl = URL(initializeQueryParams(url, data))

            var httpURLConnection = (executeUrl.openConnection() as HttpURLConnection)

            httpURLConnection.addRequestProperty("Accept", "application/json")
            httpURLConnection.addRequestProperty("Content-Type", "text/plain")
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.doInput = true
            httpURLConnection.connectTimeout = 30000
            httpURLConnection.connect()

            InputStreamReader(httpURLConnection.inputStream).forEachLine {
                result.append(it)
            }

            resultLiveData.postValue(result)
        })
    }

    fun initiateDownloadFile(url: String){
        initializeExecutors().execute(Runnable {
            var url = URL(url)
            var httpUrlConnection = (url.openConnection() as HttpURLConnection)

            httpUrlConnection.connectTimeout = 30000
            httpUrlConnection.readTimeout = 30000
            httpUrlConnection.requestMethod = "GET"
            httpUrlConnection.doInput = true
            httpUrlConnection.connect()

            bitmapLiveData.postValue(BitmapFactory.decodeStream(httpUrlConnection.inputStream))
        })
    }
}