package com.example.littlelemon

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

data class MenuList(var menu: List<Dish>)
@Entity(tableName = "DataBaseLL")
data class Dish(
    @PrimaryKey var id:Int,
    var title:String,
    var description:String,
    var price:String,
    var image:String,
    var category: String
)
@Dao //Data Access Object
interface DishesDao{
    @Insert
    suspend fun insertDish(dish: Dish)
    @Query("SELECT * FROM DataBaseLL")
    suspend fun getMenuList():List<Dish>
    @Query("SELECT * FROM DataBaseLL WHERE category= :category")
    suspend fun sortByCategory(category: String):List<Dish>
    @Query("DELETE FROM DataBaseLL")
    suspend fun deleteAllDishes()
}
@Database(entities = [Dish::class], version = 1)
abstract class DatabaseLLAbs:RoomDatabase(){
    abstract fun dishesDao():DishesDao
}

interface State{
    var value:String
}
object Loading:State{
    override var value="loading"
}
object Authenticated:State{
    override var value="authen"
}
object Unauthenticated:State{
    override var value="unauthen"
}
object EmailNotVerified:State{
    override var value="emailnotverified"
}
data class Error(val msg:String):State{
    override var value="Error"
}
object DataIsLoading:State{
    override var value="loading"
}
object DataRetrieved:State{
    override var value: String="Retrieved"
}
object DataRetrievedFromRoom:State{
    override var value: String="RetrievedFromRoom"
}
object DataError:State{
    override var value: String="Error"
}
object FireBaseDataUploaded:State{
    override var value="Data Uploaded"
}
object FireBaseDataError:State{
    override var value="Data Error"
    var errMessage=""
}
object FireBaseDataRetrieved:State{
    override var value="Data Retrieved"
}
object FireBaseDataLoading:State{
    override var value="Data loading"
}
