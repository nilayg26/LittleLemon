package com.example.littlelemon.pages

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.ViewModels.AuthViewModel
import com.example.littlelemon.DataError
import com.example.littlelemon.DataIsLoading
import com.example.littlelemon.DataRetrieved
import com.example.littlelemon.DataRetrievedFromRoom
import com.example.littlelemon.MenuList
import com.example.littlelemon.ViewModels.KtorViewModel
import com.example.littlelemon.ProfilePage
import com.example.littlelemon.R
import com.example.littlelemon.createToastMessage
import com.example.littlelemon.ui.theme.Colors
import com.example.littlelemon.ui.theme.Fonts
import com.example.littlelemon.ui.theme.LittleLemonTheme
import java.util.Locale

@Composable
fun HomePage(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    sharedPreferences: SharedPreferences,
    dataViewModel: KtorViewModel
) {
    var category by remember {
        mutableStateOf("")
    }
    val context= LocalContext.current
    var isDataLoading by remember {
        mutableStateOf(true)
    }
    val dataState=dataViewModel.dataState.observeAsState()
    LaunchedEffect(dataState.value){
            when (dataState.value) {
                DataIsLoading -> {
                    isDataLoading = true;
                    context.createToastMessage("Data Loading... ")
                }
                DataRetrieved -> {
                    isDataLoading =
                        false;println("Data is retrieved")
                }
                DataError -> {
                    isDataLoading = false;context.createToastMessage("Failed to fetch Content")
                }
                DataRetrievedFromRoom->{
                    context.createToastMessage("Cannot connect to server")
                }
            }
    }
    LaunchedEffect(Unit){
        if(dataState.value!=DataRetrieved) {
            dataViewModel.loadData()
        }
    }
    val menuList=dataViewModel.getLiveData().observeAsState()
    val horizontalScrollState= rememberScrollState()
    val verticalScrollState= rememberScrollState()
    var search by remember {
        mutableStateOf("")
    }
    val list:List<String> = listOf("Starters","Mains","Desserts","Drinks")
    LittleLemonTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier =Modifier.weight(0.75F))
                LogoButtonLL()
                Spacer(modifier = Modifier.width(25.dp))
                IconButtonLL(sharedPreferences = sharedPreferences,onClick = {navController.navigate(ProfilePage.route)})
                Spacer(modifier = Modifier.width(10.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Colors.Primary, contentColor = Colors.O4), modifier = Modifier.fillMaxWidth(), shape = RectangleShape) {
                Spacer(modifier = Modifier.height(10.dp))
                TextLL(text = "Little Lemon", size = 40,fontFamily = Fonts.headlines, color = Colors.Secondary)
                TextLL(text="Kanpur", size = 20)
                Spacer(modifier = Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(modifier = Modifier
                        .size(180.dp)){
                        TextLL(text="We are a Multi Cuisine restaurant," +
                                    " focused on traditional recipes served with a modern twist",size = 15)
                    }
                    Box(
                        modifier = Modifier
                            .padding(bottom = 25.dp, end = 10.dp)
                            .size(150.dp)
                            .clip(RoundedCornerShape(16.dp))

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.hero_image),
                            contentDescription = "Hero Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                    }
                }
                Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                    TextField(value = search, onValueChange ={newVal->search = newVal}, label = { Text(text = "Type to Search🔎")}, shape = RoundedCornerShape(20.dp))
                }
                Spacer(modifier = Modifier.height(25.dp))
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(text ="ORDER FOR DELIVERY !", fontFamily = Fonts.paragraph,
                    fontSize = 15.sp,
                    color = Colors.O3,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(horizontalScrollState)) {
                Spacer(modifier = Modifier.width(10.dp))
                list.forEach{
                    ButtonLL(text=it, color = Colors.O4, fontSize = 12){
                        category=it
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(verticalScrollState), horizontalAlignment = Alignment.CenterHorizontally) {
                when (dataState.value) {
                    DataRetrieved, DataRetrievedFromRoom -> {
                        FilterData(menuList = menuList, search = search, category = category)
                    }
                    DataIsLoading -> {
                        Spacer(modifier = Modifier.height(10.dp))
                        LoadingScreenLL(45)
                    }
                    else -> {
                        Spacer(modifier = Modifier.height(20.dp))
                        TextLL("Error Loading Content\nCheck Internet and Restart the App", color = Colors.O3)
                    }
                }
            }

        }
    }
}
@Composable
fun FilterData(menuList: State<MenuList?>,search:String,category: String){
    if (search.isNotEmpty()){
        menuList.value?.menu?.filter {
            it.title.contains(search, true)
        }?.forEach { dish ->
            ItemCard(
                price = dish.price,
                dishName = dish.title,
                des = dish.description,
                picUrl = dish.image
            )
        }
    }
    else {
        if (category.isNotEmpty())
            menuList.value?.menu?.filter {
                category.equals(it.category, true)
            }?.forEach { dish ->
                ItemCard(
                    price = dish.price,
                    dishName = dish.title,
                    des = dish.description,
                    picUrl = dish.image
                )
            }
        else {
            menuList.value?.menu?.forEach { dish ->
                ItemCard(
                    price = dish.price,
                    dishName = dish.title,
                    des = dish.description,
                    picUrl = dish.image
                )
            }
        }
    }
}

