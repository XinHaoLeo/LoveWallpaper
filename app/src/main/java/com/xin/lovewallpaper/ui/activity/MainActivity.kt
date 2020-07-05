/*
 * Copyright 2020 Leo
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xin.lovewallpaper.ui.activity

import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.xin.lovewallpaper.R
import com.xin.lovewallpaper.base.BaseMvpActivity
import com.xin.lovewallpaper.contract.MainContract
import com.xin.lovewallpaper.presenter.MainPresenter
import com.xin.lovewallpaper.ui.fragment.DouYinFragment
import com.xin.lovewallpaper.ui.fragment.MagazineTaoTuFragment
import com.xin.lovewallpaper.ui.fragment.SettingFragment
import com.xin.lovewallpaper.ui.fragment.TwitterFragment
import com.xin.lovewallpaper.util.StatusBarUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : BaseMvpActivity<MainContract.View, MainPresenter>(), MainContract.View {

    private lateinit var mFragments: ArrayList<Fragment>
    private var mLastFgIndex: Int = 0
    private var mLastTime = 0L

    override fun initPresenter(): MainPresenter = MainPresenter()

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun initData() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        StatusBarUtils.setStatusColor(
            window,
            ContextCompat.getColor(this, R.color.blue),
            1f
        )
        mFragments = ArrayList()
        initFragment()
        showFragment("杂志桃图", 0)
        initBottomNavigationView()
    }

    private fun initFragment() {
        val magazineTaoTuFragment = MagazineTaoTuFragment()
        val douYinFragment = DouYinFragment()
        val twitterFragment = TwitterFragment()
        val settingFragment = SettingFragment()
        mFragments.add(magazineTaoTuFragment)
        mFragments.add(douYinFragment)
        mFragments.add(twitterFragment)
        mFragments.add(settingFragment)
    }

    private fun initBottomNavigationView() {
        bottom_navigation_view.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.tabPeach -> showFragment("杂志桃图", 0)
                R.id.tabDouYin -> showFragment("抖音网红", 1)
                R.id.tabTwitter -> showFragment("推特图源", 2)
                R.id.tabSetting -> showFragment("设置", 3)
            }
            true
        }
    }

    /**
     * 切换fragment
     *
     * @param title toolbar显示的标题
     * @param position 要显示的fragment的下标
     */
    private fun showFragment(title: String, position: Int) {
        if (position >= mFragments.size) {
            return
        }
        tvToolbarTitle.text = title
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val targetFg = mFragments[position]
        val lastFg = mFragments[mLastFgIndex]
        mLastFgIndex = position
        ft.hide(lastFg)
        if (!targetFg.isAdded) {
            supportFragmentManager.beginTransaction().remove(targetFg)
                .commitAllowingStateLoss()
            ft.add(R.id.fragment_group, targetFg)
        }
        ft.show(targetFg)
        ft.commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - mLastTime > 2000){

            showShortToast("再按一次返回键退出")
            mLastTime = System.currentTimeMillis()
        }else{
            super.onBackPressed()
        }
    }
}
