package com.example.littlelemon.pages
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.ViewModels.AuthViewModel
import com.example.littlelemon.Authenticated
import com.example.littlelemon.Error
import com.example.littlelemon.FireBaseDataError
import com.example.littlelemon.FireBaseDataLoading
import com.example.littlelemon.FireBaseDataUploaded
import com.example.littlelemon.HomePage
import com.example.littlelemon.Loading
import com.example.littlelemon.R
import com.example.littlelemon.ViewModels.FirebaseDataBaseViewModel
import com.example.littlelemon.ViewModels.User
import com.example.littlelemon.createToastMessage
import com.example.littlelemon.ui.theme.Colors
import com.example.littlelemon.ui.theme.Fonts
import kotlinx.coroutines.launch

@Composable
fun SignUp(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    sharedPreferences: SharedPreferences,
    firebaseDataBaseViewModel: FirebaseDataBaseViewModel
) {
    val context= LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var pass by remember {
        mutableStateOf("")
    }
    var cPass by remember {
        mutableStateOf("")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var isGoogle by remember {
        mutableStateOf(false)
    }
    val authState = authViewModel.authState.observeAsState()
    val fDbState=firebaseDataBaseViewModel.getLiveState().observeAsState()
    LaunchedEffect(authState.value){
        if(!check(listOf(email,pass,cPass)) ||isGoogle) {
            when (authState.value) {
                is Error -> {context.createToastMessage((authState.value as Error).msg); isLoading=false}
                is Loading->isLoading=true
                is Authenticated ->{
                    val user=authViewModel.getUser()
                    val pic=user?.email.toString()
                    val uid=user?.uid
                    sharedPreferences.edit().putString("uid",uid).apply()
                    sharedPreferences.edit().putString("pic",pic).apply()
                    firebaseDataBaseViewModel.addUser(user = User(uid=uid.toString(),name=name))
                }
                else -> isLoading=false
            }
        }
    }
    LaunchedEffect(fDbState.value){
        when(fDbState.value){
            FireBaseDataUploaded ->{
                sharedPreferences.edit().
                putBoolean("LoginStatus",true).
                putString("name",name).
                putString("email",email)
                    .apply()
                navController.navigate(HomePage.route)
            }
            FireBaseDataLoading->{
                isLoading=true
            }
            FireBaseDataError->{
                println("Error: ${FireBaseDataError.errMessage}")
            }
            else->{isLoading=false}
        }
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        LogoButtonLL()
        TextCardLL(text = "Lets Connect !")
        TextFieldLL(text = name,label="Enter name", password = false){
            name=it
            name
        }
        TextFieldLL(text = email,label="Enter Email", password = false){
            email=it
            email
        }
        TextFieldLL(text = pass,label="Set Password"){
            pass=it
            pass
        }
        TextFieldLL(text = cPass,label="Confirm Password"){
            cPass=it
            cPass
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {if(check(
                listOf(
                    name,
                    pass,
                    cPass
                )
            )
        ){context.createToastMessage("Invalid Credentials")}
        else{
            authViewModel.signUp(email,pass)
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
                   coroutineScope.launch{
                       authViewModel.googleLogIn(context = context)
                   }.invokeOnCompletion {
                       authState.value?.let { it1 -> Log.d(null, it1.value) }
                   }
        }, colors = ButtonDefaults.buttonColors(containerColor = Colors.O4, contentColor = Colors.Primary)) {
            Text(text = "Continue with ",fontFamily = Fonts.paragraph, fontSize = 18.sp)
            Image(painter = painterResource(id = R.drawable.google_logo), contentDescription ="Google Logo", modifier = Modifier.size(20.dp))
        }
    }
}