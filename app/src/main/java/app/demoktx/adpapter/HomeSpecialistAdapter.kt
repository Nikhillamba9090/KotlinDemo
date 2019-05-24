package app.demoktx.adpapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList

import app.demoktx.R
import app.demoktx.adpapter.viewholder.HomeSpecialistViewHolder
import app.demoktx.interfaces.IRecyclerClickListener
import app.demoktx.retrofit.response.data.SpecialistData

class HomeSpecialistAdapter(internal var context: Context?, internal var specialistList: ArrayList<SpecialistData>, internal var clickListener: IRecyclerClickListener) : RecyclerView.Adapter<HomeSpecialistViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeSpecialistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_specialist, parent, false)
        return HomeSpecialistViewHolder(view, clickListener)

    }

    override fun onBindViewHolder(holder: HomeSpecialistViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 20
    }
}
