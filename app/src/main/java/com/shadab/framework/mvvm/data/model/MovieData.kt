package com.shadab.framework.mvvm.data.model

import com.google.gson.annotations.SerializedName

data class MovieData(
    @SerializedName("Title")
    val title: String = "",
    @SerializedName("Year")
    val year: Int = 0,
    @SerializedName("imdbID")
    val imdbID: String = "",
    @SerializedName("Type")
    val type: String = "",
    @SerializedName("Poster")
    val poster: String = ""
)