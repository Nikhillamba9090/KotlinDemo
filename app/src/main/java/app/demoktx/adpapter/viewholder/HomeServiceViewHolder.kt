package app.demoktx.adpapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

import app.demoktx.interfaces.IRecyclerClickListener

class HomeServiceViewHolder(itemView: View, clickListener: IRecyclerClickListener) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener { clickListener.onRecyclerClick(adapterPosition, "", "service_click") }
    }
}
