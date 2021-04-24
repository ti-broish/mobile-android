package bg.dabulgaria.tibroish.presentation.main


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BaseActivity
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

interface IMainScreenView{

    fun showScreen(content : Fragment,
                   contentTag : String,
                   addToBackStack : Boolean,
                   transitionContent : Boolean)

    fun setRequestedOrientation( orientation:Int)

    val supportFragmentMngr: FragmentManager?

    val appCompatActivity: AppCompatActivity
}

class MainActivity : BaseActivity(), IMainScreenView, HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var resourceProvider: IResourceProvider

    @Inject
    lateinit var mainNavigator: IMainNavigator

    //region AppCompatActivity overrides
    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {

        super.onResume()
        mainNavigator.showHomeScreen()
    }
    //region AppCompatActivity overrides

    override fun androidInjector(): AndroidInjector<Any> {

        return dispatchingAndroidInjector
    }

    override val appCompatActivity: AppCompatActivity
        get() = this

    override val supportFragmentMngr: FragmentManager?
      = this.supportFragmentManager
}
