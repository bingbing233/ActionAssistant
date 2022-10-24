package com.example.actionassistant.ui.add

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.actionassistant.MainViewModel
import com.example.actionassistant.base.BaseFragment
import com.example.actionassistant.databinding.FragmentAddBinding
import com.example.actionassistant.dialog.AddDialog
import kotlinx.coroutines.flow.collect

class AddFragment : BaseFragment<FragmentAddBinding>(FragmentAddBinding::inflate) {

    private val mAdapter = CommandItemAdapter()
    private val viewModel:MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAdd.setOnClickListener {
            val dialog = AddDialog()
            dialog.show(parentFragmentManager,null)
            dialog.setOnPosClickListener {
                viewModel.addCommand(it)
            }
        }

        mAdapter.onItemClick = { index ->
            val dialog = AddDialog()
            dialog.apply {
                val command = viewModel.commands.value[index]
                setNode(command.nodeName)
                setPkgName(command.pkgName)
                setPosition(command.position)
                setOnPosClickListener {
                    viewModel.updateCommandAt(index,it)
                    mAdapter.notifyDataSetChanged()
                }
            }
            dialog.show(parentFragmentManager,null)
        }
        mAdapter.onDeleteClick = {
            viewModel.removeCommand(it)
        }
        binding.rvCommands.apply {
         adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.commands.collect{
                mAdapter.list = it
                viewModel.saveData()
                mAdapter.notifyDataSetChanged()
            }
        }
    }
}