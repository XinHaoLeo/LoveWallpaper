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

import android.content.Intent
import android.text.TextUtils
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kongzue.dialog.v3.TipDialog
import com.xin.lovewallpaper.R
import com.xin.lovewallpaper.app.Constant
import com.xin.lovewallpaper.base.BaseActivity
import com.xin.lovewallpaper.http.bean.ContentData
import com.xin.lovewallpaper.ui.adapter.MagazineTaoTuAdapter
import kotlinx.android.synthetic.main.activity_keyword.*

/**
 *
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 *@author : Leo
 *@date : 2020/7/7 9:55
 *@since : lightingxin@qq.com
 *@desc :
 */
class KeywordActivity : BaseActivity() {

    override fun initLayoutView(): Int = R.layout.activity_keyword

    override fun initEvent() {

    }

    override fun initData() {
        val json = intent.getStringExtra(Constant.SEARCH_DATA)
        val type = object : TypeToken<ArrayList<ContentData>>() {}.type
        val list: ArrayList<ContentData> = Gson().fromJson(json, type)
        //布局一样,复用首页adapter
        val taoTuAdapter = MagazineTaoTuAdapter(this, list)
        rvKeyword.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvKeyword.adapter = taoTuAdapter
        taoTuAdapter.setOnItemClickListener(object : MagazineTaoTuAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val contentData = list[position]
                if (TextUtils.isEmpty(contentData.contentUrl)) {
                    TipDialog.show(this@KeywordActivity, "资源未找到,访问失败", TipDialog.TYPE.ERROR)
                } else {
                    val intent = Intent(this@KeywordActivity, BigImageActivity::class.java)
                    intent.putExtra(Constant.IMAGE_URL, contentData.contentUrl)
//                    intent.putExtra(Constant.IMAGE_NUM, filterNumber(contentData.contentImgNum))
                    startActivity(intent)
                }
            }

        })
    }

}