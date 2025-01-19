package com.example.littlelemon.pages
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.littlelemon.HomePage
import com.example.littlelemon.LogInPage
import com.example.littlelemon.ViewModels.AuthViewModel
import com.example.littlelemon.ViewModels.FirebaseDataBaseViewModel
import com.example.littlelemon.ui.theme.LittleLemonTheme

@Composable
fun ProfilePage(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    sharedPreferences: SharedPreferences,
    firebaseDataBaseViewModel: FirebaseDataBaseViewModel
) {
    val email by remember {
        mutableStateOf(sharedPreferences.getString("email", null))
    }
    val uid by remember {
        mutableStateOf(sharedPreferences.getString("uid",""))
    }
    val fDbState = firebaseDataBaseViewModel.getLiveState().observeAsState()
    val nameFb = firebaseDataBaseViewModel.getLiveName().observeAsState()
    LaunchedEffect(fDbState.value) {
        if (nameFb.value ==null) {
            firebaseDataBaseViewModel.getUser(uid!!)
        }
    }
    LittleLemonTheme {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            LogoButtonLL()
            TextCardLL(text = "All about you !")
            Spacer(Modifier.height(50.dp))
            IconButtonLL(sharedPreferences, size = 100)
            Spacer(Modifier.height(20.dp))
            TextFieldLL(
                text = if (nameFb.value.toString() == "null") {
                    "Loading..."
                }; else {
                    nameFb.value.toString()
                }, lamda = {
                    it
                }, label = "Name"
            )
            Spacer(Modifier.height(20.dp))
            TextFieldLL(text = email.toString(), lamda = {
                it
            }, label = "Email")
            Spacer(Modifier.height(100.dp))
            ButtonLL(text = "Log out", onClick = {
                navController.navigate(LogInPage.route) {
                    popUpTo(HomePage.route) {
                        inclusive = true
                    }
                }
                authViewModel.signOut(sharedPreferences)
                firebaseDataBaseViewModel.setFirebaseDatabaseStateToNull()
            }
            )
        }
    }
}

