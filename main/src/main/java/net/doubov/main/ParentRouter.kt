package net.doubov.main

import net.doubov.api.models.NewsDataResponse

interface ParentRouter {
    val fragment: ParentFragment
    fun goToListFragment()
    fun goToDetailFragment(newsDataResponse: NewsDataResponse)
}