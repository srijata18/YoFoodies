package com.example.dashboardmodule.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.custompreferences.Constants
import com.example.custompreferences.CustomPager
import com.example.custompreferences.model.CustomPagerModel
import com.example.dashboardmodule.R
import kotlinx.android.synthetic.main.layout_dashboard.*
import java.util.*
import kotlin.collections.ArrayList
import android.util.Base64
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.networkmodule.data.UserDetails


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    private var mPager: ViewPager? = null
    private var currentPage = 0
    private var pageNumbers = 0
    private val customPagerList =
        arrayOf(
            CustomPagerModel(
                R.drawable.burger,
                "EXCLUSIVE",
                "Epic deals",
                "40% off",
                "On legendary Restaurants"
            ),
            CustomPagerModel(R.drawable.burger, "OFFER", "40% off", "CODE: Z14CT", "Free Delivery"),
            CustomPagerModel(
                R.drawable.burger,
                "EXCLUSIVE",
                "Epic deals",
                "40% off",
                "On legendary Restaurants"
            ),
            CustomPagerModel(R.drawable.burger, "OFFER", "40% off", "CODE: Z14CT", "Free Delivery")
        )

    private val imagesArray = ArrayList<CustomPagerModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userDetails = this.arguments?.getParcelable(Constants.USERDETAILSKEY) as? UserDetails?
        if (userDetails != null)
            Toast.makeText(activity, userDetails?.u_name ?: "None received", Toast.LENGTH_SHORT)
                .show()
        initPager(userDetails)
    }

    private fun initPager(userDetails: UserDetails?) {
        for (element in customPagerList) imagesArray.add(element)

        mPager = view?.findViewById(R.id.vp_dynamicPager) as ViewPager
        mPager?.adapter = CustomPager(imagesArray, requireActivity())

        pageNumbers = customPagerList.size
        // Auto start of viewpager
        val handler = Handler()
        val update = Runnable {
            if (currentPage === pageNumbers) {
                currentPage = 0
            }
            mPager?.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 2000, 2000)

        userDetails?.let {
            userDetails?.u_img?.let {
                val imageBytes = Base64.decode(userDetails?.u_img, Base64.DEFAULT)
                val bitmap: Bitmap? = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                Glide.with(this)
                    .load(bitmap)
                    .error(R.drawable.ic_search)
                    .circleCrop()
                    .into(iv_userImage)
            }
            tv_userDetails.text =
                "Welcome foodie ${userDetails?.u_name} ! Your phone number : ${userDetails?.u_phone} \n Email : ${userDetails?.u_email}"
        }
    }
}