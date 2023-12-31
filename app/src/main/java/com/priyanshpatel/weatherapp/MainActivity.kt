package com.priyanshpatel.weatherapp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.airbnb.lottie.L.TAG
import com.google.android.material.search.SearchBar
import com.priyanshpatel.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//878af76accdcb3707927bd468c3637ef
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("Gandhinagar")
        SearchCity()
    }

    private fun SearchCity(){
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

    }

    private fun fetchWeatherData(Cityname: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response =
            retrofit.getWeatherData("Gandhinagar", "878af76accdcb3707927bd468c3637ef", "metric")
        response.enqueue(/* callback = */ object : Callback<WeatherApp> {
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {

                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise
                    val sunSet = responseBody.sys.sunset
                    val seaLevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min

                    binding.temp.text = "$temperature °C"
                    binding.weather.text = condition
                    binding.maxTemp.text = "Max Temp: $maxTemp °C"
                    binding.minTemp.text = "Min Temp: $minTemp °C"
                    binding.humidity.text = "$humidity %"
                    binding.windSpeed.text = "windSpeed m/s"
                    binding.sunRise.text = "$sunRise"
                    binding.sunset.text = "$sunSet"
                    binding.Sea.text = "$seaLevel hPa"
                    binding.condition.text = condition
                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = date()
                    binding.CityName.text = "$Cityname"
                    // Log.d("TAG", "onResponse: $temperature")
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {

            }
        })
    }

        private fun changeImagesAccordingToWeatherCondition(comditions: String)
        {
            when(comditions){
                "Clear Sky","Sunny","Clear" -> {
                    binding.root.setBackgroundResource(R.drawable.sunny_background)
                    binding.lottieAnimationView3.setAnimation(R.raw.sun)
                }
                "Partly Clouds","Clouds","Overcast","Mist","Foggy" ->{
                    binding.root.setBackgroundResource(R.drawable.colud_background)
                    binding.lottieAnimationView3.setAnimation(R.raw.cloud)
                }

                "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain" ->{
                    binding.root.setBackgroundResource(R.drawable.rain_background)
                    binding.lottieAnimationView3.setAnimation(R.raw.cloud)
                }

                "Light Rain","Moderate Snow","Heavy Snow","Blizzard" ->{
                    binding.root.setBackgroundResource(R.drawable.colud_background)
                    binding.lottieAnimationView3.setAnimation(R.raw.cloud)
                }
            }
            binding.lottieAnimationView3.playAnimation()
        }


    private fun date(): String {
        val sdf = SimpleDateFormat("dd:MMMM:yyyy", Locale.getDefault())
        return sdf.format((Date()))

    }
    fun dayName(timeStamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }

}

private fun SearchBar.setOnQueryTextListener(onQueryTextListener: SearchView.OnQueryTextListener) {

}

