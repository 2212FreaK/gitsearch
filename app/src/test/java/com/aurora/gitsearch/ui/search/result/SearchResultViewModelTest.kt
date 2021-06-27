package com.aurora.gitsearch.ui.search.result

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aurora.gitsearch.fake.FakeSearchRepository
import com.aurora.gitsearch.fake.FakeSearchRepository.Companion.MOCKED_ERROR
import com.aurora.gitsearch.fake.FakeSearchRepository.Companion.MOCKED_ERROR_QUERY
import com.aurora.gitsearch.fake.FakeSearchRepository.Companion.MOCKED_GIT_PROJECTS_1
import com.aurora.gitsearch.fake.FakeSearchRepository.Companion.MOCKED_GIT_PROJECTS_2
import com.aurora.gitsearch.fake.FakeSearchRepository.Companion.MOCKED_GIT_PROJECTS_3
import com.aurora.gitsearch.fake.FakeSearchRepository.Companion.MOCKED_NO_INTERNET_QUERY
import com.aurora.gitsearch.fake.FakeSearchRepository.Companion.MOCKED_SUCCESS_QUERY
import com.aurora.gitsearch.fake.toGitProject
import com.aurora.gitsearch.helper.MainCoroutineRule
import com.aurora.gitsearch.helper.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchResultViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val fakeSearchRepository = spyk(FakeSearchRepository())

    private lateinit var underTest: SearchResultViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        underTest = SearchResultViewModel(MOCKED_SUCCESS_QUERY, fakeSearchRepository)
    }

    @Test
    fun `should emit first page of git projects on init`() {
        //then
        assertEquals(
            underTest.gitProjectsLiveData.getOrAwaitValue(),
            MOCKED_GIT_PROJECTS_1
        )
    }

    @Test
    fun `should emit second page of git projects on load more`() {
        //when
        underTest.loadMore()

        //then
        assertEquals(
            underTest.gitProjectsLiveData.getOrAwaitValue(),
            MOCKED_GIT_PROJECTS_1 + MOCKED_GIT_PROJECTS_2
        )
        coVerifySequence {
            fakeSearchRepository.search(MOCKED_SUCCESS_QUERY, 1)
            fakeSearchRepository.search(MOCKED_SUCCESS_QUERY, 2)
        }
        confirmVerified(fakeSearchRepository)
    }

    @Test
    fun `should emit last page of git projects on load more`() {
        //when
        underTest.loadMore()
        underTest.loadMore()
        underTest.loadMore()

        //then
        assertEquals(
            underTest.gitProjectsLiveData.getOrAwaitValue(),
            MOCKED_GIT_PROJECTS_1 + MOCKED_GIT_PROJECTS_2 + MOCKED_GIT_PROJECTS_3
        )
        coVerifySequence {
            fakeSearchRepository.search(MOCKED_SUCCESS_QUERY, 1)
            fakeSearchRepository.search(MOCKED_SUCCESS_QUERY, 2)
            fakeSearchRepository.search(MOCKED_SUCCESS_QUERY, 3)
        }
        confirmVerified(fakeSearchRepository)
    }

    @Test
    fun `should emit no internet even when no internet`() {
        //given
        underTest = SearchResultViewModel(MOCKED_NO_INTERNET_QUERY, fakeSearchRepository)

        //then
        assertEquals(
            underTest.networkErrorEvent.getOrAwaitValue(),
            Unit
        )
    }

    @Test
    fun `should emit unknown error even when unexpected error`() {
        //given
        underTest = SearchResultViewModel("unknown", fakeSearchRepository)

        //then
        assertEquals(
            underTest.unknownErrorEvent.getOrAwaitValue(),
            Unit
        )
    }

    @Test
    fun `should emit generic error even when expected error`() {
        //given
        underTest = SearchResultViewModel(MOCKED_ERROR_QUERY, fakeSearchRepository)

        //then
        assertEquals(
            underTest.genericErrorEvent.getOrAwaitValue(),
            MOCKED_ERROR.message
        )
    }

    @Test
    fun `should emit open url even on search item clicked`() {
        //when
        val data = "1".toGitProject()
        underTest.onResultItemClicked(data)

        //then
        assertEquals(
            underTest.openUrlEvent.getOrAwaitValue(),
            data.htmlUrl
        )
    }
}
