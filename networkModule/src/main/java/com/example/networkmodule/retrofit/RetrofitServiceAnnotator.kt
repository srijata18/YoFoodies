package com.example.networkmodule.retrofit

import com.example.networkmodule.data.UserDetails
import retrofit2.Call
import retrofit2.http.*


interface RetrofitServiceAnnotator {
    @PUT
    fun createUser(@Body user: UserDetails?, @Url url : String): Call<UserDetails>

    @GET
    fun getUser( @Url url : String): Call<UserDetails?>

}