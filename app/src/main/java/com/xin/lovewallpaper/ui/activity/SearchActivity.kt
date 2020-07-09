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
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.kongzue.dialog.util.BaseDialog
import com.kongzue.dialog.util.DialogSettings
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.TipDialog
import com.xin.lovewallpaper.R
import com.xin.lovewallpaper.app.Constant
import com.xin.lovewallpaper.base.BaseActivity
import com.xin.lovewallpaper.http.OnHttpListener
import com.xin.lovewallpaper.http.getSearchData
import com.xin.lovewallpaper.http.parseContentData
import com.xin.lovewallpaper.util.DaoUtils
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.nodes.Document


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
 *@date : 2020/7/6 16:55
 *@since : lightingxin@qq.com
 *@desc :
 */
class SearchActivity : BaseActivity(), DaoUtils.OnNotifyDataChangedListener {

    private var mSearchRecordList: ArrayList<String>? = null
    private var mAdapter: TagAdapter<String>? = null

    override fun initLayoutView(): Int = R.layout.activity_search

    override fun initEvent() {
        DaoUtils.init(this)
        initOnClick()
        initAdapter()
    }

    override fun initData() {
        querySearchRecords()
        DaoUtils.setOnNotifyDataChanged(this)
    }

    private fun querySearchRecords() {
        GlobalScope.launch(Dispatchers.IO) {
            mSearchRecordList = DaoUtils.getRecordsList()
            withContext(Dispatchers.Main) {
                mAdapter?.setData(mSearchRecordList)
                mAdapter?.notifyDataChanged()
                if (mSearchRecordList?.size == 0) {
                    llHistory.visibility = View.GONE
                } else {
                    llHistory.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun notifyDataChanged() {
        querySearchRecords()
    }

    private fun initOnClick() {
        ivClearAllHistory.setOnClickListener {
            MessageDialog.build(this@SearchActivity)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setTitle("提示")
                .setMessage("确定删除所有历史搜索记录吗?")
                .setOkButton("确定") { baseDialog: BaseDialog, _: View? ->
                    DaoUtils.deleteAllRecords()
                    llHistory.visibility = View.GONE
                    baseDialog.doDismiss()
                    false
                }
                .setCancelButton("取消") { baseDialog: BaseDialog, _: View? ->
                    baseDialog.doDismiss()
                    false
                }.show()

        }
        tvSearch.setOnClickListener {
            startSearch()
        }
        ivClear.setOnClickListener {
            val record = etQuery.text.toString()
            if (record.isNotEmpty()) {
                val selectionStart = etQuery.selectionStart
                if (selectionStart > 0) {
                    etQuery.text?.delete(selectionStart - 1, selectionStart)
                    etQuery.setSelection(selectionStart - 1)
                }
            }
        }
        ivClear.setOnLongClickListener {
            etQuery.text?.clear()
            true
        }
        ivBack.setOnClickListener { onBackPressed() }
    }

    private fun startSearch() {
        val record: String = etQuery.text.toString()
        if (!TextUtils.isEmpty(record)) {
            TipDialog.showWait(this, "小鑫正在为您努力加载中...").setTipTime(5000)
            DaoUtils.addRecords(record)
            etQuery.text?.clear()
            getSearchData(record, object : OnHttpListener {
                override fun onSuccess(document: Document) {
                    TipDialog.dismiss()
                    val list = parseContentData(document)
                    if (list.size > 0) {
                        val intent = Intent(this@SearchActivity, KeywordActivity::class.java)
                        val json = Gson().toJson(list)
                        intent.putExtra(Constant.SEARCH_DATA, json)
                        startActivity(intent)
                        onBackPressed() //这里应该结束搜索界面
                    } else {
                        TipDialog.show(this@SearchActivity, "抱歉暂无搜索到数据哦", TipDialog.TYPE.ERROR)
                    }
                }

                override fun onError(errorMsg: String) {
                    TipDialog.show(this@SearchActivity, errorMsg, TipDialog.TYPE.ERROR)
                }

            })
        } else {
            showShortToast("哎呀，什么都还没输入呢~")
        }
    }

    private fun initAdapter() {
        mAdapter = object : TagAdapter<String>(mSearchRecordList) {
            override fun getView(parent: FlowLayout, position: Int, content: String): View {
                val tvHistory = LayoutInflater.from(this@SearchActivity).inflate(
                    R.layout.item_history,
                    parent, false
                ) as TextView
                tvHistory.text = content
                return tvHistory
            }

        }
        flowLayout.adapter = mAdapter
        flowLayout.setOnTagClickListener { _, position, _ ->
            etQuery.setText("")
            etQuery.setText(mSearchRecordList?.get(position))
            etQuery.setSelection(etQuery.length())
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> startSearch()
            KeyEvent.KEYCODE_BACK -> onBackPressed()
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        DaoUtils.closeDatabase()
        DaoUtils.removeNotifyDataChanged()
    }

}