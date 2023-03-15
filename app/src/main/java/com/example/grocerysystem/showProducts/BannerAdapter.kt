package com.example.grocerysystem.showProducts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.transform.RoundedCornersTransformation
import com.example.grocerysystem.R
import com.example.grocerysystem.model.BannerItem

class BannerAdapter(
    val context: Context,
    val list: List<BannerItem>

) : RecyclerView.Adapter<BannerAdapter.ModelViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_model, parent, false)
        return ModelViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {

//        Glide.with(context).load(list[position].image).into(holder.img)

//        holder.name.text = list[position].title
//        holder.date.text = list[position].imageUrl

        holder.img.load(list[position].imageUrl) {
            crossfade(true)
            crossfade(1000)
            placeholder(R.drawable.f1)
            error(R.drawable.f2)
            memoryCachePolicy(CachePolicy.ENABLED)
            transformations(RoundedCornersTransformation(16f))
//            transformations(CircleCropTransformation())
        }

    }

    class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById(R.id.imgModel) as ImageView
        val name = itemView.findViewById(R.id.tvName) as TextView
        val date = itemView.findViewById(R.id.tvDate) as TextView
    }
}