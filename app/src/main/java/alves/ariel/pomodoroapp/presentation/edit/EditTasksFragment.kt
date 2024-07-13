package alves.ariel.pomodoroapp.presentation.edit

import alves.ariel.pomodoroapp.databinding.FragmentEditTasksBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class EditTasksFragment : Fragment() {
    private var _binding: FragmentEditTasksBinding? = null
    private val binding get() =  _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentEditTasksBinding.inflate(
           inflater,
           container,
           false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}