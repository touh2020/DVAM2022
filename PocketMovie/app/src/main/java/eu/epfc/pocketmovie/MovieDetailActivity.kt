package eu.epfc.pocketmovie

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MovieDetailActivity : AppCompatActivity(),MovieRepository.DataUIListener {
    private val urlDetailMovie = "https://api.themoviedb.org/3/movie/"
    private val keyString = "?api_key=ea2dcee690e0af8bb04f37aa35b75075" // code for access to the server
    private  var movieId : Int = 0
    var youtubeId : String ?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
    /** get the intent used to create this Activity and  extract a MovieId from it */
        movieId= intent.getIntExtra("MovieId",0)
        if (movieId != 0) {
            val videoUrl = urlDetailMovie+movieId+"/videos"+keyString // the url of movieDetail
            /** the first fetch is for dwonload the details of movie and the second is for to dowload the Id for Youtup */
            MovieRepository.instance.fetchMovies(urlDetailMovie + movieId + keyString, 1)
            MovieRepository.instance.fetchMovies(videoUrl,2)
            MovieRepository.instance.observe(this)
        }
    }
    /** play the video */
    fun onPlayVideo(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$youtubeId"))
        startActivity(intent)
    }

    /** update the fragement */
    override fun updateUI() {
        val movieDetail = MovieRepository.instance.movieDetail
        val detailFragment = supportFragmentManager.findFragmentById(R.id.fragment_movie_detail) as MovieDetailFragement?
        // send the movie reference to the fragment
        detailFragment?.movie = movieDetail
        youtubeId = MovieRepository.instance.keyVideo
    }
}