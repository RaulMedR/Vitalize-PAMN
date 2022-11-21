package com.example.vitalize.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitalize.data.AuthRepository
import com.example.vitalize.data.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _loginFlow = MutableLiveData<Resource<FirebaseUser>?>()
    val loginFlow: LiveData<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableLiveData<Resource<FirebaseUser>?>()
    val signupFlow: LiveData<Resource<FirebaseUser>?> = _signupFlow

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if(repository.currentUser != null){
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        val result = repository.login(email, password)
        _loginFlow.value = result
    }

    fun singup(name: String, email: String, password: String) = viewModelScope.launch {
        Log.d("viewmodelregister", "he2")
        val result = repository.signup(name, email, password)
        Log.d("viewmodelregister", "he3")
        _signupFlow.value = result
    }

    fun logout() {
        repository.logout()
        _loginFlow.value = null
        _signupFlow.value = null
    }

    fun resetFlow(){
        _loginFlow.value = null
        _signupFlow.value = null
    }







    /*

        private val context = getApplication<Application>().applicationContext
        private val TAG = "EmailPassword"

        private lateinit var _mAuth: FirebaseAuth
        val mAuth: FirebaseAuth
            get() = _mAuth


        init {
            Log.d("User", "UserViewModel created!")
            _mAuth = Firebase.auth
        }

        override fun onCleared() {
            super.onCleared()
            Log.d("User", "UserViewModel destroyed!")
        }



        /*
        * Re-initializes the game data to restart the game.
        */
        fun reinitializeData() {
            //_score = 0
            //_currentWordCount = 0
            //wordsList.clear()
            //getNextWord()
        }

        fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
            _mAuth.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener() {
                if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user: FirebaseUser? = mAuth?.getCurrentUser()
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", it.exception)
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        // [END create_user_with_email]
        }

         fun signIn(email: String, password: String) {
             mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener {
                 if (it.isSuccessful) {
                     Toast.makeText(context, "Se ha iniciado sesión correctamente", Toast.LENGTH_SHORT).show()
                     val user = mAuth?.currentUser
                     Log.d(TAG, "signIn:success")
                 } else {
                     Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                     Toast.makeText(context, "Ha habido un error", Toast.LENGTH_SHORT).show()
                     Log.d(TAG, "signIn:unsuccess")
                 }
             }
        }

        private fun updateUI(user: FirebaseUser?) {}


     */
}