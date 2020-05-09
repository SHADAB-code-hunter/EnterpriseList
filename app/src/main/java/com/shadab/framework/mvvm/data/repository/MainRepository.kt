package com.shadab.framework.mvvm.data.repository

import com.shadab.framework.mvvm.data.api.ApiHelper
import com.shadab.framework.mvvm.data.model.EnterpriseModel
import com.shadab.framework.mvvm.data.model.MovieDB
import io.reactivex.Single

class MainRepository(private val apiHelper: ApiHelper) {

    fun getMovieList(pageNo: Int?, search: String?): Single<MovieDB> {
        return apiHelper.getMovieList(pageNo,search)
    }


    fun getEnterpriseList(pageNo: Int?, search: String?): Single<List<EnterpriseModel>> {
        return apiHelper.getEnterpriseList(pageNo,search)
    }


}