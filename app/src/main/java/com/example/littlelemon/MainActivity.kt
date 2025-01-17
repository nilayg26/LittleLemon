package com.example.littlelemon

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.littlelemon.pages.HomePage
import com.example.littlelemon.pages.LogIn
import com.example.littlelemon.pages.SignUp
import com.example.littlelemon.ui.theme.Colors
import com.example.littlelemon.ui.theme.LittleLemonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel:AuthViewModel by viewModels()
        val sharedPreferences=this.getSharedPreferences("LittleLemon",Context.MODE_PRIVATE)
        setContent {
            LittleLemonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Colors.O2
                ) {
                    Navigation(authViewModel,sharedPreferences)
                }
            }
        }
    }
}

@Composable
fun Navigation(authViewModel: AuthViewModel, sharedPreferences: SharedPreferences){
    val navController= rememberNavController()
    val status= sharedPreferences.getBoolean("LoginStatus",false)
    NavHost(navController = navController, startDestination = when(status){
            true->HomePage.route
            else->LogInPage.route
    }){
        composable(LogInPage.route) {
            LogIn(navController = navController,authViewModel,sharedPreferences)
        }
        composable(SignInPage.route){
            SignUp(navController=navController,authViewModel,sharedPreferences)
        }
        composable(HomePage.route) {
            HomePage(navController,authViewModel,sharedPreferences)
        }
        composable(ProfilePage.route) {
          //  ProfilePage(navController,authViewModel,sharedPreferences)
        }
    }
}