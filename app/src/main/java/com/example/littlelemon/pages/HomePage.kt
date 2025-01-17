package com.example.littlelemon.pages

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.example.littlelemon.AuthViewModel
import com.example.littlelemon.Authenticated
import com.example.littlelemon.LogInPage
import com.example.littlelemon.Unauthenticated
import com.example.littlelemon.User

@Composable
fun HomePage(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    sharedPreferences: SharedPreferences
) {
    var clicked by remember {
        mutableStateOf(false)
    }
    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    Column {
        val authState = authViewModel.authState.observeAsState()
        LaunchedEffect(authState.value){
            when(authState.value){
                is Unauthenticated ->{sharedPreferences.edit().putBoolean("LoginStatus",false).apply()
                    navController.navigate(LogInPage.route)
                }
                is Authenticated ->{
                    name= sharedPreferences.getString("name",null).toString()
                    email=sharedPreferences.getString("email",null).toString()
                }
                else -> Unit
            }
        }
        println(name+"this is name")
        println(email+"this is name")
        Text(text = name)
        Text(text = email);
        Button(onClick = {sharedPreferences.edit().putBoolean("LoginStatus",false).apply();
            authViewModel.signOut()}) {
            Text(text = "sign out")
        }

    }
}
