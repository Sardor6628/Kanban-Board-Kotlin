package com.example.tempstructure.ui.intro.ViewPager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.tempstructure.data.model.IntroModel
import com.example.tempstructure.databinding.VpItemIntroBinding

class IntroViewPagerAdapter(var context: Context, private val introModelList: List<IntroModel>) : PagerAdapter() {

    private lateinit var binding : VpItemIntroBinding

    override fun getCount(): Int {
        return introModelList.size
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        binding = VpItemIntroBinding.inflate(LayoutInflater.from(context), collection, false)

        binding.apply {
            tvTitle.text = introModelList[position].title
            tvDescription.text = introModelList[position].description

            ivImage.setImageResource(introModelList[position].imageId)
        }

        collection.addView(binding.root)
        return binding.root
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }
}