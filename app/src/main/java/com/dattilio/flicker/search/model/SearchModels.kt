package com.dattilio.flicker.search.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

sealed class SearchUiState {
    object Loading : SearchUiState()
    object Empty : SearchUiState()
    data class Success(val images: List<Image>) : SearchUiState()
    data class Error(val error: String) : SearchUiState()
}

@Parcelize
@JsonClass(generateAdapter = true)
data class Image(
    val id: String,
    val title: String,
    val secret: String,
    val server: String,
    val farm: String
) : Parcelable {

    fun gridUrl(): String {
        return "https://farm$farm.staticflickr.com/$server/$id" + "_$secret" + "_q.jpg"
    }

    fun fullUrl(): String {
        return "https://farm$farm.staticflickr.com/$server/$id" + "_$secret.jpg"
    }

    override fun toString(): String {
        return title
    }
}