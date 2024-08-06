package pl.glownia.maciej.wd.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import pl.glownia.maciej.wd.R

// Also is good to check explanation of this class at link below.
// https://medium.com/@kitek/recyclerview-swipe-to-delete-easier-than-you-thought-cff67ff5e5f6
abstract class SwipeToEditCallback(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    private val editIcon = ContextCompat.getDrawable(context, R.drawable.ic_edit_white_24dp)
    private val intrinsicWidth = editIcon!!.intrinsicWidth
    private val intrinsicHeight = editIcon!!.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#24AE05")
    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        /**
         * To disable "swipe" for specific item return 0 here.
         * For example:
         * if (viewHolder?.itemViewType == YourAdapter.SOME_TYPE) return 0
         * if (viewHolder?.adapterPosition == 0) return 0
         */
        // So he says if the view holder is 10, then he says that to zero.
        // So you can deactivate this functionality for specific items.
        if (viewHolder.adapterPosition == 10) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    // You could move the whole thing around altogether
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    // Draw edit view, so...
    // Set the background to the background color and move things around a little bit.
    // Draw the green added background, then calculate the position of the edit icon
    // so that it always is at the right spot
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                c, itemView.left + dX, itemView.top.toFloat(),
                itemView.left.toFloat(), itemView.bottom.toFloat()
            )
            super.onChildDrawOver(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }

        // Draw the green edit background
        background.color = backgroundColor
        background.setBounds(
            itemView.left + dX.toInt(),
            itemView.top,
            itemView.left,
            itemView.bottom
        )
        background.draw(c)

        // Calculate position of edit icon
        val editIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val editIconMargin = (itemHeight - intrinsicHeight)
        val editIconLeft = itemView.left + editIconMargin - intrinsicWidth
        val editIconRight = itemView.left + editIconMargin
        val editIconBottom = editIconTop + intrinsicHeight

        // Draw the delete icon
        editIcon!!.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom)
        editIcon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        c.drawRect(left, top, right, bottom, clearPaint)
    }
    // Now, next thing we need to do is we need to bind this feature
}