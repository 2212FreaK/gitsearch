package com.aurora.gitsearch.ui.search

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aurora.gitsearch.R
import com.aurora.gitsearch.databinding.FragmentSearchBinding
import com.aurora.gitsearch.ui.extension.viewBindingLifecycle
import com.aurora.gitsearch.ui.search.epoxy.searchHistoryItemView
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var binding: FragmentSearchBinding by viewBindingLifecycle()
    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        setUpViews()
        setUpObservers()

        return binding.root
    }

    private fun setUpViews() {
        with(binding.keySearchView) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query?.isNotBlank() == true) {
                        viewModel.onQueryTextSubmit(query)
                    }
                    return true

                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })

            findViewById<TextView>(R.id.search_src_text).apply {
                filters = arrayOf(InputFilter.LengthFilter(25))
            }

            setOnQueryTextFocusChangeListener { _, hasFocus ->
                binding.historyRecyclerView.isVisible = hasFocus
            }
        }
    }

    private fun setUpObservers() {
        with(viewModel) {
            loadSearchKeys()

            searchKeysEvent.observe(viewLifecycleOwner) {
                binding.historyRecyclerView.withModels {
                    it.forEach {
                        searchHistoryItemView {
                            id(it)
                            name(it)
                            onItemClicked {
                                binding.keySearchView.setQuery(it, true)
                            }
                        }
                    }
                }
            }

            querySubmittedEvent.observe(viewLifecycleOwner) {
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(it)
                )
            }
        }
    }
}
