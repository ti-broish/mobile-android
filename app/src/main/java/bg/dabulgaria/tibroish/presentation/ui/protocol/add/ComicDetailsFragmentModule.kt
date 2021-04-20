package bg.dabulgaria.tibroish.presentation.ui.protocol.add

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
    fun bindDetailsPresenter( detailsPresenter: ComicDetailsPresenter): IComicDetailsPresenter

}