package com.example.vitalize

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.vitalize.user.LogIn
import com.example.vitalize.user.SignUp
import com.example.vitalize.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Establecemos la primera página
        //supportFragmentManager.beginTransaction().replace(R.id.nav_graph, SignUp()).commit()
    }

    /*fun checkCurrentUser() {
        // [START check_current_user]
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }
        // [END check_current_user]
    }

    fun getUserProfile() {
        // [START get_user_profile]
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = user.uid
        }
        // [END get_user_profile]
    }

    fun getProviderData() {
        // [START get_provider_data]
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            for (profile in user.providerData) {
                // Id of the provider (ex: google.com)
                val providerId = profile.providerId

                // UID specific to the provider
                val uid = profile.uid

                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
            }
        }
        // [END get_provider_data]
    }

    fun updateProfile() {
        // [START update_profile]
        val user = FirebaseAuth.getInstance().currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName("Jane Q. User")
            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
            .build()
        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }
        // [END update_profile]
    }

    fun logOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut()
        // [END auth_sign_out]
    }*/

}