package app.demoktx.adpapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import app.demoktx.R
import app.demoktx.carousel.LoopingPagerAdapter

class HomeHeaderAdapter(context: Context?, itemList: List<String>, isInfinite: Boolean) : LoopingPagerAdapter<String>(context, itemList, isInfinite) {

    override fun inflateView(viewType: Int, container: ViewGroup, listPosition: Int): View {
        return LayoutInflater.from(context).inflate(R.layout.item_header_home, container, false)
    }

    override fun bindView(convertView: View, listPosition: Int, viewType: Int) {

    }
}
