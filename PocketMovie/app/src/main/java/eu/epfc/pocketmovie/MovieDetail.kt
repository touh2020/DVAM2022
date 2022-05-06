package eu.epfc.pocketmovie

import java.io.Serializable

class MovieDetail(
    val movieTitle : String ,
    val movieRating : String,
    val movieImageUrl : String,
    val movieReleaseDate : String,
    val movieOverview : String ,
    val movieGenre : String,
    val movieId : Int
): Serializable {

}