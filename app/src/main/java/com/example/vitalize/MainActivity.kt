package com.example.vitalize

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.vitalize.databinding.ActivityMainBinding
import com.example.vitalize.user.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: UserViewModel by viewModels()
    private lateinit var  bottomNavigationView: BottomNavigationView;

    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (viewModel.currentUser != null) {
            replaceFragment(HomeSession())
        } else {
            replaceFragment(HomeNoSession())
        }
        setupNav()
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeSession_nav -> replaceFragment(HomeSession())
                R.id.userProfile_nav -> replaceFragment(UserProfile())
                //R.id.scan_bottomNav -> replaceFragment(ScanBarCode())
                //R.id.list_bottomNav -> replaceFragment(List())
                else -> {

                }
            }
            true
        }


        //Establecemos la primera página
        //supportFragmentManager.beginTransaction().replace(R.id.nav_graph, SignUp()).commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
        fragmentTransaction.commit()
    }

    private fun setupNav() {
        val navController = findNavController(R.id.nav_host_fragment)
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeSession -> showBottomNav()
                R.id.userProfile -> showBottomNav()
                else -> hideBottomNav()
            }
        }


    }

    private fun showBottomNav() {
        binding.bottomNav.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNav.visibility = View.GONE

    }
}
