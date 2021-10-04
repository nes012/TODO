package nesty.anzhy.todo.fragments

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import nesty.anzhy.todo.R
import nesty.anzhy.todo.data.models.Priority

class SharedViewModel(application: Application): AndroidViewModel(application) {

    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when(position){
                0->{ (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red))}
                1->{ (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow))}
                2->{ (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green))}


            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }

    fun verifyDataFromUser(title: String, description: String): Boolean{
        //we will check here if our data not empty before adding to DB
        return if(TextUtils.isEmpty(title)|| TextUtils.isEmpty(description)){
            false
        }
        else !(title.isEmpty()||description.isEmpty())
    }

     fun parsePriority(priority: String): Priority {
        return when(priority){
            "High Priority" ->(Priority.HIGH)
            "Medium Priority" ->(Priority.MEDIUM)
            "Low Priority" ->(Priority.LOW)

            else -> Priority.LOW
        }
    }
}