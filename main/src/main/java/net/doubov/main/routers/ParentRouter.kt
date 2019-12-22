package net.doubov.main.routers

import net.doubov.api.models.NewsDataResponse
import net.doubov.core.Router
import net.doubov.main.ParentFragment

interface ParentRouter : Router<ParentFragment> {
    fun goToListFragment()
    fun goToDetailFragment(newsDataResponse: NewsDataResponse)
}