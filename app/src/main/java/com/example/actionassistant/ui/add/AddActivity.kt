package com.example.actionassistant.ui.add

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.actionassistant.R
import com.example.actionassistant.databinding.ActivityAddBinding
import com.example.actionassistant.dialog.AddDialog
import com.example.actionassistant.module.Command
import com.google.gson.Gson

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    private val list = mutableListOf<Command>()
    private val mAdapter = CommandItemAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list.addAll(commands)
        binding.fabAdd.setOnClickListener {
            val dialog = AddDialog()
            dialog.setOnPosClickListener {
                list.add(it)
                mAdapter.notifyDataSetChanged()
            }
            dialog.show(supportFragmentManager,"")
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AddActivity )
            mAdapter.list = list
            adapter = mAdapter
        }

        mAdapter.onItemClick = { position->
            val dialog = AddDialog()
            dialog.setOnPosClickListener {
                list[position] = it
                mAdapter.notifyItemChanged(commands.size-1)
            }
            dialog.setPkgName(list[position].pkgName)
            dialog.setNode(list[position].nodeName)
            dialog.show(supportFragmentManager,"")
            dialog.setPosition(list[position].position)
        }
        mAdapter.onDeleteClick = {
            list.removeAt(it)
            mAdapter.notifyItemRemoved(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save -> {
                save()
                Toast.makeText(applicationContext, "保存成功", Toast.LENGTH_SHORT).show()
                finish()
                //do save
            }
        }
        return true
    }

    private fun save(){
        val sp = getSharedPreferences(SP_COMMAND, Context.MODE_PRIVATE).edit()
        val gson = Gson()
        val string = gson.toJson(list)
        Log.e("TAG", "save: $string", )
        sp.putString(SP_COMMAND,string)
        sp.apply()
        commands = list
    }


}