package net.doubov.hungryforreddit.di.main.detail

import net.doubov.main.DetailFragment
import net.doubov.main.routers.DetailRouter
import javax.inject.Inject

@MainDetailScope
class DetailRouterImpl @Inject constructor(
    override val fragment: DetailFragment
) : DetailRouter