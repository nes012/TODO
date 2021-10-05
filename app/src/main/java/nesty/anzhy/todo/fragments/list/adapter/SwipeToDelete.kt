package nesty.anzhy.todo.fragments.list.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDelete: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //we don't need to move our items. so this method will return false
        return false
    }
    /*
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }
     */
}