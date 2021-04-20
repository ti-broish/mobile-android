package bg.dabulgaria.tibroish.presentation.comic.list.presenter

import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.interactors.IComicInteractor
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.persistence.remote.ILocationsRemoteRepo
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.comic.details.model.ComicDetailsViewData
import bg.dabulgaria.tibroish.presentation.providers.INetworkInfoProvider
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.presentation.comic.list.view.IComicListView
import bg.dabulgaria.tibroish.presentation.main.IMainNavigator
import bg.dabulgaria.tibroish.utils.ISchedulersProvider
import io.reactivex.rxjava3.core.Single

import io.reactivex.rxjava3.disposables.Disposable


import javax.inject.Inject

open class ComicListPresenter @Inject
constructor(val comicInteractor : IComicInteractor,
            val schedulersProvider : ISchedulersProvider,
            val resourceProvider : IResourceProvider,
            val networkInfoProvider : INetworkInfoProvider,
            val logger: ILogger,
            val mainRouter: IMainNavigator,
            dispHandler: IDisposableHandler,
            val locationsRemoteRepo: ILocationsRemoteRepo) : BasePresenter<IComicListView>(dispHandler), IComicListPresenter {


    override fun onComicClick(viewData: ComicDetailsViewData) {

        mainRouter.showComicDetails( viewData )
    }

    override fun loadData() {
        TODO("Not yet implemented")
    }

    override fun loadComics(refresh: Boolean ) {
        add( getLocations( refresh ) )
    }

    private fun getLocations(refresh: Boolean) : Disposable {

        return Single.fromCallable{ locationsRemoteRepo.getLocations() }
                .subscribeOn( schedulersProvider.ioScheduler())
                .observeOn( schedulersProvider.uiScheduler())
                .subscribe({

                    view?.onComicsLoaded(it)
                    view?.onLoadingStateChange(false )
                },
                        {

                            onError( throwable = it )
                        })

    }

    private fun onError( throwable : Throwable ){

        logger.e( TAG, throwable.message, throwable)

        view?.onLoadingStateChange(false )

        var resId =
                if( !networkInfoProvider.isNetworkConnected )
                    R.string.internet_connection_offline
                else
                    R.string.oops_went_wrong_try

        view?.onError(resourceProvider.getString(resId ) )
    }


    companion object {

        private val TAG = ComicListPresenter::class.simpleName
        private val LIST_LIMIT = 20L
    }
}
