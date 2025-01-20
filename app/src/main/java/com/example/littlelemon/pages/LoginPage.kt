package com.example.littlelemon.pages
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.ViewModels.AuthViewModel
import com.example.littlelemon.Authenticated
import com.example.littlelemon.Error
import com.example.littlelemon.HomePage
import com.example.littlelemon.Loading
import com.example.littlelemon.LogInPage
import com.example.littlelemon.R
import com.example.littlelemon.SignInPage
import com.example.littlelemon.ViewModels.FirebaseDataBaseViewModel
import com.example.littlelemon.createToastMessage
import com.example.littlelemon.ui.theme.Colors
import com.example.littlelemon.ui.theme.Fonts
import kotlinx.coroutines.launch
@Composable
fun LogIn(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    sharedPreferences: SharedPreferences,
    firebaseDataBaseViewModel: FirebaseDataBaseViewModel
) {
    val context= LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var email by remember {
        mutableStateOf("")
    }
    var pass by remember {
        mutableStateOf("")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var isGoogle by remember {
        mutableStateOf(false)
    }
    val authState=authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value){
        Log.d(null, authState.value?.value.toString()+" From Launched Effect")
        if(!check(listOf(email,pass)) ||isGoogle) {
            when (authState.value) {
                is Authenticated -> {
                    val user=authViewModel.getUser()
                    val name= user?.displayName.toString()
                    val e=user?.email.toString()
                    val pic=user?.photoUrl.toString()
                    val uid=user?.uid.toString()
                    firebaseDataBaseViewModel.checkUser(uid, name)
                    sharedPreferences.edit().
                            putBoolean("LoginStatus",true).
                            putString("name",name).
                            putString("email",e).
                            putString("pic",pic).
                            putString("uid",uid)
                        .apply()
                    navController.navigate(HomePage.route){
                        popUpTo(LogInPage.route){
                            inclusive=true
                        }
                    }
                }
                is Loading-> isLoading=true
                is Error -> {context.createToastMessage("Credentials not found"); isLoading=false}
                else -> isLoading=false
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()
    , horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoButtonLL()
        TextCardLL(text = "Lets Get you in !")
        TextFieldLL(text = email,label="Enter email", password = false){
            email=it
            email
        }
        TextFieldLL(text = pass,label="Enter Password"){
            pass=it
            pass
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {if(check(
                listOf(
                    email,
                    pass
                )
            )
        ){context.createToastMessage("Fields cannot be empty")}
        else{
            authViewModel.login(email, pass)
        }}, colors = ButtonDefaults.buttonColors(containerColor = Colors.O4, contentColor = Colors.Primary)) {
            if (!isLoading) {
                Text(text = "Go! ", fontFamily = Fonts.paragraph, fontSize = 18.sp)
            }
            else{
                LoadingScreenLL()
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Or", fontSize = 20.sp, fontFamily = Fonts.paragraph)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            isGoogle=true
            authViewModel.setAuthState(Loading);
            println(authViewModel.authState.value?.value)
                         coroutineScope.launch {
                             authViewModel.googleLogIn(context = context)
                         }.invokeOnCompletion {
                             authState.value?.let { it1 -> Log.d(null, it1.value) }
                         }
                         }, colors = ButtonDefaults.buttonColors(containerColor = Colors.O4, contentColor = Colors.Primary)) {
            Text(text = "Continue with ",fontFamily = Fonts.paragraph, fontSize = 18.sp)
            Image(painter = painterResource(id = R.drawable.google_logo), contentDescription ="Google Logo", modifier = Modifier.size(20.dp
            ))
        }
        Text(text = "Newbie? Sign In", modifier = Modifier
            .padding(top = 80.dp)
            .clickable {
                navController.navigate(
                    SignInPage.route
                )
            }, color = Color.Blue, fontFamily = Fonts.paragraph)
    }
}






















