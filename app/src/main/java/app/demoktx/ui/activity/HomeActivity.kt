package app.demoktx.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import app.demoktx.R
import app.demoktx.ui.fragments.HomeFragment

 class HomeActivity : BaseActivity() {
    internal lateinit var fragmentManager: FragmentManager

    override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        fragmentManager = getSupportFragmentManager()
        addFragment(HomeFragment(), "home_frag")

    }
// add home fragment
    private fun addFragment(fragment: Fragment, tag: String) {
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.replace(R.id.container, fragment, tag)
//        transaction.addToBackStack(tag)
        transaction.commitAllowingStateLoss()
    }
}
