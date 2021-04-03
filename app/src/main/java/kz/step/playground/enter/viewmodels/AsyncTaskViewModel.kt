package kz.step.playground.enter.viewmodels

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class AsyncTaskViewModel {

    var asyncLiveData = MutableLiveData<Result<String>>()

    inner class NetworkTask: AsyncTask<String, Int, Result<String>>(){
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: String?): Result<String> {
            return initiateGetRequest(p0.get(0))
        }

        override fun onPostExecute(result: Result<String>) {
            super.onPostExecute(result)
            asyncLiveData.value = result

        }
    }

    fun initiateGetRequest(url: String?): Result<String>{
        var url = URL(url)

        var httpURLConnection = (url.openConnection() as HttpURLConnection)

        httpURLConnection.requestMethod = "GET"

        httpURLConnection.connectTimeout = 30000

        httpURLConnection.doInput = true

        httpURLConnection.addRequestProperty("Content-Type", "application/json")

        httpURLConnection.connect()

        var inputStreamReader = InputStreamReader(httpURLConnection.inputStream)

        var data = ""

        inputStreamReader.forEachLine {
            data = data + it + "\n"
        }

        return Result<String>().apply {
            success = data
        }
    }

    class Result<T>{
        var success: T? = null

        var error: String? = null
    }
}