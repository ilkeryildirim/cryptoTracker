package com.ilkeryildirim.cryptracker.data.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ilkeryildirim.cryptracker.api.CryptrackerApiResult
import com.ilkeryildirim.cryptracker.api.model.coin.CoinItem
import com.ilkeryildirim.cryptracker.api.model.coin.DiscoverModel
import com.ilkeryildirim.cryptracker.ui.coins.CoinsDataRepository
import com.ilkeryildirim.cryptracker.ui.coins.CoinsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
class CoinsFragmentViewModelTest {

    private lateinit var viewModel: CoinsViewModel

    @MockK
    private lateinit var coinsRepository: CoinsDataRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = CoinsViewModel(coinsRepository)
    }

    @Test
    fun `when get coinsList`() =
        mainCoroutineRule.runBlockingTest {
            val res = CryptrackerApiResult.Success(mockk<ArrayList<CoinItem>>())
            coEvery { coinsRepository.getCoinList() } returns res
            viewModel.getCoinList()
            assertEquals(viewModel.uiState.value, res)
        }
}