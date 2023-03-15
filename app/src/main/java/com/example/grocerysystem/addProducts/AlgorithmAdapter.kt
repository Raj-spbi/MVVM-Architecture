package com.example.grocerysystem.addProducts


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.grocerysystem.R
import com.example.grocerysystem.model.CategoryItem


class AlgorithmAdapter(
    context: Context,
    algorithmList: ArrayList<CategoryItem>, resource: Int
) : ArrayAdapter<CategoryItem>(context, resource) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView!!, parent)
    }

    private fun initView(
        position: Int, convertView: View,
        parent: ViewGroup
    ): View {
        // It is used to set our custom view.
        var convertView: View = convertView
        if (convertView == null) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.spinner_layout, parent, false)
        }
        val textViewName: TextView = convertView.findViewById(R.id.text_view)
        val currentItem: CategoryItem? = getItem(position)

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            textViewName.text = currentItem.title
        }
        return convertView
    }
}