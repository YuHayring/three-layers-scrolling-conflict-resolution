package cn.hayring.threelayersscrollingconflict

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class OldMidFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layer_mid_old, container, false)
    }

    val tabRecycler by lazy {
        requireView().findViewById<RecyclerView>(R.id.tab_recycler).also { it.setOnTouchListener(LogOnTouchListener) }
    }

    val viewPager by lazy {
        requireView().findViewById<ViewPager>(R.id.view_pager).also { it.setOnTouchListener(LogOnTouchListener) }
    }

    var currentIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        tabRecycler.adapter = SimpleAdapter(requireContext())
        viewPager.adapter = SimplePagerAdapter(requireContext())
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (currentIndex != position) {
                    currentIndex = position
                    (tabRecycler.adapter as SimpleAdapter).notifyDataSetChanged()
                    (tabRecycler.layoutManager as LinearLayoutManager).scrollToPosition(currentIndex)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    class SimplePagerAdapter(val context: Context) :
        PagerAdapter() {
        override fun getCount() = 5

        val views = arrayOf(NestScrollVerticalRecyclerView(context), NestScrollVerticalRecyclerView(context) ,NestScrollVerticalRecyclerView(context) ,NestScrollVerticalRecyclerView(context), NestScrollVerticalRecyclerView(context)).also {
            it.forEach {
                it.adapter = EndFragment.SimpleAdapter(context)
                it.layoutManager = GridLayoutManager(context, 2)
            }
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(views[position])
        }


        override fun instantiateItem(view: ViewGroup, position: Int): Any {
            return views[position].also {
                view.addView(it)
            }
        }
    }

    inner class SimpleAdapter(val context: Context): RecyclerView.Adapter<SimpleViewHolder>() {

        @SuppressLint("RestrictedApi")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SimpleViewHolder(AppCompatTextView(context).also {
            it.gravity = Gravity.CENTER
            val paddingH = com.google.android.material.internal.ViewUtils.dpToPx(context, 100).toInt()
            val paddingV = com.google.android.material.internal.ViewUtils.dpToPx(context, 20).toInt()
            it.setPadding(paddingH, paddingV, paddingH, paddingV)
        })

        override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
            with(holder.itemView as TextView) {
                text = position.toString()
                if (currentIndex == position) {
                    setBackgroundColor(Color.GRAY)
                } else {
                    setBackgroundColor(Color.TRANSPARENT)
                }
            }
        }

        override fun getItemCount() = 5

    }

    inner class SimpleViewHolder(itemView: AppCompatTextView): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                viewPager.currentItem = adapterPosition
            }
        }
    }
}