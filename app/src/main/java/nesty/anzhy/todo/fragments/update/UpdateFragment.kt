package nesty.anzhy.todo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import nesty.anzhy.todo.R
import nesty.anzhy.todo.data.models.ToDoData
import nesty.anzhy.todo.data.viewmodel.ToDoViewModel
import nesty.anzhy.todo.databinding.FragmentUpdateBinding
import nesty.anzhy.todo.fragments.SharedViewModel

class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val mToDoViewModel: ToDoViewModel by viewModels()

    private val mSharedViewModel: SharedViewModel by viewModels()

    //UpdateFragmentArgs automatically generate by safeargs library
    //we're using this class to get data
    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        //Set Menu
        setHasOptionsMenu(true)


        binding.etCurrentTitleUpdate.setText(args.currentItem.title)
        binding.etCurrentDescriptionUpdateFragment.setText(args.currentItem.description)
        binding.spinnerCurrentUpdateFragment.setSelection(mSharedViewModel.parsePriorityToInt(args.currentItem.priority))

        binding.spinnerCurrentUpdateFragment.onItemSelectedListener = mSharedViewModel.listener
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    //this method will handle a click event on our menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> deleteItem()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun deleteItem() {
        //Show alert dialog to confirm remove item
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){
            _,_->
            mToDoViewModel.deleteData(args.currentItem)
            Toast.makeText(
                requireContext(),
            "Successfully removed ${args.currentItem.title}",
                    Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_->}
        builder.setTitle("Delete ${args.currentItem.title}?")
        builder.setMessage("Are you sure you want to remove '${args.currentItem.title}'?")
        builder.create().show()
    }

    private fun updateItem() {
        val title = binding.etCurrentTitleUpdate.text.toString()
        val description = binding.etCurrentDescriptionUpdateFragment.text.toString()
        val getPriority = binding.spinnerCurrentUpdateFragment.selectedItem.toString()

        val validation = mSharedViewModel.verifyDataFromUser(
            title,
            description
        )
        if (validation) {
            //update current item
            val updatedItem = ToDoData(
                args.currentItem.id,
                title,
                mSharedViewModel.parsePriority(getPriority),
                description
            )
            mToDoViewModel.updateData(updatedItem)
            Toast.makeText(
                requireContext(),
                "Successfully updated",
                Toast.LENGTH_SHORT
            ).show()

            //navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(
                requireContext(),
                "Please fill out all fields.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

