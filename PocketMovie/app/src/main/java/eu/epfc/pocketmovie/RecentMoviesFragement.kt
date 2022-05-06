package eu.epfc.pocketmovie
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
/**
 * A simple [Fragment] subclass.
 * Use the [RecentMoviesFragement.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecentMoviesFragement : Fragment() ,MovieRepository.DataUIListener,MovieAdapter.ListItemClickListener{
    private var first : Boolean= true  // to download the list of movies for the first time (index page =1)
    private var nextPage : Boolean = false // to download the list of movies when  index page >1
    private lateinit var recyclerView: RecyclerView
    private var movieDetail : Boolean = true // control bit detail of movie
    private var rotateScreen : Int =0
    private lateinit var moviesAdapter : MovieAdapter
    private var urlMovies :String  = "https://api.themoviedb.org/3/movie/popular?api_key=ea2dcee690e0af8bb04f37aa35b75075&language=en-US&page="
    private var state : Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mycontext: Context?= context
        return inflater.inflate(R.layout.fragment_recentmovies, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MovieRepository.instance.applicationContext = view.context
        if (view != null) {
            recyclerView = view.findViewById(R.id.recycler_view)
            moviesAdapter = MovieAdapter(this)
            recyclerView.adapter = moviesAdapter
            val myContext : Context? = context
            if (myContext != null) {
                val layoutManager = LinearLayoutManager(myContext)
                recyclerView.layoutManager = layoutManager
                MovieRepository.instance.observe(this)
            }
        }
    }
     override fun onStart() {
        super.onStart()
         /** download the data movies for the first time when index page =1 */
        if (first) {
            first = false
            val page = MovieRepository.instance.pageUrl  //  default page index is 1
            MovieRepository.instance.fetchMovies(urlMovies + page, 0) //http request with page index = 1
        }
    }

    override fun onPause() {
        super.onPause()
        state = recyclerView.layoutManager?.onSaveInstanceState()!!
    }

    override fun onResume() {
        super.onResume()
        if(state != null) {
            recyclerView.layoutManager?.onRestoreInstanceState(state)
        }
    }
    override fun updateUI() {
       var movieList = MovieRepository.instance.movies
       val pageUrl = MovieRepository.instance.pageUrl
           /**  populate RecyclerView with data from server when page index =1 */
       if (movieList != null && pageUrl==1 && nextPage==false ) {
           moviesAdapter.movieData = movieList
           moviesAdapter.notifyDataSetChanged()
           Log.d("on start moviadapter update first", first.toString())
       }
       /**  populate RecyclerView with data from server when page index > 1  and  "Next Page" is clicked  */
        else if (nextPage ==true && movieList!=null) {
       nextPage = false
       val postionStart = moviesAdapter.movieData?.size
       Log.d("la taille avant  est :",postionStart.toString())
       moviesAdapter.movieData?.addAll(movieList)
       moviesAdapter.notifyItemRangeInserted(postionStart!!,movieList.size)
       Log.d("la taille apres  est :",moviesAdapter.movieData?.size.toString())
       Log.d("on start moviadapter update state", movieDetail.toString())
        }
         /* else if (rotateScreen ==1) {
               rotateScreen=0
               moviesAdapter.movieData = movieList
               moviesAdapter.notifyDataSetChanged()
               Log.d("rotatescreen ok","okkkk")
           }*/
    }
    override fun onListItemClick(clickedItemIndex: Int) {
        /**increment the page index  when you click on item "Next page" */
        if (clickedItemIndex == moviesAdapter.itemCountNew -1) {
            nextPage = true
            Log.d("itemcountNewvoir",moviesAdapter.itemCountNew.toString())
            MovieRepository.instance.pageUrl++
            val page= MovieRepository.instance.pageUrl
            /** download the data movies for the first time when index page > 1 */
            MovieRepository.instance.fetchMovies(urlMovies+page,0)
            MovieRepository.instance.observe(this)
        }
        /** display the MovieDetailActivity  when you click on movie item */
        else  {
            movieDetail = false
            var selectedMovieId = moviesAdapter.movieData?.get(clickedItemIndex)?.movieId /** we get the Id of the movie */
            if (selectedMovieId != 0){
                val myContext : Context? = context
                val detailIntent = Intent(myContext,MovieDetailActivity::class.java)
                val movieID = detailIntent.putExtra("MovieId",selectedMovieId) //
                startActivity(detailIntent) /** we start the MovieDetailActivity */
            }
        }
    }
}


