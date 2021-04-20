package bg.dabulgaria.tibroish.presentation.comic.details.di

import bg.dabulgaria.tibroish.presentation.comic.details.presenter.ComicDetailsPresenter
import bg.dabulgaria.tibroish.presentation.comic.details.presenter.IComicDetailsPresenter
import bg.dabulgaria.tibroish.presentation.comic.details.view.ComicDetailsFragment
import bg.dabulgaria.tibroish.presentation.comic.details.view.IComicDetailsView
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ComicDetailsFragmentModule {

    @ContributesAndroidInjector
    fun bindDetailsFragment(): ComicDetailsFragment

    @Binds
    fun bindsDetailsView( detailsFragment: ComicDetailsFragment): IComicDetailsView

    @Binds
    fun bindDetailsPresenter( detailsPresenter: ComicDetailsPresenter ): IComicDetailsPresenter

}