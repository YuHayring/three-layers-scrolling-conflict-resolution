package cn.hayring.threelayersscrollingconflict

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {




    val tabLayout by lazy {
        findViewById<TabLayout>(R.id.tab_layout).also { it.setOnTouchListener(LogOnTouchListener) }
    }

    val viewPager2 by lazy {
        findViewById<ViewPager2>(R.id.view_pager2).also { it.getChildAt(0).setOnTouchListener(LogOnTouchListener) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layer_root)
        viewPager2.adapter = fragmentAdapter
        TabLayoutMediator(
            tabLayout, viewPager2, true, true
        ) { tab, position ->
            tab.text = tabName[position]
        }.attach()

    }

    val tabName = arrayOf("Mid", "End", "End", "End", "OldMid")



    val fragmentAdapter by lazy {
        object : FragmentStateAdapter(this) {

            override fun getItemCount() = 5
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> MidFragment()
                    1 -> EndFragment()
                    2 -> EndFragment()
                    3 -> EndFragment()
                    else -> OldMidFragment()
                }
            }
        }
    }
}