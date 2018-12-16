package com.dattilio.flicker.search

import com.dattilio.flicker.RxTestRule
import com.dattilio.flicker.search.model.*
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import org.junit.ClassRule
import org.junit.Test

class SearchViewModelTest {
    companion object {
        @ClassRule
        @JvmField
        val rxTestRule = RxTestRule()
    }

    @Test
    fun search() {
        val mockSearchRepository = mock<SearchRepository> {}
        val searchViewModel = SearchViewModel(mockSearchRepository)
        val testObserver = searchViewModel.searchStateSubject.test()
        testObserver.assertValue(SearchUiState.Empty)
    }

    @Test
    fun searchLoadingThenEmpty() {
        val query = ""
        val apiResponse = FlickrApiResponse(Photos(0, 0, 0, "", emptyList()))

        val mockSearchRepository = mock<SearchRepository> {
            on { imagesBySearchQuery(query) } doReturn Single.just(apiResponse)
        }
        val searchViewModel = SearchViewModel(mockSearchRepository)
        val testObserver = searchViewModel.searchStateSubject.test()
        searchViewModel.search(query)
        testObserver.assertValues(SearchUiState.Empty, SearchUiState.Loading, SearchUiState.Empty)
    }

    @Test
    fun setupThenError() {
        val query = ""
        val mockSearchRepository = mock<SearchRepository> {
            on { imagesBySearchQuery(query) } doReturn Single.error(Throwable("test"))
        }
        val searchViewModel = SearchViewModel(mockSearchRepository)
        val testObserver = searchViewModel.searchStateSubject.test()
        searchViewModel.search(query)
        testObserver.assertValues(SearchUiState.Empty, SearchUiState.Loading, SearchUiState.Error("Oops"))
    }

    @Test
    fun setupThenSuccess() {
        val query = ""
        val photo = Photo("123", "secrets", "servers", "farms", "testTitle")
        val list = listOf(photo)
        val imageList = listOf(Image("123", "testTitle", "secrets", "servers", "farms"))
        val apiResponse = FlickrApiResponse(Photos(0, 0, 0, "", list))

        val mockSearchRepository = mock<SearchRepository> {
            on { imagesBySearchQuery(query) } doReturn Single.just(apiResponse)
        }
        val searchViewModel = SearchViewModel(mockSearchRepository)
        val testObserver = searchViewModel.searchStateSubject.test()
        searchViewModel.search(query)
        testObserver.assertValues(SearchUiState.Empty, SearchUiState.Loading, SearchUiState.Success(imageList))

    }
}