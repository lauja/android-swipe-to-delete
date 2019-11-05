package com.example.swipetodelete

import android.annotation.SuppressLint
import android.view.*
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import kotlin.math.abs

private const val SWIPE_MIN_DISTANCE = 5
private const val SWIPE_THRESHOLD_VELOCITY = 200

class MyAdapter(private val values: List<String>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        setWidthOfTileToFillScreen(view.cardView)
        addSwipeToDismissHandler(view as? HorizontalScrollView)
        return MyViewHolder(view)
    }

    private fun setWidthOfTileToFillScreen(cardView: View) {
        // The tile needs to be the width of the screen so we need to do this
        // programmatically as we can not set match_parent on a horizontalscrollview
        cardView.layoutParams = LinearLayout.LayoutParams(cardView.context.resources.displayMetrics.widthPixels,
            LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addSwipeToDismissHandler(view: HorizontalScrollView?) {
        view?.let {
            val deleteGestureDetector = DeleteGestureDetector(view)
            val gestureDetector = GestureDetectorCompat(it.context, deleteGestureDetector)
            val deleteCard = it.findViewById<CardView>(R.id.deleteCard)

            it.setOnTouchListener { _, event ->
                // User is swiping
                if (gestureDetector.onTouchEvent(event)) {
                    true
                } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    val layoutParams = deleteCard.layoutParams as? ViewGroup.MarginLayoutParams
                    val layoutMargin = layoutParams?.let { params -> params.leftMargin + params.rightMargin } ?: 0
                    val deleteCardWidth = deleteCard.measuredWidth + layoutMargin
                    val activeFeature = (view.scrollX + deleteCardWidth / 2) / deleteCardWidth
                    val scrollTo = activeFeature * deleteCardWidth
                    // "snap" dismiss in or out depending on how much of the delete button is shown
                    view.smoothScrollTo(scrollTo, 0)
                    true
                } else {
                    false
                }
            }
        }
    }

    inner class DeleteGestureDetector(private val horizontalScrollView: HorizontalScrollView?) : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            if (e1 != null && e2 != null) {
                // right to left
                if (e1.x - e2.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    horizontalScrollView?.smoothScrollTo(horizontalScrollView.measuredWidth, 0)
                    return true
                } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) { // left to right
                    horizontalScrollView?.smoothScrollTo(0, 0)
                    return true
                }
            }

            return false
        }
    }


    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(values[position])
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(value: String) {
            view.textView.text = value
        }
    }
}