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

package com.xin.lovewallpaper.ui.fragment

import android.content.Intent
import android.text.TextUtils
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kongzue.dialog.v3.TipDialog
import com.xin.lovewallpaper.R
import com.xin.lovewallpaper.app.Constant
import com.xin.lovewallpaper.base.BaseMvpFragment
import com.xin.lovewallpaper.contract.DouYinContract
import com.xin.lovewallpaper.http.bean.ContentData
import com.xin.lovewallpaper.presenter.DouYinPresenter
import com.xin.lovewallpaper.ui.activity.BigImageActivity
import com.xin.lovewallpaper.ui.adapter.MagazineTaoTuAdapter
import kotlinx.android.synthetic.main.fragment_dou_yin.*

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
 * @date : 2020/7/4 23:55
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class DouYinFragment : BaseMvpFragment<DouYinContract.View, DouYinPresenter>(),
    DouYinContract.View {

    private lateinit var mContentDataList: ArrayList<ContentData>
    private lateinit var mAdapter: MagazineTaoTuAdapter

    override fun initPresenter(): DouYinPresenter = DouYinPresenter()

    override fun initLayoutView(): Int = R.layout.fragment_dou_yin

    override fun initData() {
        TipDialog.showWait(mActivity, "小鑫正在为您努力加载中...").setTipTime(5000)
        mPresenter.getListDouYinData()
        mContentDataList = ArrayList()
        //布局一样,复用首页adapter
        mAdapter = MagazineTaoTuAdapter(mActivity, mContentDataList)
        rvDouYin.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvDouYin.adapter = mAdapter
        mAdapter.setOnItemClickListener(object : MagazineTaoTuAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val contentData = mContentDataList[position]
                if (TextUtils.isEmpty(contentData.contentUrl)) {
                    TipDialog.show(mActivity, "资源未找到,访问失败", TipDialog.TYPE.ERROR)
                } else {
                    val intent = Intent(mActivity, BigImageActivity::class.java)
                    intent.putExtra(Constant.IMAGE_URL, contentData.contentUrl)
//                    intent.putExtra(Constant.IMAGE_NUM, filterNumber(contentData.contentImgNum))
                    startActivity(intent)
                }
            }

        })
    }

    override fun showListDouYinData(list: ArrayList<ContentData>) {
        TipDialog.dismiss()
        mContentDataList.addAll(list)
        mAdapter.notifyDataSetChanged()
    }
}