package com.shadab.framework.mvvm.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.shadab.framework.mvvm.R
import com.shadab.framework.mvvm.data.model.EnterpriseModel
import kotlinx.android.synthetic.main.item_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

class EnterpriseAdapter(private val enterpriseItemList: ArrayList<EnterpriseModel>) :
    RecyclerView.Adapter<EnterpriseAdapter.DataViewHolder>(), Filterable {

    var enterpriseFilterList = ArrayList<EnterpriseModel>()
    lateinit var mcontext: Context

    init {
        enterpriseFilterList = enterpriseItemList
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(enterpriseModel: EnterpriseModel) {
            itemView.textViewTitle.text = enterpriseModel.title
            itemView.textViewNoOfBackers.text = enterpriseModel.numBackers.toString()
            itemView.textViewBy.text = enterpriseModel.by.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout, parent,
                false
            )
        )

    override fun getItemCount(): Int = enterpriseFilterList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(enterpriseFilterList[position])

    fun addData(list: List<EnterpriseModel>) {
        enterpriseFilterList.addAll(list)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    enterpriseFilterList = enterpriseItemList
                } else {
                    val resultList = ArrayList<EnterpriseModel>()
                    for (row in enterpriseItemList) {
                        if (row.title.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    enterpriseFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = enterpriseFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                enterpriseFilterList = results?.values as ArrayList<EnterpriseModel>
                notifyDataSetChanged()
            }

        }
    }

}