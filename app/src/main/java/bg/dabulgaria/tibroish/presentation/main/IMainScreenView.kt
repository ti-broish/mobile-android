package bg.dabulgaria.tibroish.presentation.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface IMainScreenView{

    fun showScreen(content : Fragment,
                   contentTag : String,
                   addToBackStack : Boolean,
                   transitionContent : Boolean)

    fun setRequestedOrientation( orientation:Int)

    fun navigateBack()

    val supportFragmentMngr: FragmentManager?

    val appCompatActivity: AppCompatActivity

    fun showNavigation(show:Boolean)

    fun closeDrawer()

    fun showProcessing(processing: Boolean)

    fun showDismissableDialog(message: String, dismissCallback: () -> Unit)
}