package pl.qbasso.omisechallenge.api

import io.reactivex.Observable
import pl.qbasso.omisechallenge.model.Charity
import pl.qbasso.omisechallenge.model.Donation
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChallengeApi {

    @GET("/")
    fun getCharities() : Observable<Response<List<Charity>>>

    @POST("donate/")
    fun donate(@Body donation : Donation) : Observable<Response<Unit>>
}