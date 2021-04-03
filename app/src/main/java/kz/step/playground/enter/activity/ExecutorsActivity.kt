package kz.step.playground.enter.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import kz.step.playground.enter.viewmodels.ExecutorsViewModel
import kz.step.playground.R

class ExecutorsActivity : AppCompatActivity() {

    var viewModel = ExecutorsViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.bitmapLiveData.observe(this, Observer{
            image.setImageBitmap(it)
        })

        viewModel.resultLiveData.observe(this, Observer {
            Log.d("DATA", it.toString())
        })
        viewModel.initiateGetRequest(
            "http://api.openweathermap.org/data/2.5/weather",
            HashMap<String, String>().apply {
                put("q", "almaty")
                put("appid", "1747f4277a992f22e1f3886f6c729462")
            })

        viewModel.initiateDownloadFile("https://i1.wp.com/itc.ua/wp-content/uploads/2017/07/google-amp-fast-speed-travel-ss-1920-800x450.jpg")
    }
}