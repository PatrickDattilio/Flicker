package com.dattilio.flicker.search.model

data class FlickrApiResponse(val photos: Photos)
data class Photos(
    val page: Int,
    val pages: Int,
    val perPage: Int,
    val total: String,
    val photo: List<Photo>
)

data class Photo(
    val id: String,
    val secret: String,
    val server: String,
    val farm: String,
    val title: String
)