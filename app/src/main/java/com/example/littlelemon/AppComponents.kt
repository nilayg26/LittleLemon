package com.example.littlelemon

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.littlelemon.ui.theme.Colors
import com.example.littlelemon.ui.theme.Fonts

@Composable
fun LogoButtonLL(padding:Int=30){
    Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Colors.O4, contentColor = Colors.Primary), modifier = Modifier.padding(padding.dp)) {
        Image(painter = painterResource(id =R.drawable.img), contentDescription = "logo",
            Modifier
                .padding(10.dp)
                .size(40.dp))
        Text(text = "Little Lemon", fontFamily = Fonts.headlines, fontSize = 28.sp)
    }
}
@Composable
fun TextCardLL(text:String){
    Card(colors = CardDefaults.cardColors(containerColor = Colors.Primary, contentColor = Colors.O4), modifier = Modifier.fillMaxWidth(), shape = RectangleShape) {
        Text(
            text = text,
            fontFamily = Fonts.paragraph,
            fontSize = 28.sp,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(40.dp)
        )
    }
}
@Composable
fun TextFieldLL(text: String,  label: String="",lamda: (String) -> String){
    OutlinedTextField(value = text, onValueChange ={newVal->lamda(newVal)}, label = { Text(
        text = label
    )},
        shape = RoundedCornerShape(20.dp), modifier = Modifier.padding(top = 10.dp)
    )
}
fun check(list:List<String>):Boolean{
    return list.contains("")
}
fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun LoadingScreenLL() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            strokeWidth = 5.dp,
            modifier = Modifier.size(25.dp)
        )
    }

}
@Preview(showBackground = true)
@Composable
fun Preview(){
    LoadingScreenLL()
}

