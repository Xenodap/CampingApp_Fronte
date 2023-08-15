package com.camp.campingapp.retrofit

import com.camp.campingapp.model.NaverReverseGeocodeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverNetworkService {
    //    https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=128.12345,37.98776&output=json

    //    @GET("/naveropenapi.apigw.ntruss.com/")
    @GET("/map-reversegeocode/v2/gc")
    @Headers(
        "X-NCP-APIGW-API-KEY-ID: 426fftzyvu",
        "X-NCP-APIGW-API-KEY: EbRpqWC1Aehm3Nv2UmftiLQNqLgIIUGjyRNFaVPV"
    )
    fun reverseGeocode(
        @Query("coords") coords: String?,
        @Query("output") output: String = "json"

    ): Call<NaverReverseGeocodeResponse>

}








