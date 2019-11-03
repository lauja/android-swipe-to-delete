package com.example.swipetodelete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class MyAdapter(private val values: List<String>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        setWidthOfTileToFillScreen(view.cardView)
        return MyViewHolder(view)
    }

    private fun setWidthOfTileToFillScreen(cardView: View) {
        // The tile needs to be the width of the screen so we need to do this
        // programmatically as we can not set match_parent on a horizontalscrollview
        cardView.layoutParams = LinearLayout.LayoutParams(cardView.context.resources.displayMetrics.widthPixels,
            LinearLayout.LayoutParams.WRAP_CONTENT)
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