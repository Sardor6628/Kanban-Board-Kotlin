package com.example.tempstructure.ui.intro

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.tempstructure.R
import com.example.tempstructure.app.BaseActivity
import com.example.tempstructure.databinding.ActivityIntroBinding
import com.example.tempstructure.ui.home.HomeActivity
import com.example.tempstructure.ui.intro.ViewPager.IntroViewPagerAdapter
import com.example.tempstructure.utils.ListUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPagerListener()
        initView()
        initAdapter()
        setClick()
    }

    private fun initView() {

    }

    private fun initAdapter() {
        val introViewPagerAdapter = IntroViewPagerAdapter(this@IntroActivity, ListUtils.getIntroList())
        binding.vpIntro.adapter = introViewPagerAdapter
        binding.dotsIndicator.setViewPager(binding.vpIntro)
        binding.vpIntro.currentItem = 0
    }

    private fun viewPagerListener() {
        binding.vpIntro.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                binding.tvSkip.visibility = View.VISIBLE
                when (position) {
                    2 -> { binding.tvSkip.visibility = View.GONE }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setClick() {
        binding.btnNext.setOnClickListener(this)
        binding.tvSkip.setOnClickListener(this)
    }

    private fun skipIntro() {
        prefsUtils.setLogin(true)
        val intent = Intent(this@IntroActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onClick(p0: View?) {
        when(p0){
            binding.btnNext -> {
                if (binding.vpIntro.currentItem != ListUtils.getIntroList().size - 1) {
                    binding.vpIntro.currentItem = binding.vpIntro.currentItem + 1
                } else {
                    skipIntro()
                }
            }
            binding.tvSkip -> {
                skipIntro()
            }
        }
    }

}