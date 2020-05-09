package com.shadab.framework.mvvm.data.api

class ApiHelper(private val apiService: ApiService) {

    fun getMovieList(pageNo: Int?, search: String?) = apiService.getMovieList(pageNo,search)
    fun getEnterpriseList(pageNo: Int?, search: String?) = apiService.getEnterpriseList(pageNo,search)

}