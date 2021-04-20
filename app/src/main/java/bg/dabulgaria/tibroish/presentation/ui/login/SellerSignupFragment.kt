package bg.dabulgaria.tibroish.presentation.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bg.dabulgaria.tibroish.R

class SellerSignupFragment : Fragment() {

    companion object {
        fun newInstance() = SellerSignupFragment()
    }

    private lateinit var viewModel: SellerSignupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.seller_signup_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SellerSignupViewModel::class.java)
        // TODO: Use the ViewModel
    }

}