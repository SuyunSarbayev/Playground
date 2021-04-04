package kz.step.playground.second.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.InputStreamReader
import java.net.URL
import java.net.HttpURLConnection
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream

class SecondCoroutineViewModel(context: Context) : ViewModel() {

    var liveData = MutableLiveData<String>()
    var liveDataFile = MutableLiveData<String>()
    var liveDataImage = MutableLiveData<Bitmap>()

    var backScope = CoroutineScope(Dispatchers.IO + Job())

    var uiScope = CoroutineScope(Dispatchers.Main + Job())

    fun initiateGetRequest(url: String){
        backScope.launch {
            var result = ""

            var url = URL(url)

            var httpURLConnection = (url.openConnection() as HttpURLConnection)

            httpURLConnection.requestMethod = "GET"

            httpURLConnection.addRequestProperty("Content-Type", "application/json")

            httpURLConnection.connect()

            InputStreamReader(httpURLConnection.inputStream).forEachLine{
                result = result + it
            }

            uiScope.launch {
                liveData.value = result
            }
        }
    }

    fun initiateDownloadImage(url: String){
        backScope.launch {
            var url = URL(url)

            var httpURLConnection = (url.openConnection() as HttpURLConnection)

            httpURLConnection.requestMethod = "GET"

            httpURLConnection.addRequestProperty("Content-Type", "application.json")

            httpURLConnection.connect()

            var bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)

            uiScope.launch {
                liveDataImage.value = bitmap
            }
        }
    }

    fun initiateDownloadFile(url: String){

        backScope.launch{
            var url = URL(url)

            var httpURLConnection = (url.openConnection() as HttpURLConnection)

            httpURLConnection.requestMethod = "GET"

            httpURLConnection.doInput = true

            httpURLConnection.inputStream

            var fileFolder = File(Environment.getExternalStorageDirectory().toString()
                    + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator + "Playground")

            fileFolder.mkdir()

            if(fileFolder.exists()){
                var file = File(fileFolder, "file.pdf")
                file.createNewFile()

                var readed = 0

                var byteArray = ByteArray(1024)

                var fileOutputStream = FileOutputStream(file)

                while(true){
                    readed = httpURLConnection.inputStream.read(byteArray)

                    if(readed == -1){
                        break
                    }

                    fileOutputStream.write(byteArray, 0, readed)
                }

                uiScope.launch {
                    liveDataFile.value = file.path
                }
            }
        }
    }
}