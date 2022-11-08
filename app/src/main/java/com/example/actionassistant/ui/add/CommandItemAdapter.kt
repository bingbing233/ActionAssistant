package com.example.actionassistant.ui.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.actionassistant.databinding.ItemAddActionBinding
import com.example.actionassistant.module.Command

class CommandItemAdapter : RecyclerView.Adapter<CommandItemAdapter.CommandViewHolder>() {

    var list:List<Command> = mutableListOf<Command>()
    var onItemClick :(Int)->Unit= { }
    var onDeleteClick :(Int)->Unit= { }
    class CommandViewHolder(val binding: ItemAddActionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommandViewHolder {
        val view = ItemAddActionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CommandViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommandViewHolder, position: Int) {
        val command = list[position]
        holder.binding.apply {
            tvPkg.text = "Package:${command.pkgName}"
            tvPos.text = "${command.position}"
            root.setOnClickListener {
                onItemClick.invoke(position)
            }
            tvDelete.setOnClickListener {
                onDeleteClick.invoke(position)
            }
            tvType.text = "事件类型：${Command.getTypeContent(command.type)}"
            tvPos.isVisible = command.type == Command.TYPE_CLICK
        }
    }

    override fun getItemCount(): Int = list.size

}