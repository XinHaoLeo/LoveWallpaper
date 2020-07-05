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
import com.xin.lovewallpaper.contract.MagazineTaoTuContract
import com.xin.lovewallpaper.http.bean.ContentData
import com.xin.lovewallpaper.presenter.MagazineTaoTuPresenter
import com.xin.lovewallpaper.ui.activity.BigImageActivity
import com.xin.lovewallpaper.ui.adapter.MagazineTaoTuAdapter
import kotlinx.android.synthetic.main.fragment_magazine_taotu.*

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
 * @date : 2020/7/4 22:50
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class MagazineTaoTuFragment : BaseMvpFragment<MagazineTaoTuContract.View, MagazineTaoTuPresenter>(),
    MagazineTaoTuContract.View {

    private lateinit var mContentDataList: ArrayList<ContentData>
    private lateinit var mAdapter: MagazineTaoTuAdapter
    private var page = 1

    override fun initPresenter(): MagazineTaoTuPresenter = MagazineTaoTuPresenter()

    override fun initLayoutView(): Int = R.layout.fragment_magazine_taotu

    override fun initData() {
        //默认设置五秒钟,等解析完成立马取消
        TipDialog.showWait(mActivity, "小鑫正在为您努力加载中...").setTipTime(5000)
        mPresenter.getListContentData(page)
        mContentDataList = ArrayList()
        mAdapter = MagazineTaoTuAdapter(mActivity, mContentDataList)
        rvContent.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvContent.adapter = mAdapter
        //不做下拉刷新操作,下拉刷新请求过于频繁
        srfPeach.setEnableRefresh(false)
        srfPeach.setOnLoadMoreListener {
            page++
            mPresenter.getListContentData(page)
            it.finishLoadMore()
        }
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

    override fun showListContentData(list: ArrayList<ContentData>) {
        TipDialog.dismiss()
        mContentDataList.addAll(list)
        mAdapter.notifyDataSetChanged()
    }


//    fun filterNumber(number: String): Int {
//        val s = number.replace("[^(0-9)]".toRegex(), "")
//        return s.toInt()
//    }
}