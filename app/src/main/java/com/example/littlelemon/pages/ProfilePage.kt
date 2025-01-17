package com.example.littlelemon.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.littlelemon.LogoButtonLL
import com.example.littlelemon.TextCardLL
import com.example.littlelemon.TextFieldLL
import com.example.littlelemon.ui.theme.Colors
import com.example.littlelemon.ui.theme.Fonts
import com.example.littlelemon.ui.theme.LittleLemonTheme

@Composable
fun ProfilePage(){
    var name:String?=""
    LittleLemonTheme {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            LogoButtonLL()
            TextCardLL(text = "All about you!")
            Spacer(Modifier.height(20.dp))
            TextFieldLL(text = "Name", lamda = {it
            }, label = "Name")
            Spacer(Modifier.height(20.dp))
            TextFieldLL(text = "Email", lamda = {it
            }, label = "Email")
            Spacer(Modifier.height(200.dp))
            ButtonLL(text = "Log out", onClick = {/*call The signout function*/})
        }
    }
}
@Composable
fun ButtonLL(text:String="",onClick:()->(Unit)={}){
    Button(onClick = onClick, colors = ButtonDefaults.buttonColors(containerColor = Colors.Secondary, contentColor = Colors.Primary)) {
        Text(text = text, fontFamily = Fonts.paragraph, fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(){
    ProfilePage()
}