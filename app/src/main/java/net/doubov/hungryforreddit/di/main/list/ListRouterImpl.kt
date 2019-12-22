package net.doubov.hungryforreddit.di.main.list

import net.doubov.main.ListFragment
import net.doubov.main.routers.ListRouter
import javax.inject.Inject

@MainListScope
class ListRouterImpl @Inject constructor(
    override val fragment: ListFragment
) : ListRouter