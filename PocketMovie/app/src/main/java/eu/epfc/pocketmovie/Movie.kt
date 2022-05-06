package eu.epfc.pocketmovie

import java.io.Serializable
class Movie (
                val movieTitle : String ,
                val movieRating : String,
                val movieImageUrl : String,
                val movieReleaseDate : String,
                val movieOverview : String ,
                val movieId     : Int
                ) : Serializable{
}
