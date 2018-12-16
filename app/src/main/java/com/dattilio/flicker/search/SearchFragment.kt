package com.dattilio.flicker.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dattilio.flicker.R
import com.dattilio.flicker.search.model.Image
import com.dattilio.flicker.search.model.SearchUiState
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_search.*
import timber.log.Timber

interface OnImageClickedListener {
    fun onImageClicked(layout: View, image: Image)
}

class SearchFragment : Fragment(), OnImageClickedListener {

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var subscription: Disposable
    private val searchRepository: SearchRepository = SearchRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = SearchAdapter(this)
        recyclerview.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        recyclerview.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        recyclerview.adapter = searchAdapter

        viewModel = ViewModelProviders.of(this, SearchViewModelFactory(searchRepository)).get(SearchViewModel::class.java)

        subscription =
                viewModel.searchStateSubject.subscribe({ uiState -> updateUi(uiState) }, { error -> Timber.e(error) })

        search_button.setOnClickListener { viewModel.search(search_text.text.toString()) }
        search_text.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search_button.performClick()
                true
            } else {
                false
            }
        }

    }

    private fun updateUi(uiState: SearchUiState) {
        when (uiState) {
            SearchUiState.Loading -> showLoading()
            SearchUiState.Empty -> showEmpty()
            is SearchUiState.Success -> showLocations(uiState.images)
            is SearchUiState.Error -> showError()
        }
    }

    private fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun showLocations(images: List<Image>) {
        searchAdapter.update(images)
        loading.visibility = GONE
        empty.visibility = GONE
        recyclerview.visibility = VISIBLE
    }

    private fun showEmpty() {
        recyclerview.visibility = GONE
        loading.visibility = GONE
        empty.visibility = VISIBLE
    }

    private fun showLoading() {
        recyclerview.visibility = GONE
        empty.visibility = GONE
        loading.visibility = VISIBLE
    }

    override fun onImageClicked(layout: View, image: Image) {
        val action = SearchFragmentDirections.openImageAction(image)
        findNavController(this).navigate(action)
    }
}
