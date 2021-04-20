package bg.dabulgaria.tibroish.presentation.comic.details.presenter

import android.util.Log
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.interactors.IComicInteractor
import bg.dabulgaria.tibroish.domain.models.Comic
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.comic.details.model.ComicDetailsViewData
import bg.dabulgaria.tibroish.presentation.comic.details.view.IComicDetailsView
import bg.dabulgaria.tibroish.presentation.providers.INetworkInfoProvider
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.utils.ISchedulersProvider
import io.reactivex.rxjava3.disposables.Disposable


import javax.inject.Inject

class ComicDetailsPresenter @Inject
constructor(val comicInteractor : IComicInteractor,
            val schedulersProvider : ISchedulersProvider,
            val resourceProvider : IResourceProvider,
            val networkInfoProvider : INetworkInfoProvider,
            dispHandler: IDisposableHandler) : BasePresenter<IComicDetailsView>(dispHandler),
        IComicDetailsPresenter,
        IDisposableHandler by dispHandler{

    var comicDetailsView : IComicDetailsView ? = null

    override fun onViewCreated(view: IComicDetailsView) {
        this.comicDetailsView = view
    }

    override fun onViewDisposed() {
        this.comicDetailsView = null
    }

    override fun loadData() {
    }

    override fun loadComicDetails(refresh: Boolean, id:Long ) {

        add(getComicDetails(refresh, id ) )
    }

    private fun getComicDetails(refresh: Boolean, id:Long ) : Disposable {

        return comicInteractor.getComic( refresh, id ).map {

                return@map createFrom( it )
            }
                .subscribeOn( schedulersProvider.ioScheduler())
                .observeOn( schedulersProvider.uiScheduler())
                .subscribe({

                    comicDetailsView?.onDetailsLoaded(it )
                    comicDetailsView?.onLoadingStateChange(false )
                },
                        {

                            onError( throwable = it )
                        })

    }

    private fun onError( throwable : Throwable ){

        Log.e( TAG, throwable.message, throwable)

        comicDetailsView?.onLoadingStateChange(false )

        var resId =
                if( !networkInfoProvider.isNetworkConnected )
                    R.string.internet_connection_offline
                else
                    R.string.oops_went_wrong_try

        comicDetailsView?.onError(resourceProvider.getString(resId ) )
    }

    private fun createFrom(comic : Comic) : ComicDetailsViewData{

        return ComicDetailsViewData(comic.id,
                                    comic.title,
                                    comic.thumbnailUrl,
                                    comic.description)
    }

    companion object {

        private val TAG = ComicDetailsPresenter::class.simpleName
    }
}
