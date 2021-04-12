package com.example.paintapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.paintapp.R
import com.example.paintapp.databinding.ItemRvtoolsBinding
import com.example.paintapp.models.ToolsItem
import com.example.paintapp.utils.ToolsEnum
import com.example.paintapp.viewmodel.MainActivityViewmodel

class ToolsAdapter(val viewmodel: MainActivityViewmodel) :
    ListAdapter<ToolsItem, ToolsViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolsViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemRvtoolsBinding>(
            inflator,
            R.layout.item_rvtools,
            parent,
            false
        )
        return ToolsViewHolder(binding, viewmodel)
    }

    override fun onBindViewHolder(holder: ToolsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class DiffUtilCallback : DiffUtil.ItemCallback<ToolsItem>() {

    override fun areItemsTheSame(oldItem: ToolsItem, newItem: ToolsItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ToolsItem, newItem: ToolsItem): Boolean {
        return oldItem.equals(newItem)
    }

}

class ToolsViewHolder(
    val binding: ItemRvtoolsBinding,
    val viewmodel: MainActivityViewmodel
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ToolsItem) {
        binding.imageView.setImageResource(item.id)
        binding.textView.setText(item.name)
        binding.rootLayout.setOnClickListener {
            viewmodel.onToolSelected(toToolsEnum(item.name))
        }
        binding.tool = item
        binding.viewmodel = viewmodel
    }

    private fun toToolsEnum(toolName: String) =  when(toolName){
        ToolsEnum.BRUSH.toolName -> ToolsEnum.BRUSH
        ToolsEnum.ERASER.toolName -> ToolsEnum.ERASER
        ToolsEnum.COLOUR.toolName -> ToolsEnum.COLOUR
        ToolsEnum.BACKGROUND.toolName -> ToolsEnum.BACKGROUND
        ToolsEnum.RETURN.toolName -> ToolsEnum.RETURN
        ToolsEnum.BRUSHSIZE.toolName -> ToolsEnum.BRUSHSIZE
        else -> ToolsEnum.BRUSH
    }
}