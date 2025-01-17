package com.example.littlelemon

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
class AuthViewModel: ViewModel() {
    private val auth: FirebaseAuth =FirebaseAuth.getInstance()
    private val _authState= MutableLiveData<State>()
    val authState: LiveData<State> = _authState
    init {
        checkAuthStatus()
    }
    private fun checkAuthStatus(isGoogle:Boolean=false) {
        if (!isGoogle) {
            when (auth.currentUser) {
                null -> _authState.value = Unauthenticated
                else -> _authState.value = Authenticated
            }
            println(auth.currentUser.toString())
        }
    }
    fun login(email:String, pass:String,isGoogle: Boolean=false){
        _authState.value=Loading
        if(!isGoogle) {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = Authenticated
                    } else {
                        try {
                            _authState.value =
                                Error(task.exception?.message ?: "Something went wrong")
                        } catch (e: Exception) {
                            _authState.value = Error(e.message.toString())
                        }
                    }
                }
        }
    }
    fun signUp(email: String,pass: String,isGoogle: Boolean=false){
        _authState.value=Loading
        if (!isGoogle) {

            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = Authenticated
                    println(authState.value?.value)
                }
                else {
                    _authState.value = Error(task.exception?.message ?: "Something went wrong")
                }
            }
        }
    }
    fun setAuthState(state: State){
        _authState.value=state
    }
    fun signOut(){
        _authState.value=Unauthenticated
        auth.signOut()
    }
    private fun getRequest(context: Context):GetCredentialRequest{
        val googleIdOption=GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .build()
        return (GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build())
    }
    @SuppressLint("SuspiciousIndentation")
//    suspend fun googleSignUp(context: Context){
//        _authState.value=Loading
//        val credentialManager=CredentialManager.create(context = context)
//        val request=getRequest(context)
//        val result=credentialManager.getCredential(request = request, context = context)
//        when(result.credential){
//            is CustomCredential->{
//                if (result.credential.type== GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
//                    val googleIdTokenCredential=GoogleIdTokenCredential.createFrom(
//                        result.credential.data
//                    )
//                    val googleIdTokenId=googleIdTokenCredential.idToken
//                    val authCredential= GoogleAuthProvider.getCredential(googleIdTokenId,null)
//                    auth.currentUser?.linkWithCredential(authCredential).addOnCompleteListener {
//                        task-> if (task.isSuccessful) {
//                            _authState.value = Authenticated
//                            Log.d(null, "Was reached here!")
//
//                        } else {
//                            _authState.value =
//                                Error(task.exception?.message ?: "Something went wrong")
//                        }
//                    }.await()
//                }
//                else {
//                    context.createToastMessage("Try Again!")
//                }
//            }
//        }


//    catch (e:Exception){
//        context.createToastMessage("Could not get to your Google Account")
//        println("Hi")
//        println(e.message.toString())
//        e.message?.let { Log.d("error", it) }
//    }
   // }
    fun getUser():FirebaseUser?{
       return auth.currentUser
    }
    suspend fun googleLogIn(context: Context) {
        _authState.value = (Loading)
        val request = getRequest(context = context)
        val credentialManager = CredentialManager.create(context = context)
        try {
            val result = credentialManager.getCredential(request = request, context = context)
            when (result.credential) {
                is CustomCredential -> {
                    if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(
                            result.credential.data
                        )
                        val googleIdTokenId = googleIdTokenCredential.idToken
                        val authCredential = GoogleAuthProvider.getCredential(googleIdTokenId, null)
                        auth.signInWithCredential(authCredential).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                 _authState.value = Authenticated
                                Log.d(null, "Was reached here!")

                            } else {
                                _authState.value =
                                    Error(task.exception?.message ?: "Something went wrong")
                            }
                        }.await()
//                        println(auth.currentUser?.displayName)
//                        println(auth.currentUser?.email)
                    } else {
                        context.createToastMessage("Try Again!")
                    }
                }

                else -> {}
            }

        } catch (e: Exception) {
            _authState.value=Unauthenticated
            context.createToastMessage("Could not get to your Google Account")
            e.message?.let { Log.d("error", it) }
        }
    }
}


interface State{
    var value:String
}
object Loading:State{
    override var value="loading"
}
object Authenticated:State{
    override var value="authen"
}
object Unauthenticated:State{
    override var value="unauthen"
}
data class Error(val msg:String):State{
    override var value="Error"
}
