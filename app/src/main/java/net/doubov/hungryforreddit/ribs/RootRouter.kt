package net.doubov.hungryforreddit.ribs

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link RootRibBuilder.RootRibScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class RootRouter(
    view: RootView,
    interactor: RootInteractor,
    component: RootBuilder.Component
) : ViewRouter<RootView, RootInteractor, RootBuilder.Component>(view, interactor, component)
