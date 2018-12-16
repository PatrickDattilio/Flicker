package com.dattilio.flicker.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dattilio.flicker.search.model.Image
import com.dattilio.flicker.search.model.SearchUiState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class SearchViewModelFactory(private val searchRepository: SearchRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(searchRepository) as T
    }
}

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    val searchStateSubject: BehaviorSubject<SearchUiState> =
        BehaviorSubject.createDefault(SearchUiState.Empty)
    private lateinit var subscription: Disposable
    fun search(query: String) {
        //Request images on the IO thread, map them from the response type to a list of Images.
        searchStateSubject.onNext(SearchUiState.Loading)
        subscription = searchRepository.imagesBySearchQuery(query)
            .map { response ->
                response.photos.photo
                    .map { photo -> Image(photo.id, photo.title, photo.secret, photo.server, photo.farm) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ imageList ->
                handleSuccess(imageList)
            }, { error ->
                handleError(error)
            })
    }

    private fun handleError(error: Throwable) {
        Timber.e(error)
        searchStateSubject.onNext(SearchUiState.Error("Oops"))
    }

    private fun handleSuccess(images: List<Image>) {
        if (images.isEmpty()) {
            searchStateSubject.onNext(SearchUiState.Empty)
        } else {
            searchStateSubject.onNext(SearchUiState.Success(images))
        }
    }

    override fun onCleared() {
        subscription.dispose()
        super.onCleared()
    }
}
