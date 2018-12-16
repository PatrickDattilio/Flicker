package com.dattilio.flicker.search

import com.dattilio.flicker.search.model.FlickrApiResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class SearchRepository {

    private val imageService = Retrofit.Builder()
        .baseUrl("https://api.flickr.com/services/rest/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(SearchService::class.java)

    interface SearchService {
        @GET("?method=flickr.photos.search&api_key=1508443e49213ff84d566777dc211f2a&format=json&per_page=25&nojsoncallback=1")
        fun imagesBySearchQuery(@Query("text") query: String): Single<FlickrApiResponse>
    }
    fun imagesBySearchQuery(query:String): Single<FlickrApiResponse>{
        return imageService.imagesBySearchQuery(query)
    }
}
