package com.anaju.task.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anaju.task.R
import com.anaju.task.data.model.Status
import com.anaju.task.data.model.Task
import com.anaju.task.databinding.ItemTaskBinding

class TaskAdapter(
    private val context: Context,
    private val taskSelected: (Task, Int) -> Unit
) : ListAdapter<Task, TaskAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        const val SELECT_BACK = 1
        const val SELECT_REMOVE = 2
        const val SELECT_EDIT = 3
        const val SELECT_DETAILS = 4
        const val SELECT_NEXT = 5

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = getItem(position)
        holder.binding.textDescription.text = task.description
        setIndicators(task, holder)
    }

    private fun setIndicators(task: Task, holder: MyViewHolder) {
        with(holder.binding) {

            when (task.status) {

                Status.TODO -> {
                    buttonBack.isVisible = false
                    buttonFoward.setOnClickListener { taskSelected(task, SELECT_NEXT) }
                }

                Status.DOING -> {
                    buttonBack.setColorFilter(
                        ContextCompat.getColor(context, R.color.color_status_todo)
                    )
                    buttonFoward.setColorFilter(
                        ContextCompat.getColor(context, R.color.color_status_done)
                    )
                    buttonFoward.setOnClickListener { taskSelected(task, SELECT_NEXT) }
                    buttonBack.setOnClickListener { taskSelected(task, SELECT_BACK) }
                }

                Status.DONE -> {
                    buttonFoward.isVisible = false
                    buttonBack.setOnClickListener { taskSelected(task, SELECT_BACK) }
                }
            }

            buttonDelete.setOnClickListener { taskSelected(task, SELECT_REMOVE) }
            buttonEditar.setOnClickListener { taskSelected(task, SELECT_EDIT) }
            buttonDetails.setOnClickListener { taskSelected(task, SELECT_DETAILS) }
        }
    }

    inner class MyViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)
}