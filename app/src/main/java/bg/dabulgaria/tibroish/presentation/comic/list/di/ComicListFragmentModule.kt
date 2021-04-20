package bg.dabulgaria.tibroish.presentation.comic.list.di

import bg.dabulgaria.tibroish.presentation.comic.list.presenter.ComicListPresenter
import bg.dabulgaria.tibroish.presentation.comic.list.presenter.IComicListPresenter
import bg.dabulgaria.tibroish.presentation.comic.list.view.ComicListFragment
import bg.dabulgaria.tibroish.presentation.comic.list.view.IComicListView
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ComicListFragmentModule {

    @ContributesAndroidInjector
    fun bindComicListFragment(): ComicListFragment

    @Binds
    fun provideComicsListView(comicListFragment: ComicListFragment ): IComicListView

    @Binds
    fun provideComicsListPresenter( comicsListPresenter: ComicListPresenter): IComicListPresenter
}