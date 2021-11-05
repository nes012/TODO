package nesty.anzhy.todo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.*
import nesty.anzhy.todo.R
import nesty.anzhy.todo.data.models.ToDoData
import nesty.anzhy.todo.viewmodel.ToDoViewModel
import nesty.anzhy.todo.databinding.FragmentListBinding
import nesty.anzhy.todo.viewmodel.SharedViewModel
import nesty.anzhy.todo.fragments.list.adapter.ListAdapter
import nesty.anzhy.todo.fragments.list.adapter.SwipeToDelete
import nesty.anzhy.todo.utils.hideKeyBoard
import nesty.anzhy.todo.utils.observeOnce

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val mAdapter: ListAdapter by lazy { ListAdapter() }

    private val mToDoViewModel: ToDoViewModel by viewModels()

    private val mSharedViewModel: SharedViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        setupRecyclerView()


        //Observe LiveData
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, { toDoList ->
            mSharedViewModel.checkIfDatabaseEmpty(toDoList)
            mAdapter.setData(toDoList)
        })
        /*
        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer{
            showEmptyDatabaseViews(it)
        })
         */

        //Set Menu. We also need to override method onCreateOptionsMenu. And in this method we need to inflate our new menu
        setHasOptionsMenu(true)

        //hide soft feyboard
        hideKeyBoard(requireActivity())

        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300

        }
        //Swipe to Delete
        swipeToDelete(recyclerView)
    }


    /*
    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) {
       if(emptyDatabase){
           binding.tvNoData.visibility = View.VISIBLE
           binding.ivNoData.visibility = View.VISIBLE
       }
        else{
           binding.tvNoData.visibility = View.INVISIBLE
           binding.ivNoData.visibility = View.INVISIBLE
       }
    }
     */


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> mToDoViewModel.sortByHighPriority.observe(viewLifecycleOwner, {
                mAdapter.setData(it)
            })
            R.id.menu_priority_low -> mToDoViewModel.sortByLowPriority.observe(viewLifecycleOwner, {
                mAdapter.setData(it)
            })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        var searchQuery = "%$query%"
        mToDoViewModel.searchDatabase(searchQuery).observeOnce(viewLifecycleOwner, { list ->
            list?.let {
                mAdapter.setData(list)
            }
        })
    }


    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }


    //Show ALertDialog to confirm removal all items from database table
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully Removed Everything!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to remove everything?")
        builder.create().show()
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = mAdapter.dataList[viewHolder.adapterPosition]

                //Delete Item
                mToDoViewModel.deleteData(itemToDelete)
                mAdapter.notifyItemRemoved(viewHolder.adapterPosition)

                //Restore Deleted Item
                restoreDeletedData(
                    viewHolder.itemView,
                    itemToDelete,
                    //viewHolder.adapterPosition
                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        //we need to call this function inside setupRecyclerView
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData,
                                   //position: Int
    ) {
        val snackbar = Snackbar.make(
            view, "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo") {
            mToDoViewModel.insertData(deletedItem)
            //if we want to use StaggeredGridLayoutManager we can't use notifyItemChanged
            //mAdapter.notifyItemChanged(position)
        }
        snackbar.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        //this way we will avoid memory leaks
        _binding = null
    }

}