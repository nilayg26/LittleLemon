package com.example.littlelemon.pages
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.littlelemon.ViewModels.AuthViewModel
import com.example.littlelemon.HomePage
import com.example.littlelemon.LogInPage
import com.example.littlelemon.ui.theme.LittleLemonTheme

@Composable
fun ProfilePage(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    sharedPreferences: SharedPreferences
) {
    val name=sharedPreferences.getString("name",null)
    val email=sharedPreferences.getString("email",null)
    LittleLemonTheme {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            LogoButtonLL()
            TextCardLL(text = "All about you !")
            Spacer(Modifier.height(50.dp))
            IconButtonLL(sharedPreferences, size = 100)
            Spacer(Modifier.height(20.dp))
            TextFieldLL(text = name.toString(), lamda = {it
            }, label = "Name")
            Spacer(Modifier.height(20.dp))
            TextFieldLL(text = email.toString(), lamda = {it
            }, label = "Email")
            Spacer(Modifier.height(100.dp))
            ButtonLL(text = "Log out", onClick = {
                navController.navigate(LogInPage.route){
                    popUpTo(HomePage.route){
                        inclusive=true
                    }
                }
                authViewModel.signOut(sharedPreferences)
            }
            )
        }
    }
}

