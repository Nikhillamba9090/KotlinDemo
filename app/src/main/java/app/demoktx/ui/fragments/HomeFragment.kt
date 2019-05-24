package app.demoktx.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList

import app.demoktx.R
import app.demoktx.adpapter.HomeHeaderAdapter
import app.demoktx.adpapter.HomeServiceAdapter
import app.demoktx.adpapter.HomeSpecialistAdapter
import app.demoktx.carousel.LoopingViewPager
import app.demoktx.carousel.ZoomPageTransformer
import app.demoktx.interfaces.IRecyclerClickListener
import app.demoktx.retrofit.response.data.ServiceData
import app.demoktx.retrofit.response.data.SpecialistData
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

class HomeFragment : BaseFragment(), IRecyclerClickListener {
    override fun onRecyclerClick(o: Any, data: Any, extraData: Any) {
         if (extraData != null) {
         if (extraData == "service_click") {
         }
     }
    }

    internal lateinit var view: View

    @BindView(R.id.loop_view_pager)
    public lateinit var loopingViewPager: LoopingViewPager
    @BindView(R.id.services_recycler)
    lateinit var serviceRecycler: RecyclerView
    @BindView(R.id.specialist_recycler)
    lateinit var specialistRecycler: RecyclerView
    internal lateinit var headerAdapter: HomeHeaderAdapter
    internal lateinit var serviceAdapter: HomeServiceAdapter
    internal var bannerList: MutableList<String> = ArrayList()
    internal var serviceList = ArrayList<ServiceData>()
    internal var specialistList = ArrayList<SpecialistData>()
    internal lateinit var specialistAdapter: HomeSpecialistAdapter
    internal var mContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_home, container, false)
        ButterKnife.bind(this, view)
        addItems()
        addServiceItems()
        headerAdapter = HomeHeaderAdapter(mContext, bannerList, true)
        loopingViewPager!!.setAdapter(headerAdapter)
        loopingViewPager!!.setPageTransformer(false, ZoomPageTransformer(true))
        loopingViewPager!!.setOffscreenPageLimit(bannerList.size)
        initServiceAdapter()

        return view
    }

    private fun initServiceAdapter() {
        serviceAdapter = HomeServiceAdapter(mContext, serviceList, this)
        serviceRecycler!!.layoutManager = object : GridLayoutManager(mContext, 4) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        serviceRecycler!!.adapter = serviceAdapter

        initSpecialAdapter()
    }

    private fun initSpecialAdapter() {
        specialistAdapter = HomeSpecialistAdapter(mContext, specialistList, this)
        specialistRecycler!!.layoutManager = object : LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        specialistRecycler!!.adapter = specialistAdapter
    }

    internal fun addItems() {
        bannerList.add("sd")
        bannerList.add("sad")
        bannerList.add("sd")
        bannerList.add("sad")
        bannerList.add("sd")
        bannerList.add("sad")
        bannerList.add("sd")
        bannerList.add("sad")
        bannerList.add("sd")
        bannerList.add("sad")
        bannerList.add("sd")
        bannerList.add("sad")

    }

    internal fun addServiceItems() {
        serviceList.add(ServiceData("1", "sName", ""))
        serviceList.add(ServiceData("1", "sName", ""))
        serviceList.add(ServiceData("1", "sName", ""))
        serviceList.add(ServiceData("1", "sName", ""))
        serviceList.add(ServiceData("1", "sName", ""))
        serviceList.add(ServiceData("1", "sName", ""))
        serviceList.add(ServiceData("1", "sName", ""))
    }




    @OnClick(R.id.all_tv)
    internal fun showAllTherapist() {
    }


}
