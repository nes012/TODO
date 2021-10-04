package nesty.anzhy.todo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import nesty.anzhy.todo.data.models.ToDoData

@Dao
interface ToDoDAO {

    @Query("SELECT*FROM todo_table ORDER BY id ASC")
    fun getAllData():LiveData<List<ToDoData>>


    //suspend function mean that we want to run our function on a background thread later in the view model
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData)
}