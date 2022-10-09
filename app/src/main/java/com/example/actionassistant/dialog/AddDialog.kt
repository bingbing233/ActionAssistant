package com.example.actionassistant.dialog

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.actionassistant.databinding.DialogAddBinding
import com.example.actionassistant.module.Command

class AddDialog : DialogFragment() {

    val TAG = javaClass.simpleName
    lateinit var binding: DialogAddBinding
    private var mOnPosClick: ((Command) -> Unit)? =null
    private var pkgName = "com."
    private var node = "null"
    private var position = Point()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window?.let {
            val lp = it.attributes
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.horizontalMargin = 150f
            it.attributes = lp
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvNeg.setOnClickListener {
            dismiss()
        }

        binding.tvPos.setOnClickListener {
            mOnPosClick?.invoke(createCommand())
            dismiss()
        }
        binding.editPgkName.setText(pkgName)
        binding.editNode.setText(node)
        binding.editX.setText(position.x.toString())
        binding.editY.setText(position.y.toString())
    }

    private fun createCommand(): Command {
        val pkgName = binding.editPgkName.text?.toString()
        val node = binding.editNode.text.toString()
        val x = binding.editX.text.toString()
        val y = binding.editY.text.toString()
        val command = Command().apply {
            this.pkgName = pkgName?:""
            this.nodeName = node
            this.position = Point(x.toInt(),y.toInt())
            Log.e(TAG, "createCommand: point = ${this.position}", )
        }
        return command
    }

    fun setOnPosClickListener(block:(Command)->Unit){
        this.mOnPosClick = block
    }
    fun setPkgName(pkgName:String){
        this.pkgName = pkgName
    }
    fun setNode(node:String){
        this.node = node
    }

    fun setPosition(point: Point){
        this.position = point
    }
}