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
import android.view.View
import com.kongzue.dialog.util.BaseDialog
import com.kongzue.dialog.util.DialogSettings
import com.kongzue.dialog.v3.MessageDialog
import com.kongzue.dialog.v3.TipDialog
import com.xin.lovewallpaper.R
import com.xin.lovewallpaper.base.BaseFragment
import com.xin.lovewallpaper.ui.activity.AboutUsActivity
import com.xin.lovewallpaper.ui.activity.IssueActivity
import com.xin.lovewallpaper.util.FileUtils
import kotlinx.android.synthetic.main.fragment_setting.*
import java.io.File

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
 * @date : 2020/7/5 0:00
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class SettingFragment : BaseFragment() {

    private lateinit var mFile: File

    override fun initLayoutView(): Int = R.layout.fragment_setting

    override fun initEvent() {
        rlCleanCache.setOnClickListener {
            MessageDialog.build(mActivity)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setTitle("提示")
                .setMessage("图片缓存可以有效节省流量,确定清空缓存吗")
                .setOkButton("确定") { baseDialog: BaseDialog, _: View? ->
                    val isDelete = FileUtils.delete(mFile)
                    if (isDelete) {
                        TipDialog.show(mActivity, "清除成功", TipDialog.TYPE.SUCCESS)
                    } else {
                        TipDialog.show(mActivity, "清除失败", TipDialog.TYPE.SUCCESS)
                    }
                    tvCacheSize.text = FileUtils.formatFileSize(FileUtils.getFileSize(mFile))
                    baseDialog.doDismiss()
                    false
                }
                .setCancelButton("取消") { baseDialog: BaseDialog, _: View? ->
                    baseDialog.doDismiss()
                    false
                }.show()
        }
        rlIssue.setOnClickListener {
            startActivity(Intent(mActivity,IssueActivity::class.java))
        }
        rlAboutUs.setOnClickListener {
            startActivity(Intent(mActivity, AboutUsActivity::class.java))
        }
    }

    override fun initData() {
        mFile = File(mActivity.cacheDir.absolutePath)
        val fileSize = FileUtils.getFileSize(mFile)
        tvCacheSize.text = FileUtils.formatFileSize(fileSize)
    }

    override fun onResume() {
        super.onResume()
        //保证每次看完图片再次来到这个fragment能够重新获取缓存数据大小
        val fileSize = FileUtils.getFileSize(mFile)
        tvCacheSize.text = FileUtils.formatFileSize(fileSize)
    }
}