package cn.hayring.threelayersscrollingconflict

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.ViewUtils
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.random.Random

class EndFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layer_end, container, false)
    }

    val recyclerView by lazy {
        requireView().findViewById<RecyclerView>(R.id.recycler_view)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = SimpleAdapter(requireContext())
    }


    class SimpleAdapter(private val context: Context): RecyclerView.Adapter<SimpleViewHolder>() {

        val count = java.util.Random().nextInt(5) + 15

        @SuppressLint("RestrictedApi")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SimpleViewHolder(AppCompatTextView(context).also {
            it.gravity = Gravity.CENTER
            it.textSize = com.google.android.material.internal.ViewUtils.dpToPx(context, 14)
            it.setPadding(com.google.android.material.internal.ViewUtils.dpToPx(context, 30).toInt())
        })

        override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
            (holder.itemView as TextView).text = position.toString()
        }

        override fun getItemCount() = count

    }

    class SimpleViewHolder(itemView: View): ViewHolder(itemView)
}