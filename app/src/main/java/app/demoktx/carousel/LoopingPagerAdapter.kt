package app.demoktx.carousel

import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup

import androidx.viewpager.widget.PagerAdapter

/**
 * A Pager Adapter that supports infinite loop.
 * This is achieved by adding a fake item at both beginning and the last,
 * And then silently changing to the same, real item, thus looks like infinite.
 */

abstract class LoopingPagerAdapter<T>(protected var context: Context?, itemList: List<T>, isInfinite: Boolean) : PagerAdapter() {

    var itemList: ArrayList<T> = ArrayList()
    protected var viewCache = SparseArray<View>()

    var isInfinite = false
        protected set
    protected var canInfinite = true

    private var dataSetChangeLock = false

    val listCount: Int
        get() = if (itemList == null) 0 else itemList!!.size

    val lastItemPosition: Int
        get() = if (isInfinite) {
            if (itemList == null) 0 else itemList!!.size
        } else {
            if (itemList == null) 0 else itemList!!.size - 1
        }

    init {
        this.isInfinite = isInfinite
        setItemList(itemList)
    }

    fun setItemList(itemList: List<T>) {
        viewCache = SparseArray()
//        this.itemList.clear();
        this.itemList.addAll( itemList);
        canInfinite = itemList.size > 1
        notifyDataSetChanged()
    }

    /**
     * Child should override this method and return the View that it wish to inflate.
     * View binding with data should be in another method - bindView().
     *
     * @param listPosition The current list position for you to determine your own view type.
     */
    protected abstract fun inflateView(viewType: Int, container: ViewGroup, listPosition: Int): View

    /**
     * Child should override this method to bind the View with data.
     * If you wish to implement ViewHolder pattern, you may use setTag() on the convertView and
     * pass in your ViewHolder.
     *
     * @param convertView  The View that needs to bind data with.
     * @param listPosition The current list position for you to get data from itemList.
     */
    protected abstract fun bindView(convertView: View, listPosition: Int, viewType: Int)

    fun getItem(listPosition: Int): T? {
        return if (listPosition >= 0 && listPosition < itemList!!.size) {
            itemList!![listPosition]
        } else {
            null
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val listPosition = if (isInfinite && canInfinite) getListPosition(position) else position

        val viewType = getItemViewType(listPosition)

        val convertView: View
        if (viewCache.get(viewType, null) == null) {
            convertView = inflateView(viewType, container, listPosition)
        } else {
            convertView = viewCache.get(viewType)
            viewCache.remove(viewType)
        }

        bindView(convertView, listPosition, viewType)

        container.addView(convertView)

        return convertView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val listPosition = if (isInfinite && canInfinite) getListPosition(position) else position

        container.removeView(`object` as View)
        if (!dataSetChangeLock) viewCache.put(getItemViewType(listPosition), `object`)
    }

    override fun notifyDataSetChanged() {
        dataSetChangeLock = true
        super.notifyDataSetChanged()
        dataSetChangeLock = false
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
        var count = 0
        if (itemList != null) {
            count = itemList!!.size
        }
        return if (isInfinite && canInfinite) {
            count + 2
        } else {
            count
        }
    }

    /**
     * Allow child to implement view type by overriding this method.
     * instantiateItem() will call this method to determine which view to recycle.
     *
     * @param listPosition Determine view type using listPosition.
     * @return a key (View type ID) in the form of integer,
     */
    protected fun getItemViewType(listPosition: Int): Int {
        return 0
    }

    private fun getListPosition(position: Int): Int {
        if (!(isInfinite && canInfinite)) return position
        val listPosition: Int
        if (position == 0) {
            listPosition = count - 1 - 2 //First item is a dummy of last item
        } else if (position > count - 2) {
            listPosition = 0 //Last item is a dummy of first item
        } else {
            listPosition = position - 1
        }
        return listPosition
    }
}
