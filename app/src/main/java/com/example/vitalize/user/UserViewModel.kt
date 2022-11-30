package com.example.vitalize.user

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitalize.data.AuthRepository
import com.example.vitalize.data.FirestoreRepository
import com.example.vitalize.data.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val authRepository: AuthRepository, private val firestoreRepository: FirestoreRepository) : ViewModel() {

    private val _userWeight = MutableLiveData<Resource<String>>()
    val userWeight: LiveData<Resource<String>> = _userWeight

    private val _userHeight = MutableLiveData<Resource<String>>()
    val userHeight: LiveData<Resource<String>> = _userHeight

    private val _loginFlow = MutableLiveData<Resource<FirebaseUser>?>()
    val loginFlow: LiveData<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableLiveData<Resource<FirebaseUser>?>()
    val signupFlow: LiveData<Resource<FirebaseUser>?> = _signupFlow

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init {
        if(authRepository.currentUser != null){
            _loginFlow.value = Resource.Success(authRepository.currentUser!!)
            loadDataUser()
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        val result = authRepository.login(email, password)
        _loginFlow.value = result
        loadDataUser()
    }

    fun singup(name: String, email: String, password: String) = viewModelScope.launch {
        val result = authRepository.signup(name, email, password)
        _signupFlow.value = result
    }

    fun logout() {
        authRepository.logout()
        _loginFlow.value = null
        _signupFlow.value = null
    }

    fun resetFlow(){
        _loginFlow.value = null
        _signupFlow.value = null
    }

    fun getNameCurrentUser(): String? {
        return currentUser?.displayName
    }

    fun getHeightCurrentUser() = viewModelScope.launch {
        val result = firestoreRepository.getDataUser(currentUser!!.uid, "height")
        _userHeight.value = result
        Log.d("userviewmodel", _userHeight.value.toString())
    }

    fun getWeightCurrentUser() = viewModelScope.launch {
        val result = firestoreRepository.getDataUser(currentUser!!.uid, "weight")
        _userWeight.value = result
        Log.d("userviewmodel", _userWeight.value.toString())
    }

    fun getPhotoUrl(): Uri? {
        return currentUser?.photoUrl
    }
    fun loadDataUser() = viewModelScope.launch {
        getWeightCurrentUser()
        getHeightCurrentUser()
    }


}