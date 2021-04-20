package bg.dabulgaria.tibroish.presentation.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

import dagger.android.AndroidInjection

import bg.dabulgaria.tibroish.R

open class BaseActivity : AppCompatActivity() {


    //region show screen
    fun showScreen(content : Fragment,
                   contentTag : String,
                   addToBackStack : Boolean,
                   transitionContent : Boolean) {

        val ft = getSupportFragmentManager().beginTransaction()

        // Content area slide animation
        if (transitionContent) {

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        ft.replace( R.id.placeholder_content, content, contentTag )

        if (addToBackStack) {
            ft.addToBackStack(contentTag + System.identityHashCode(content))
        }

        ft.commitAllowingStateLoss()
    }
    //endregion

    //region AppCompatActivity implementation
    override fun onCreate(savedInstanceState : Bundle?) {

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
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
    //endregion
}