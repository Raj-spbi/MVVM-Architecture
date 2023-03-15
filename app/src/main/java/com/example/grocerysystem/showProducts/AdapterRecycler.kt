package com.example.grocerysystem.showProducts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.transform.RoundedCornersTransformation
import com.example.grocerysystem.R
import com.example.grocerysystem.databinding.ItemProductBinding
import com.example.grocerysystem.model.Items

class AdapterRecycler(
    val data: ArrayList<Items>,
    val onItemClick: (Items) -> Unit,
) : RecyclerView.Adapter<AdapterRecycler.ExViewHolder>() {

    class ExViewHolder(var binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExViewHolder {
        val view = DataBindingUtil.inflate<ItemProductBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_product, parent, false
        )
        return ExViewHolder(view)

    }

    override fun onBindViewHolder(holder: ExViewHolder, position: Int) {
        holder.binding.title.text = data[position].title
        holder.binding.desc.text = data[position].description
        holder.binding.catName.text = data[position].category
        holder.binding.newprice.text = "Rs. ${data[position].price}"



        holder.binding.image.load(data[position].image) {
            crossfade(true)
            crossfade(1000)
            placeholder(R.drawable.f1)
            error(R.drawable.f2)
            memoryCachePolicy(CachePolicy.ENABLED)
            transformations(RoundedCornersTransformation(16f))
//            transformations(CircleCropTransformation())
        }

        holder.binding.image.setOnClickListener {
            onItemClick(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}