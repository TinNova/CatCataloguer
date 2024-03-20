package com.tinnovakovic.catcataloguer.presentation.detail.images

import androidx.paging.PagingData
import app.cash.turbine.test
import com.tinnovakovic.catcataloguer.data.CatBreedIdInMemoryCache
import com.tinnovakovic.catcataloguer.data.CatRepo
import com.tinnovakovic.catcataloguer.data.models.local.CatBreedImage
import com.tinnovakovic.catcataloguer.presentation.detail.images.DetailImagesContract.UiEvents
import com.tinnovakovic.catcataloguer.shared.ErrorToUser
import com.tinnovakovic.catcataloguer.shared.ExceptionHandler
import com.tinnovakovic.catcataloguer.shared.TestException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DetailImagesViewModelTest {

    private val catRepo: CatRepo = mockk(relaxed = true)
    private val exceptionHandler: ExceptionHandler = mockk(relaxed = true)
    private val catBreedIdInMemoryCache: CatBreedIdInMemoryCache = mockk(relaxed = true)

    private lateinit var sut: DetailImagesViewModel

    private fun createSut() {
        sut = DetailImagesViewModel(
            catRepo,
            exceptionHandler,
            catBreedIdInMemoryCache
        )
    }

    @Test
    fun `WHEN initialise(), THEN verify uiState updates with a non emptyFlow `() =
        runTest {
            // Given
            val catBreedId = "abcd"
            val pagingDataFlow = mockk<Flow<PagingData<CatBreedImage>>>()
            every { catBreedIdInMemoryCache.cache.value } returns catBreedId
            coEvery { catRepo.observeCatBreedImagePager(catBreedId) } returns pagingDataFlow

            createSut()

            sut.uiState.test {
                // When
                sut.onUiEvent(UiEvents.Initialise)
                awaitItem() //ignore the initial hardcoded state

                // Then
                val firstUiState = awaitItem()
                Assertions.assertTrue(firstUiState.images != emptyFlow<PagingData<CatBreedImage>>())
            }
        }

    @Test
    fun `GIVEN PagingError() previously occurred, WHEN ClearErrorMessage(), THEN verify uiState updates displayError = null`() =
        runTest {
            // Given
            val errorMessage = "errorMessage"
            every { exceptionHandler.execute(TestException) } returns ErrorToUser(errorMessage)
            createSut()

            sut.uiState.test {
                // When
                sut.onUiEvent(UiEvents.PagingError(TestException))
                awaitItem() //ignore the initial hardcoded state

                // Then
                val firstUiState = awaitItem()
                assertTrue(firstUiState.displayError == errorMessage)
                assertTrue(firstUiState.errorShown)

                sut.onUiEvent(UiEvents.ClearErrorMessage)

                // Then
                val secondUiState = awaitItem()
                assertNull(secondUiState.displayError)
            }
        }

    @Test
    fun `GIVEN uiState errorShown == false, WHEN PagingError(), THEN verify uiState errorShown == true and errorMessage`() =
        runTest {
            // Given
            val errorMessage = "errorMessage"
            every { exceptionHandler.execute(TestException) } returns ErrorToUser(errorMessage)
            createSut()

            sut.uiState.test {
                // When
                sut.onUiEvent(UiEvents.PagingError(TestException))
                awaitItem() //ignore the initial hardcoded state

                // Then
                val firstUiState = awaitItem()
                assertTrue(firstUiState.displayError == errorMessage)
                assertTrue(firstUiState.errorShown)
            }
        }

}
