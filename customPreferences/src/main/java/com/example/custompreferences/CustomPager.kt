package com.example.custompreferences

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.example.custompreferences.model.CustomPagerModel

class CustomPager(val customPagerModel: ArrayList<CustomPagerModel>?, val context: Context) :
    PagerAdapter() {

    private var inflater: LayoutInflater? = null
    private var randomBackgroundList = arrayOf(
        android.R.color.holo_orange_dark,
        android.R.color.holo_red_dark,
        android.R.color.holo_purple
    )

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as? View?)
    }

    override fun getCount(): Int {
        return customPagerModel?.size ?: 0
    }

    override fun instantiateItem(view: ViewGroup, position: Int): View {
        val layout = inflater?.inflate(R.layout.layout_custom_view, view, false)
        val linearLayout = layout?.findViewById<LinearLayout?>(R.id.ll_customPagerLayout)
        val tvLable = linearLayout?.findViewById<TextView?>(R.id.tv_label)
        customPagerModel?.get(position)?.apply {
            linearLayout?.background = imageResource?.let { context.resources.getDrawable(it) }
//                imageResource?.let { it1 -> ContextCompat.getDrawable(context, it1) }
            linearLayout?.apply {
                tvLable?.text = label
                findViewById<TextView?>(R.id.tv_title)?.text = title
                findViewById<TextView?>(R.id.tv_offer)?.text = offer
                findViewById<TextView?>(R.id.tv_subTitle)?.text = subTitle
            }
        }
        tvLable?.setBackgroundColor(context.resources.getColor(randomBackgroundList.random()))
        view.addView(layout, 0)
        return layout ?: View(context)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }
}
