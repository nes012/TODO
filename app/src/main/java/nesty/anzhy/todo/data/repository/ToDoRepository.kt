package nesty.anzhy.todo.data.repository

import androidx.lifecycle.LiveData
import nesty.anzhy.todo.data.ToDoDAO
import nesty.anzhy.todo.data.models.ToDoData

class ToDoRepository(private val toDoDAO: ToDoDAO) {

    val getAllData: LiveData<List<ToDoData>> = toDoDAO.getAllData()

    suspend fun insertData(toDoData: ToDoData){
        toDoDAO.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData){
        toDoDAO.updateData(toDoData)
    }

    suspend fun deleteData(toDoData: ToDoData) {
        toDoDAO.deleteData(toDoData)
    }

    suspend fun deleteAllData(){
        toDoDAO.deleteAllData()
    }
}