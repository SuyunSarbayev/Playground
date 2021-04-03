package kz.step.playground.enter.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kz.step.playground.R
import kz.step.playground.utils.Constants
import kz.step.playground.enter.viewmodels.AsyncTaskViewModel

class AsyncTaskActivity : AppCompatActivity(){

    var viewModel: AsyncTaskViewModel = AsyncTaskViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.NetworkTask().execute(Constants.currency_url)
        viewModel.asyncLiveData.observe(this, Observer{
            Log.d("DATA ASYNC", it.success.orEmpty())
        })
    }
}