package app.demoktx.adpapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList

import app.demoktx.R
import app.demoktx.adpapter.viewholder.HomeServiceViewHolder
import app.demoktx.interfaces.IRecyclerClickListener
import app.demoktx.retrofit.response.data.ServiceData

class HomeServiceAdapter(internal var context: Context?, internal var serviceList: ArrayList<ServiceData>, internal var clickListener: IRecyclerClickListener) : RecyclerView.Adapter<HomeServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_service, parent, false)
        return HomeServiceViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: HomeServiceViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return serviceList.size
    }
}
