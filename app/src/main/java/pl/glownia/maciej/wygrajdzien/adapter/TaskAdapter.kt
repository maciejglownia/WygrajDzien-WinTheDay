package pl.glownia.maciej.wygrajdzien.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.glownia.maciej.wygrajdzien.database.TaskEntity
import pl.glownia.maciej.wygrajdzien.databinding.ItemTaskBinding

// We can now use here in < > -> TaskAdapter.ViewHolder, which is this class that we created below
// We need now to pass into a list that we want to use -> an array list in this case
// TaskEntity is our model, which we are also using for our database
class TaskAdapter(
    private val tasks: ArrayList<TaskEntity>,
    private val onClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    // Call a view holder, which is going to require a binding, which we can get from
    // the items are all binding type or which will be of type ItemTaskBinding
    // why ItemTaskBinding? Because that's how we called our example file - item_task.xml
    inner class ViewHolder(binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        // Set up views from xml file
        val tvTaskTitle = binding.tvTaskTitle
        val ivTaskCategoryImage = binding.ivTaskCategoryImage

        init {
            binding.llMain.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            val task = tasks[position]
            if (position != RecyclerView.NO_POSITION) {
                onClickListener.onItemClick(position, task)
            }
        }
    }

    interface OnItemClickListener {
        // Pass information you want to have
        fun onItemClick(position: Int, entity: TaskEntity)
    }

    // When we create our ViewHolder we just return ViewHolder
    // We get the parent ViewGroup, which is passed to us when we create a ViewHolder
    // So once it's created, we return the ViewHolder, which will return an object of this class
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    // Here we can use that ViewHolder and bind its individual items
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.context
        // Now we need to get an individual item and we get that from our items list
        // that is passed to this adapter
        // And we need to say at which position we want to have it because the item will be
        // the individual item of our items, this which is going to be just an task entity item
        val item = tasks[position] // this item will be an task entity object

        // It is going to be data as title of task and picture of category
        holder.tvTaskTitle.text = item.title
        // TODO("implement function to take drawable from database")
    }

    // So item size will return an integer, which is what we need to return here
    override fun getItemCount(): Int {
        return tasks.size
    }
}