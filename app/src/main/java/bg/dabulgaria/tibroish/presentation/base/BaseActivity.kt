package bg.dabulgaria.tibroish.presentation.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.main.IMainScreenView
import bg.dabulgaria.tibroish.presentation.navigation.BackHandlerInterface
import bg.dabulgaria.tibroish.presentation.navigation.BackHandlerObject
import dagger.android.AndroidInjection

abstract class BaseActivity : AppCompatActivity(), IMainScreenView, BackHandlerInterface {

    protected var mBackHandlerObject: BackHandlerObject? = null
    protected var mTag: String? = null

    //region show screen
    override fun showScreen(content: Fragment,
                   contentTag: String,
                   addToBackStack: Boolean,
                   transitionContent: Boolean) {

        val ft = getSupportFragmentManager().beginTransaction()

        // Content area slide animation
        if (transitionContent) {

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        ft.replace(R.id.container, content, contentTag)

        if (addToBackStack) {
            ft.addToBackStack(contentTag + System.identityHashCode(content))
        }

        ft.commitAllowingStateLoss()
    }
    //endregion

    //region AppCompatActivity implementation
    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun navigateBack() {

        setOrientationOnBack()
        getSupportFragmentManager().popBackStackImmediate()
    }

    override fun onBackPressed() {

        if (mBackHandlerObject?.handleBackPressed() == true)
            return

        val backStackEntryCount = supportFragmentManager.backStackEntryCount

        if( backStackEntryCount >0){
            navigateBack()
        }
        else
            super.onBackPressed()
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        when(id) {

            android.R.id.home -> {

                if ( getSupportFragmentManager().getBackStackEntryCount() > 0) {

                    getSupportFragmentManager().popBackStack()

                    return true
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override val appCompatActivity: AppCompatActivity
        get() = this

    override val supportFragmentMngr: FragmentManager?
            = this.supportFragmentManager

    override fun setSelectedHandler(backHandlerObject: BackHandlerObject?, tag: String?) {

        if (mBackHandlerObject == null) {
            mBackHandlerObject = backHandlerObject
            this.mTag = tag
        } else if (this.mTag != null && this.mTag == tag) {
            mBackHandlerObject = backHandlerObject
            this.mTag = tag
        }
    }

    protected fun setOrientationOnBack()
    {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if (backStackEntryCount <= 1) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            return
        }

        val orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        val backEntry = supportFragmentManager.getBackStackEntryAt(backStackEntryCount - 2)
        val tagName = backEntry.name
    }
    //endregion
}