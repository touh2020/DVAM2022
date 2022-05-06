package eu.epfc.pocketmovie

import android.content.Context
import android.util.Log
import androidx.annotation.Nullable
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.NullPointerException
import java.lang.ref.WeakReference
import java.sql.Types.NULL
import java.util.ArrayList

class MovieRepository {

    var applicationContext : Context? = null
    var pageUrl : Int = 1
    var keyVideo : String ? = null
    var movies: MutableList<Movie>? = null
    var movieDetail : MovieDetail? = null
    private var dataUIListener: WeakReference<DataUIListener>? = null
    interface DataUIListener {
        fun updateUI()
    }
    fun onReceiveHttpResponse(response : String?, completed : Boolean,choice : Int){
        if (completed && response != null) {
            when(choice){
                0 ->{
                    // parse Movies from JSON (popular List)
                        movies = MovieRepository.instance.parseTopSMoviesResponse(response)
                }
                1->{
                        // parse Movies from JSON (get the detail movie)
                        movieDetail  = MovieRepository.instance.parseDetailMovieResponse(response)
                }
                2->{  //parse Movies from JSON get key for youtup
                        keyVideo = MovieRepository.instance.parseVideoMovieResponse(response)
                }
            }
        }
        // update the UI
        val myDataUIListener = dataUIListener
        if (myDataUIListener != null) {
            myDataUIListener.get()?.updateUI()
        }
    }

    fun observe(dataUIListener: DataUIListener) {
            this.dataUIListener = WeakReference(dataUIListener)
    }

        /**
         * Launch asynchronously a request to retrieve movies from web service. When finished,
         * updateUI() will be called on the DataUIListener instance on the UI thread.
         */
    fun fetchMovies(url: String, choice : Int) {
        val url = url
        val myapplication  = this.applicationContext
        if (myapplication != null) {
            // get  movies from the server
            val requestTask = HttpRequestTask(url,myapplication,choice)
            val requestThread = Thread(requestTask)
            requestThread.start()
        }
        else {
            val myDataUIListener = dataUIListener
            if (myDataUIListener != null){
                myDataUIListener.get()?.updateUI()
            }
        }
    }
        /**
         * Parse a json string received from Movies web service
         * @param jsonString : a json string received from the server
         * @return : a list of Movies objects parsed by the method
         */
    private fun parseTopSMoviesResponse(jsonString: String): ArrayList<Movie> {
        val newMovies = ArrayList<Movie>()
        try {
            val jsonObject = JSONObject(jsonString)
            val jsonMovies = jsonObject.getJSONArray("results")
            for (i in 0 until jsonMovies.length()) {
                val jsonMovie = jsonMovies.getJSONObject(i)
                val title = jsonMovie.getString("original_title")
                val rating = jsonMovie.getString("vote_average")
                val imagePath = jsonMovie.getString("poster_path")
                val thumbnailUrl = "https://image.tmdb.org/t/p/w500/$imagePath"
                val dateRelease = jsonMovie.getString("release_date")
                val overview  = jsonMovie.getString("overview")
                val id = jsonMovie.getInt("id")
                val newMovie = Movie(title, rating,thumbnailUrl,dateRelease,overview,id)
                newMovies.add(newMovie)
            }
        } catch (e: JSONException) {
            Log.e("MovieRepository", "can't parse json string correctly")
            e.printStackTrace()
        }
        return newMovies
    }


    private fun parseDetailMovieResponse(jsonString: String): MovieDetail? {
        var  newdetailMovie : MovieDetail ? = null
        try {
      // val jsonMovie = JSONObject(jsonString)
            val jsonMovie = JSONTokener(jsonString).nextValue() as JSONObject
            val title = jsonMovie.getString("original_title")
            val rating = jsonMovie.getString("vote_average")
            val imagePath = jsonMovie.getString("backdrop_path")
            val dateRelease = jsonMovie.getString("release_date")
            val overview  = jsonMovie.getString("overview")
            val id = jsonMovie.getInt("id")
            val  genre =jsonMovie.getJSONArray("genres").getString(0)
            val thumbnailUrl = "https://image.tmdb.org/t/p/w500/$imagePath"
            val detailMovie = MovieDetail(title, rating,thumbnailUrl,dateRelease,overview,genre,id)
            newdetailMovie = detailMovie
            return newdetailMovie

        } catch (e: JSONException) {
            Log.e("MovieRepository", "can't parse json string correctly222")
            e.printStackTrace()
            return newdetailMovie
        }

    }
    private fun parseVideoMovieResponse(jsonString: String): String? {
        var  keyVideolMovie : String ? = null
        try {
            val jsonObject = JSONObject(jsonString)
            val jsonMovies = jsonObject.getJSONArray("results")
            val jsonMovie = jsonMovies.getJSONObject(0)
            val keyVideo = jsonMovie.getString("key")
            keyVideolMovie=keyVideo
            Log.d("key video :",keyVideo)
            return keyVideolMovie
        } catch (e: JSONException) {
            Log.e("MovieRepository", "can't parse json string correctly222")
            e.printStackTrace()
            return keyVideolMovie
        }
        Log.d("the request is ","success")
    }

    companion object {
        /**
         * return the unique static instance of the singleton
         * @return an MovieManager instance
         */
        val instance = MovieRepository()
    }

}