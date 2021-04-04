package kz.step.playground.second.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import kz.step.playground.R
import kz.step.playground.second.viewmodels.SecondAsyncTaskViewModel
import kz.step.playground.second.viewmodels.SecondCoroutineViewModel
import kz.step.playground.second.viewmodels.SecondExecutorsViewModel
import kz.step.playground.utils.Constants

class SecondActivity : AppCompatActivity(){

    var secondAsyncTaskViewModel = SecondAsyncTaskViewModel(this)

    var secondExecutorsViewModel = SecondExecutorsViewModel(this)

    var secondCoroutineViewModel = SecondCoroutineViewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeObservers()
//        secondAsyncTaskViewModel.downloadFile(Constants.file_load_url)
//        secondAsyncTaskViewModel.sendRequest(Constants.currency_url)
//        secondExecutorsViewModel.initiateDownloadFile(Constants.file_load_url)
//        secondCoroutineViewModel.initiateGetRequest(Constants.currency_url)

        secondCoroutineViewModel.initiateDownloadImage(Constants.image_url)

        secondCoroutineViewModel.initiateDownloadFile(Constants.file_load_url)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(FileLoadedReceiver(), IntentFilter(FILE_LOADED_ACTION))
    }

    override fun onPause() {
        super.onPause()
//        unregisterReceiver(FileLoadedReceiver())
    }

    fun initializeObservers(){
        secondCoroutineViewModel.liveData.observe(this, Observer{
            Log.d("COROUTINE", it)
        })

        secondCoroutineViewModel.liveDataImage.observe(this, Observer{
            image.setImageBitmap(it)
        })

        secondCoroutineViewModel.liveDataFile.observe(this, Observer{
            initiateOpenFile(it)
        })
    }

    inner class FileLoadedReceiver: BroadcastReceiver(){
        override fun onReceive(context: Context, dataIntent: Intent){
            initiateOpenFile((dataIntent.extras?.get("data")).toString())
        }
    }

    fun initiateOpenFile(path: String){
        val intent = Intent(Intent.ACTION_VIEW)
        val uri: Uri = Uri.parse(
            path
        )

        intent.setDataAndType(uri, "text/pdf")
        startActivity(Intent.createChooser(intent, "Open folder"))
    }

    companion object{
        const val FILE_LOADED_ACTION = "FILE_LOADED_ACTION"
    }
}
