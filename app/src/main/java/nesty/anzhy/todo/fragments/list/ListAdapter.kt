package nesty.anzhy.todo.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import nesty.anzhy.todo.R
import nesty.anzhy.todo.data.models.Priority
import nesty.anzhy.todo.data.models.ToDoData
import nesty.anzhy.todo.databinding.RowLayoutBinding

class ListAdapter: RecyclerView.Adapter<ListAdapter.VH>() {

    var dataList = emptyList<ToDoData>()

    class VH(var binding: RowLayoutBinding): RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        var binding = RowLayoutBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val dataItem = dataList[position]
        holder.binding.tvTitleItemLayout.text = dataItem.title
        holder.binding.tvDescriptionItemLayout.text = dataItem.description

        holder.binding.rawLayout.setOnClickListener {

            //ListFragmentDirections automatically generate by safeargs library
            //we're using this class to pass data
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataItem)
            holder.itemView.findNavController().navigate(action)
        }


        val priority = dataItem.priority
        when(priority){
            Priority.HIGH ->holder.binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.red))
            Priority.MEDIUM ->holder.binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.yellow))
            Priority.LOW ->holder.binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.green))
        }
    }

    override fun getItemCount(): Int = dataList.size


    fun setData(toDoData: List<ToDoData>){
        this.dataList = toDoData
        notifyDataSetChanged()
    }
}