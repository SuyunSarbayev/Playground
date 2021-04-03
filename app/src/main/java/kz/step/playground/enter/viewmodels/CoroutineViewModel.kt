package kz.step.playground.enter.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class CoroutineViewModel : ViewModel() {

    var liveData = MutableLiveData<Result<String>>()

    var bitmapLiveData = MutableLiveData<Bitmap>()

    var job = Job()

    var uiScope = CoroutineScope(Dispatchers.IO + job)

    fun initiateGetRequest(url: String, data: HashMap<String, String>){
        uiScope.launch{
            sendRequest(url, data)
        }
    }

    fun initiateDownloadFile(url: String){
        uiScope.launch {
            withContext(Dispatchers.IO){
                var url = URL(url)


                var httpUrlConnection = (url.openConnection() as HttpURLConnection)
                httpUrlConnection.requestMethod = "GET"
                httpUrlConnection.addRequestProperty("Accept", "image")
                httpUrlConnection.doInput = true

                var bitmap = BitmapFactory.decodeStream(httpUrlConnection.inputStream)

                bitmapLiveData.postValue(bitmap)

            }
        }
    }

    fun sendRequest(url: String, data: HashMap<String, String>){
        var data = ""
        var url = URL(url)

        var httpURLConnection = (url.openConnection() as HttpURLConnection)
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.addRequestProperty("Content-Type", "application/json")
        httpURLConnection.addRequestProperty("Accept", "application/json")
        httpURLConnection.doInput = true
        httpURLConnection.connect()

        var responseCode = httpURLConnection.responseCode

        var result: Result<String> = Result()

        when(responseCode){
            200 -> {
                Log.d("DATA", "HERE")
                var inputStreamReader = InputStreamReader(httpURLConnection.inputStream)
                inputStreamReader.forEachLine {
                    data = data + it + "\n"
                }
                Log.d("DATA", responseCode.toString())
                Log.d("DATA", data)
                result.success = data
                liveData.postValue(result)
            }
            201 -> {
                Log.d("DATA", "HERE1")
                var inputStreamReader = InputStreamReader(httpURLConnection.inputStream)
                inputStreamReader.forEachLine {
                    data = data + it + "\n"
                }
                result.success = data
                liveData.postValue(result)
            }
            else -> {
                var errorStreamReader = InputStreamReader(httpURLConnection.errorStream)

                errorStreamReader.forEachLine {
                    data = data + it + "\n"
                }
                result.error = Exception(data)
                liveData.postValue(result)
            }
        }

    }
}

class Result<T>{
    var success: T? = null

    lateinit var error: Exception
}