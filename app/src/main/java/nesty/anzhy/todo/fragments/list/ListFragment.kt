package nesty.anzhy.todo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import nesty.anzhy.todo.R
import nesty.anzhy.todo.data.viewmodel.ToDoViewModel
import nesty.anzhy.todo.databinding.FragmentListBinding
import nesty.anzhy.todo.fragments.SharedViewModel

class ListFragment : Fragment() {

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

        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }



        val recyclerView = binding.recyclerView
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        mToDoViewModel.getAllData.observe(viewLifecycleOwner, {toDoList->
            mSharedViewModel.checkIfDatabaseEmpty(toDoList)
            mAdapter.setData(toDoList)
        })
        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer{
            showEmptyDatabaseViews(it)
        })

        //Set Menu. We also need to override method onCreateOptionsMenu. And in this method we need to inflate our new menu
        setHasOptionsMenu(true)

        return binding.root
    }

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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.menu_delete_all){
            confirmRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    //Show ALertDialog to confirm removal all items from database table
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){
                _,_->
            mToDoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully Removed Everything!",
                Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_->}
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to remove everything?")
        builder.create().show()
    }

}