package com.example.littlelemon.ViewModels
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.littlelemon.FireBaseDataError
import com.example.littlelemon.FireBaseDataLoading
import com.example.littlelemon.FireBaseDataRetrieved
import com.example.littlelemon.FireBaseDataUploaded
import com.example.littlelemon.State
import com.google.firebase.firestore.FirebaseFirestore

data class User(val uid:String, var name:String)
class FirebaseDataBaseViewModel:ViewModel() {
    private val fDatabaseState = MutableLiveData<State?>()
    private val fDatabase = FirebaseFirestore.getInstance()
    private val name=MutableLiveData<String?>()
    fun getLiveState(): MutableLiveData<State?> {
        return fDatabaseState
    }
    fun getLiveName(): MutableLiveData<String?> {
        return name
    }
    fun addUser(user: User) {
        fDatabaseState.value = FireBaseDataLoading
        try {
            fDatabase.collection("Users").document(user.uid).set(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("User Added");
                        fDatabaseState.value = FireBaseDataUploaded
                    } else {
                        println("Hey error from add user: " + task.exception?.message)
                        FireBaseDataError.errMessage = task.exception?.message.toString()
                        fDatabaseState.value = FireBaseDataError
                    }
                }
        } catch (e: Exception) {
            FireBaseDataError.errMessage = e.message.toString()
            println("Hey error from add user: " + e.message)
            fDatabaseState.value = FireBaseDataError
        }
    }
   fun checkUser(uid: String,name_:String){
        fDatabase.collection("Users").document(uid).get().addOnFailureListener { e ->
            Log.d("Firestore", "Error fetching documents", e)
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.exists()) {
                    Log.d(null,"User already exists")
                    val snapshot=task.result
                    name.value= snapshot.data?.get("name").toString()
                    fDatabaseState.value = FireBaseDataRetrieved
                } else {
                    println("User Does not exists");
                    addUser(User(uid, name_))
                }
            } else {
                FireBaseDataError.errMessage = task.exception?.message.toString()
                println("From getUser(): ${task.exception?.message.toString()}")
            }
        }
    }
    fun getUser(uid: String){
        fDatabaseState.value = FireBaseDataLoading
        fDatabase.collection("Users").document(uid).get().addOnFailureListener { e ->
            Log.d("Firestore", "Error fetching documents", e)
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result.exists()) {
                    val snapshot = task.result
                    name.value= snapshot.data?.get("name").toString()
                    println("Nameee isss this::"+name.value)
                } else {
                    println("From getUser(): User does not exists")
                }
                fDatabaseState.value = FireBaseDataRetrieved
            } else {
                FireBaseDataError.errMessage = task.exception?.message.toString()
                println("From getUser(): ${task.exception?.message.toString()}")
            }
        }
    }
    fun setFirebaseDatabaseStateToNull(){
        fDatabaseState.value=null
        name.value=null
    }
}