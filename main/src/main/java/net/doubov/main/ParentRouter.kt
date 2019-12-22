package net.doubov.main

import net.doubov.api.models.NewsDataResponse

interface ParentRouter {
    fun goToListFragment()
    fun goToDetailFragment(newsDataResponse: NewsDataResponse)
}