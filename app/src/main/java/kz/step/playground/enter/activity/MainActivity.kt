package kz.step.playground.enter.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import kz.step.playground.enter.viewmodels.MainViewModel
import kz.step.playground.R

class MainActivity : AppCompatActivity() {

    var viewModel: MainViewModel =
        MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.bitmapLiveData.observe(this, Observer {
            image.setImageBitmap(it)
        })

        viewModel.getRequestLiveData.observe(this, Observer{
            Log.d("DATA", it)
        })
        viewModel.initiateLaunch()
        viewModel.initiateGetRequest(
            "http://api.openweathermap.org/data/2.5/weather",
                    HashMap<String, String>().apply {
                        put("q", "almaty")
                        put("appid", "1747f4277a992f22e1f3886f6c729462")
                    }
        )
//        viewModel.initiateLaunchCoroutine(
//            {
//                viewModel.initiateGetRequest(
//                    "http://api.openweathermap.org/data/2.5/weather",
//                    HashMap<String, String>().apply {
//                        put("q", "almaty")
//                        put("appid", "1747f4277a992f22e1f3886f6c729462")
//                    })
//            })
    }

    //
}