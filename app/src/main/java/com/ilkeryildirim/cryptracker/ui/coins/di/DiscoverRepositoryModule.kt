package com.ilkeryildirim.cryptracker.ui.coins.di

import com.ilkeryildirim.cryptracker.ui.coins.CoinsDataRepository
import com.ilkeryildirim.cryptracker.ui.coins.CoinsDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DiscoverRepositoryModule {
    @Binds
    abstract fun bindDiscoverDataRepository(
            analyticsServiceImpl: CoinsDataRepositoryImpl
    ): CoinsDataRepository
}