package pl.qbasso.omisechallenge.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_charity.view.*
import pl.qbasso.omisechallenge.R
import pl.qbasso.omisechallenge.extensions.loadImage
import pl.qbasso.omisechallenge.model.Charity
import javax.inject.Inject

class CharityAdapter @Inject constructor(private var data: List<Charity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var clickListener : ClickListener? = null

    interface ClickListener {
        fun click(charity: Charity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = CharityViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_charity, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        bindPost(holder as CharityViewHolder, item)
    }

    private fun bindPost(holder: CharityViewHolder, item: Charity) {
        holder.text.text = item.name
        item.logoUrl?.let {
            holder.image.loadImage(it)
        }
        holder.root.setOnClickListener { clickListener?.click(item) }
    }

    override fun getItemCount(): Int = data.size

    fun setData(posts: List<Charity>) {
        data = posts
        notifyDataSetChanged()
    }

    internal class CharityViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var root: View = itemView.root
        var image: ImageView = itemView.image
        var text: TextView = itemView.text

    }

}
