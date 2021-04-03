package kz.step.playground.utils

class Constants {
    companion object{
        const val url = "http://api.openweathermap.org/data/2.5/weather"

        const val file_load_url = "https://androhub.com/demo/demo.pdf"

        const val currency_url = "https://api.exchangeratesapi.io/latest"

        const val image_url = "https://upload.wikimedia.org/wikipedia/commons/9/96/Google_web_search.png"

        val data = HashMap<String, String>().apply {
            put("q", "almaty")
            put("appid", "1747f4277a992f22e1f3886f6c729462")
        }
    }
}