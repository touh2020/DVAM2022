package eu.epfc.pocketmovie

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailFragement.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieDetailFragement : Fragment()  {
   var movie : MovieDetail? = null
   set(value) {
       field = value
       loadUI()
   }
   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
    // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detail_fragement, container, false)
   }

    override fun onStart() {
        super.onStart()
        loadUI()
    }

    private fun loadUI() {
        var selectMovie = this.movie
        val fragmentView = view
        if (selectMovie!= null && fragmentView != null) {
            Log.d("movie overview :",selectMovie.movieOverview )
            val movieTitle: TextView = fragmentView.findViewById(R.id.movieTitle)
            movieTitle.text = selectMovie.movieTitle.toString()
            val movieDate: TextView = fragmentView.findViewById(R.id.MovieDate)
            movieDate.text = selectMovie.movieReleaseDate.toString()
            val movieRating: TextView = fragmentView.findViewById(R.id.MovieRating)
            movieRating.text = selectMovie.movieRating.toString()
            val movieOverview: TextView = fragmentView.findViewById(R.id.movieOverview)
            movieOverview.text = selectMovie.movieOverview.toString()
            val movieImage: ImageView = fragmentView.findViewById(R.id.movieImage)
            Picasso.get().load(selectMovie.movieImageUrl).into(movieImage)
        }
    }
}