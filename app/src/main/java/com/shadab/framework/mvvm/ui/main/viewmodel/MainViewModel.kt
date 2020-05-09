package com.shadab.framework.mvvm.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shadab.framework.mvvm.data.model.EnterpriseModel
import com.shadab.framework.mvvm.data.model.MovieDB
import com.shadab.framework.mvvm.data.repository.MainRepository
import com.shadab.framework.mvvm.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val movieListLiveData = MutableLiveData<Resource<MovieDB>>()
    private val enterpriseListLiveData = MutableLiveData<Resource<List<EnterpriseModel>>>()
    private val compositeDisposable = CompositeDisposable()

    fun fetchMovieList(pageNo: Int, search: String?) {
        movieListLiveData.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getMovieList(pageNo, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ movieList ->
                    movieListLiveData.postValue(Resource.success(movieList))
                }, { throwable ->
                    movieListLiveData.postValue(Resource.error("Something Went Wrong", null))
                })
        )
    }

    fun fetchEnterpriseList(pageNo: Int, search: String?) {
        enterpriseListLiveData.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getEnterpriseList(pageNo, search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ enterpriseList ->
                    enterpriseListLiveData.postValue(Resource.success(enterpriseList))
                }, { throwable ->
                    enterpriseListLiveData.postValue(Resource.error("Something Went Wrong", null))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getMovieList(): LiveData<Resource<MovieDB>> {
        return movieListLiveData
    }

    fun getEnterpriseList(): LiveData<Resource<List<EnterpriseModel>>> {
        return enterpriseListLiveData
    }

}