package eu.epfc.pocketmovie

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MovieAdapter(listItemClickListener : ListItemClickListener) :RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    interface ListItemClickListener {
        fun onListItemClick(clickedItemIndex: Int)
    }
    // listener that will be called when an item is clicked

    val listItemClickListener : ListItemClickListener = listItemClickListener
    var movieData : MutableList<Movie>? = null // list  of movies
    var itemCountNew : Int =0

// refrech recyclerview
    fun setMovies(movieList: List<Movie>) {
        val  movieList= movieData
        notifyDataSetChanged()
    }
    inner  class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        init{
    // define the object to call when the view will be clicked
          itemView.setOnClickListener(this)
        }
        /**
         * Callback method called when the view is clicked
         */
        override fun onClick(view: View?) {
        val clickedPosition = this.adapterPosition
        this@MovieAdapter.listItemClickListener.onListItemClick(clickedPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        // inflate the layout item_movie in a view
        val movieView = layoutInflater.inflate(R.layout.item_movie, parent, false)
        // create a new ViewHolder with the view movieView
        return MovieViewHolder(movieView)
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        val myMovietData = movieData
        /** DataBinding when the position of the last item is < myMovietData.size  */
        if ( myMovietData != null && position < myMovietData.size) {
            val movie =myMovietData[position]
            val itemViewGroup = holder.itemView as ViewGroup
            val movieTitleTextView  : TextView = itemViewGroup.findViewById(R.id.text_title)
            val movieRatingTextView : TextView  = itemViewGroup.findViewById(R.id.text_rating)
            val movieImageView :ImageView = itemViewGroup.findViewById(R.id.image_movie)
            Picasso.get().load(movie.movieImageUrl).into(movieImageView)
            movieTitleTextView.text=movie.movieTitle
            movieRatingTextView.text="Rating :" + movie.movieRating
            val textNextPage : TextView = itemViewGroup.findViewById(R.id.textNextPage)
            textNextPage.visibility = View.INVISIBLE
        }
        /** the last Itemview of the Recyclerview  is replaced by "Next page .."
         * and the other widgets are hidden */
        else if ( myMovietData != null  && position == myMovietData.size  && myMovietData.size >19 ){
            val itemViewGroup = holder.itemView as ViewGroup
            val textNextPage : TextView = itemViewGroup.findViewById(R.id.textNextPage)
            val movieTitleTextView  : TextView = itemViewGroup.findViewById(R.id.text_title)
            val movieRatingTextView : TextView  = itemViewGroup.findViewById(R.id.text_rating)
            val movieImageView :ImageView = itemViewGroup.findViewById(R.id.image_movie)
            movieImageView.setImageDrawable(null)
            movieTitleTextView.text = null
            movieRatingTextView.text=null
            textNextPage.text = "Next page ..."
            textNextPage.visibility = View.VISIBLE
       }
    }
    override fun getItemCount(): Int {
        val myMovietData = movieData
        if ( myMovietData != null) {
            itemCountNew = myMovietData.size + 1  // +1 for itemeview "Next page"
            return itemCountNew
        }
        else {
            return 0
        }
    }
}