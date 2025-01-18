package com.example.littlelemon.ViewModels
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.littlelemon.DataError
import com.example.littlelemon.DataIsLoading
import com.example.littlelemon.DataRetrieved
import com.example.littlelemon.DataRetrievedFromRoom
import com.example.littlelemon.DatabaseLLAbs
import com.example.littlelemon.Dish
import com.example.littlelemon.MenuList
import com.example.littlelemon.State
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.printStack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class KtorViewModel(context: Context):ViewModel(){
    private var _dataState=MutableLiveData<State>()
    var dataState: LiveData<State> = _dataState
    private val client= HttpClient(Android)
    private var menuList=MutableLiveData<MenuList>()
    private val database= Room.databaseBuilder(
        context.applicationContext,
        DatabaseLLAbs::class.java,
        "inventory.db"
    ).createFromAsset("database/inventory.db").build()
   suspend fun loadData(){
        _dataState.value= DataIsLoading
       try {
           val response:HttpResponse=client.get("https://nilayg26.github.io/LittleLemonData/data.json")
           val responseJson=response.body<String>()
           val gson = Gson()
           val _menuList = gson.fromJson(responseJson, MenuList::class.java)!!
           val list=updateDataBase(_menuList)
           menuList.value= MenuList(list)
           _dataState.value= DataRetrieved
       }
       catch (e:UnknownHostException){
           retrieveFromDataBase()
       }
       catch (e:NullPointerException){
           retrieveFromDataBase()
       }
       catch (e:Exception){
           e.printStack()
           Log.d("Error",e.message.toString())
           _dataState.value= DataError
       }
    }
    fun getData(): MenuList {
        return menuList.value?: MenuList(listOf(Dish(1,"-None-","-None-","0","","")))
    }
    fun getLiveData():LiveData<MenuList>{
        return menuList
    }
    private suspend fun retrieveFromDataBase(){
        val list=database.dishesDao().getMenuList()
        menuList.value= MenuList(list)
        _dataState.value= DataRetrievedFromRoom
    }
    private suspend fun updateDataBase(_menuList: MenuList):List<Dish>{
        database.dishesDao().deleteAllDishes()
        _menuList.menu.forEach {
                dish->
            withContext(Dispatchers.Main) {
                database.dishesDao().insertDish(dish = dish)
            }
        }
        return database.dishesDao().getMenuList()
    }

}
