package kz.step.playground.enter.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import kz.step.playground.R
import kz.step.playground.utils.Constants
import kz.step.playground.enter.viewmodels.CoroutineViewModel

class CoroutineActivity : AppCompatActivity() {

    var viewModel: CoroutineViewModel = CoroutineViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.initiateGetRequest(Constants.currency_url, Constants.data)
        viewModel.initiateDownloadFile(Constants.image_url)
        initializeObservers()
    }

    fun initializeObservers(){
        viewModel.liveData.observe(this, Observer{
            Log.d("COROUTINE DATA", it.toString())
        })

        viewModel.bitmapLiveData.observe(this, Observer{
            Log.d("COROUTINE DATA1", "HERE")
            image.setImageBitmap(it)
        })
    }

}