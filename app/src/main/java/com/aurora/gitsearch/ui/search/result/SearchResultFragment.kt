package com.aurora.gitsearch.ui.search.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.aurora.gitsearch.databinding.FragmentSearchResultBinding
import com.aurora.gitsearch.ui.extension.dp
import com.aurora.gitsearch.ui.extension.enablePagination
import com.aurora.gitsearch.ui.extension.viewBindingLifecycle
import com.aurora.gitsearch.ui.search.result.epoxy.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class SearchResultFragment : Fragment() {

    private var binding: FragmentSearchResultBinding by viewBindingLifecycle()

    private val args by navArgs<SearchResultFragmentArgs>()

    private val viewModel: SearchResultViewModel by viewModel {
        parametersOf(args.query)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)

        setUpViews()
        setUpObservers()

        return binding.root
    }

    private fun setUpViews() {
        with(binding.searchResultsRecyclerView) {
            enablePagination(lifecycleScope) {
                viewModel.loadMore()
            }

            addItemDecoration(EpoxyItemSpacingDecorator(resources.dp(8)))

            withModels {
                loadingView {
                    id("loading")
                }
            }
        }
    }

    private fun setUpObservers() {
        with(viewModel) {
            gitProjectsLiveData.observe(viewLifecycleOwner) {
                binding.searchResultsRecyclerView.withModels {
                    it.forEach {
                        searchResultItemView {
                            id(it.id)
                            model(it)
                            onItemClicked {
                                onResultItemClicked(it)
                            }
                        }
                    }

                    if (it.isEmpty()) {
                        noResultView {
                            id("noResult")
                            onRetryClicked {
                                findNavController().navigateUp()
                            }
                        }
                    }
                }
            }

            openUrlEvent.observe(viewLifecycleOwner) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(browserIntent)
            }

            networkErrorEvent.observe(viewLifecycleOwner) {
                binding.searchResultsRecyclerView.withModels {
                    noInternetView {
                        id("noInternet")
                        onRetryClicked {
                            viewModel.loadMore()
                        }
                    }
                }
            }

            genericErrorEvent.observe(viewLifecycleOwner) {
                setUpErrorView()
            }

            unknownErrorEvent.observe(viewLifecycleOwner) {
                setUpErrorView()
            }
        }
    }

    private fun setUpErrorView() {
        binding.searchResultsRecyclerView.withModels {
            errorView {
                id("unknownError")
                onRetryClicked {
                    viewModel.loadMore()
                }
            }
        }
    }
}
