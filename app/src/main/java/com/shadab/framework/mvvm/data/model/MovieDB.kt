package com.shadab.framework.mvvm.data.model

import com.google.gson.annotations.SerializedName

data class MovieDB(
    @SerializedName("Search")
    val movieList: List<MovieData> = arrayListOf()
)