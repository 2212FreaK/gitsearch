package com.aurora.gitsearch.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aurora.gitsearch.fake.FakeSearchRepository
import com.aurora.gitsearch.fake.FakeSearchRepository.Companion.MOCKED_SEARCH_KEYS
import com.aurora.gitsearch.fake.FakeSearchRepository.Companion.MOCKED_SUBMITTED_QUERY
import com.aurora.gitsearch.helper.MainCoroutineRule
import com.aurora.gitsearch.helper.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val fakeSearchRepository = spyk(FakeSearchRepository())

    private lateinit var underTest: SearchViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        underTest = SearchViewModel(fakeSearchRepository)
    }

    @Test
    fun `should emit search keys when load search keys`() {
        //when
        underTest.loadSearchKeys()

        //then
        assertEquals(underTest.searchKeysEvent.getOrAwaitValue(), MOCKED_SEARCH_KEYS)
    }

    @Test
    fun `should emit query submit event when query text submit`() {
        //when
        underTest.onQueryTextSubmit(MOCKED_SUBMITTED_QUERY)

        //then
        assertEquals(underTest.querySubmittedEvent.getOrAwaitValue(), MOCKED_SUBMITTED_QUERY)
        coVerify { fakeSearchRepository.addSearchKey(MOCKED_SUBMITTED_QUERY) }
        confirmVerified(fakeSearchRepository)
    }
}
