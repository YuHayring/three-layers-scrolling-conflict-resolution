package cn.hayring.threelayersscrollingconflict

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MidFragment: Fragment() {

    val tabLayout by lazy {
        requireView().findViewById<TabLayout>(R.id.tab_layout).also { it.setOnTouchListener(LogOnTouchListener) }
    }

    val viewPager2 by lazy {
        requireView().findViewById<ViewPager2>(R.id.view_pager2).also { it.getChildAt(0).setOnTouchListener(LogOnTouchListener) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layer_mid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager2.adapter = fragmentAdapter
        TabLayoutMediator(
            tabLayout, viewPager2, true, true
        ) { tab, position ->
            tab.text = tabName[position]
        }.attach()
    }


    val tabName = arrayOf("End", "End", "End", "End", "End")

    val fragmentAdapter by lazy {
        object : FragmentStateAdapter(this) {
            override fun getItemCount() = 5
            override fun createFragment(position: Int) = EndFragment()
        }
    }
}