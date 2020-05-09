package com.shadab.framework.mvvm.data.api

import com.shadab.framework.mvvm.data.model.EnterpriseModel
import com.shadab.framework.mvvm.data.model.MovieDB
import io.reactivex.Single

interface ApiService {

    fun getMovieList(pageNo: Int?, search: String?): Single<MovieDB>
    fun getEnterpriseList(pageNo: Int?, search: String?): Single<List<EnterpriseModel>>

}