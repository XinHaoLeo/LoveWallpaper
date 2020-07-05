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

import com.gyf.immersionbar.ImmersionBar
import com.kongzue.dialog.v3.TipDialog
import com.xin.lovewallpaper.R
import com.xin.lovewallpaper.app.Constant
import com.xin.lovewallpaper.base.BaseMvpActivity
import com.xin.lovewallpaper.contract.BigImageContract
import com.xin.lovewallpaper.presenter.BigImagePresenter
import com.xin.lovewallpaper.ui.adapter.BigImageAdapter
import com.xin.lovewallpaper.ui.fragment.BigImageFragment
import kotlinx.android.synthetic.main.activity_big_image.*

/**
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 * @author : Leo
 * @date : 2020/7/5 12:57
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class BigImageActivity : BaseMvpActivity<BigImageContract.View, BigImagePresenter>(),
    BigImageContract.View {

    private lateinit var mAdapter: BigImageAdapter
//    private lateinit var mFragmentList: ArrayList<BaseFragment>

    override fun initPresenter(): BigImagePresenter = BigImagePresenter()

    override fun initLayoutView(): Int = R.layout.activity_big_image


    override fun initData() {
        ImmersionBar.with(this).init()
        //默认设置五秒钟,等解析完成立马取消
        TipDialog.showWait(this, "小鑫正在为您努力加载中...").setTipTime(5000)
        val url = intent.getStringExtra(Constant.IMAGE_URL)
//        val imageNum = intent.getIntExtra(Constant.IMAGE_NUM, 0)
//        Log.d("BigImageActivity", "initEvent: $imageNum")
//        mFragmentList = ArrayList()
//        for (index in 1..imageNum) {
//            mFragmentList.add(BigImageFragment())
//        }
        mAdapter = BigImageAdapter(
            supportFragmentManager,
            androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        bigImageViewPager.adapter = mAdapter
        showSnackBar(bigImageViewPager, "长按图片可以保存高清壁纸哦~")
        url?.let {
            mPresenter.getListImageUrl(url)
        }
    }


    override fun showListImageUrl(list: ArrayList<String>) {
        TipDialog.dismiss()
        list.forEach {
            BigImageFragment().setUrl(it).apply { mAdapter.addFragment(this) }
        }
    }

}