package bg.dabulgaria.tibroish.presentation.main


import android.os.Bundle
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BaseActivity
import bg.dabulgaria.tibroish.presentation.comic.details.model.ComicDetailsViewData
import bg.dabulgaria.tibroish.presentation.comic.details.view.ComicDetailsFragment
import bg.dabulgaria.tibroish.presentation.comic.list.view.ComicListFragment
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.presentation.ui.home.HomeFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : BaseActivity(), IMainNavigator, HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var resourceProvider: IResourceProvider

    //region AppCompatActivity overrides
    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {

        super.onResume()
        showHomeScreen()
    }
    //region AppCompatActivity overrides

    override fun androidInjector(): AndroidInjector<Any> {

        return dispatchingAndroidInjector
    }
    //region HasSupportFragmentInjector implementation
//    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
//
//        return dispatchingAndroidInjector
//    }
    //endregion HasSupportFragmentInjector implementation

    //region IMainNavigator implementation
    override fun showHomeScreen() {

        var homeFragment = supportFragmentManager.findFragmentByTag(HomeFragment.TAG )
        if (homeFragment == null) {

            homeFragment = HomeFragment.newInstance()
        }

        showScreen(homeFragment, HomeFragment.TAG, false, false)
    }

    override fun showComicList() {

        var comicListFragment = supportFragmentManager.findFragmentByTag(ComicListFragment.TAG )
        if (comicListFragment == null) {

            comicListFragment = ComicListFragment.newInstance()
        }

        showScreen(comicListFragment, ComicListFragment.TAG, false, false)
    }

    override fun showComicDetails(comicDetailsViewData: ComicDetailsViewData ) {

        var comicDetailsFragment = supportFragmentManager.findFragmentByTag(ComicDetailsFragment.TAG )
        if (comicDetailsFragment == null) {

            comicDetailsFragment = ComicDetailsFragment.newInstance(comicDetailsViewData )
        }

        showScreen(comicDetailsFragment, ComicListFragment.TAG, true, true)
    }
    //endregion IMainNavigator implementation

}
