package kz.step.playground.second.viewmodels

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.Parcelize
import kz.step.playground.second.activity.SecondActivity
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SecondAsyncTaskViewModel(var context: Context): ViewModel() {

    fun sendRequest(url: String){
        AsyncJob().execute(url)
    }

    fun downloadFile(url: String){
        AsyncJobDownloadFile().execute(url)
    }

    fun initiateDownloadFile(url: String): String{
        var url = URL(url)

        var httpURLConnection = (url.openConnection() as HttpURLConnection)

        httpURLConnection.requestMethod = "GET"

        httpURLConnection.doInput = true

        httpURLConnection.connect()

        var directory = File(Environment.getExternalStorageDirectory(), "downloads")
        directory.mkdirs()

        var uploadedFile = File(directory, "file.pdf")
        uploadedFile.createNewFile()

        var inputStream = httpURLConnection.inputStream

        var fileOutputStream = FileOutputStream(uploadedFile)

        var byteArrayData = ByteArray(1024)

        var readedData = 0

        while(true){
            var readed = inputStream.read(byteArrayData)
            if(readed == -1){
                break
            }
            fileOutputStream.write(byteArrayData, 0, readedData)

        }

        fileOutputStream.close()

        return uploadedFile.path
    }

    fun initiateRequest(url: String): Result<String>{
        var url = URL(url)

        var httpURLConnection = (url.openConnection() as HttpURLConnection)

        httpURLConnection.requestMethod = "GET"

        httpURLConnection.addRequestProperty("Content-Type", "application/json")

        httpURLConnection.doInput = true

        httpURLConnection.connect()

        var inputStreamReader = InputStreamReader(httpURLConnection.inputStream)

        var data = ""

        inputStreamReader.forEachLine {
            data = data + it + "\n"
        }

        data.trim()

        Log.d("DATA RESULT", data)
        return Result<String>().apply { success = data }
    }

    inner class AsyncJob : AsyncTask<String, Int, Result<String>>(){
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg arguments: String): Result<String>{
            return initiateRequest(arguments.get(0))
        }

        override fun onPostExecute(result: Result<String>){
            initiateNotifyBroadcastReceiver(result)
        }
    }


    inner class AsyncJobDownloadFile : AsyncTask<String, Int, String>(){

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: String?): String {
            return initiateDownloadFile(p0.get(0).orEmpty())
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: String){
            super.onPostExecute(result)
            Log.d("DATA", result)
            initiateNotifyFileLoaded(result)
        }
    }

    fun initiateNotifyBroadcastReceiver(result: Result<String>){
        var intent = Intent().apply {
            this.putExtra("data", result)
        }
        intent.setAction(SecondActivity.FILE_LOADED_ACTION)
        context.sendBroadcast(intent)
    }

    fun initiateNotifyFileLoaded(path: String){
        var intent = Intent().apply {
            this.putExtra("data", path)
        }
        intent.setAction(SecondActivity.FILE_LOADED_ACTION)
        context.sendBroadcast(intent)
    }
}


@Parcelize
class Result<T>: Parcelable {
    var success: T? = null
    var error: T? = null
}