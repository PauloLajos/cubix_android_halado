package hu.paulolajos.themoviedbdemo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.paulolajos.themoviedbdemo.data.Movies
import hu.paulolajos.themoviedbdemo.data.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private var movieLiveData = MutableLiveData<List<Result>>()

    fun getPopularMovies() {
        RetrofitInstance.api.getPopularMovies(ApiKey.tmdbApiKey).enqueue(object  :
            Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                if (response.body()!=null){
                    movieLiveData.value = response.body()!!.results
                }
                else{
                    return
                }
            }
            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.d("TAG",t.message.toString())
            }
        })
    }

    fun observeMovieLiveData() : LiveData<List<Result>> {
        return movieLiveData
    }
}