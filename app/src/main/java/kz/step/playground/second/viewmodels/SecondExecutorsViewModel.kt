package kz.step.playground.second.viewmodels

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.lifecycle.ViewModel
import kz.step.playground.second.activity.SecondActivity
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.net.URL
import java.net.HttpURLConnection
import java.util.concurrent.*

class SecondExecutorsViewModel(var context: Context) : ViewModel(){

    fun initiateGetExecutor(): Executor{
        return ThreadPoolExecutor(
            2,
            2,
            0,
            TimeUnit.MILLISECONDS,
            LinkedBlockingDeque()
        )
    }

    fun initiateGetRequest(url: String): String{
        var url = URL(url)

        var httpOpenConnection = (url.openConnection() as HttpURLConnection)

        httpOpenConnection.requestMethod = "GET"

        httpOpenConnection.addRequestProperty("Cpntent-Type","application/json")

        httpOpenConnection.doInput = true

        httpOpenConnection.connect()

        var inputStream = httpOpenConnection.inputStream

        var inputStreamReader = InputStreamReader(inputStream)

        var data = ""

        inputStreamReader.forEachLine{
            data = data + it + "\n"
        }

        return data
    }

    fun initiateDownloadFileRequest(url: String): String{
        var url = URL(url)

        var httpUrlConnection = (url.openConnection() as HttpURLConnection)

        httpUrlConnection.requestMethod = "GET"

        httpUrlConnection.doInput = true

        httpUrlConnection.connect()

        var fileFolder = File(Environment.getExternalStorageDirectory().toString() +
                File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator + "Playground")

        fileFolder.mkdir()

        var uploadedFile = File(fileFolder, "file.pdf")

        uploadedFile.createNewFile()

        var inputStream = httpUrlConnection.inputStream

        var fileOutputStream = FileOutputStream(uploadedFile)
        var byteArrayData = ByteArray(1024)
        var readedData = 0
        
        while(true){
            readedData = inputStream.read(byteArrayData)

            if(readedData == -1){
                break
            }

            fileOutputStream.write(byteArrayData, 0, readedData)
        }

        return uploadedFile.path
    }

    fun initiateDownloadFile(url: String){
        initiateGetExecutor().execute(Runnable{
            initiatePostData(initiateDownloadFileRequest(url))
        })
    }

    fun initiateRequest(url: String){
        initiateGetExecutor().execute(Runnable{
            initiateGetRequest(url)
        })
    }

    fun initiatePostData(path: String){
        var intent = Intent(Intent.ACTION_VIEW)
        intent.setAction(SecondActivity.FILE_LOADED_ACTION)
        intent.putExtra("data", path)
        context.sendBroadcast(intent)
    }
}