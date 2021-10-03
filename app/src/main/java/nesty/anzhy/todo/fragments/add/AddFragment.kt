package nesty.anzhy.todo.fragments.add

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import nesty.anzhy.todo.R

import nesty.anzhy.todo.databinding.FragmentAddBinding
import nesty.anzhy.todo.databinding.FragmentListBinding

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)


        //Set Menu
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }
}