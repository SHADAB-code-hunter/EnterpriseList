package com.shadab.framework.mvvm.data.api

import com.shadab.framework.mvvm.data.model.MovieDB
import com.rx2androidnetworking.Rx2AndroidNetworking
import com.shadab.framework.mvvm.data.model.EnterpriseModel
import io.reactivex.Single

class ApiServiceImpl : ApiService {

    override fun getMovieList(pageNo: Int?, search: String?): Single<MovieDB> {
        val moviewListDb: Single<MovieDB> = Rx2AndroidNetworking.get("http://www.omdbapi.com/?type=movie&apikey=5d81e1ce&page=$pageNo&s=$search")
            .build()
            .getObjectSingle(MovieDB::class.java)

        return moviewListDb
    }
    override fun getEnterpriseList(pageNo: Int?, search: String?): Single<List<EnterpriseModel>> {
        return  Rx2AndroidNetworking.get("https://www.enterprisesmail.com/json/api.json")
            .build()
            .getObjectListSingle(EnterpriseModel::class.java)

    }

   /* override fun getUsers(): Single<List<EnterpriseModel>> {
        return Rx2AndroidNetworking.get("https://5e510330f2c0d300147c034c.mockapi.io/users")
            .build()
            .getObjectListSingle(EnterpriseModel::class.java)
    }
*/

}