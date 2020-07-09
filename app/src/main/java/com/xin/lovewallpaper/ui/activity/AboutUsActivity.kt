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
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.AppUtils
import com.xin.lovewallpaper.R
import com.xin.lovewallpaper.base.BaseActivity
import com.xin.lovewallpaper.util.StatusBarUtils
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlinx.android.synthetic.main.toolbar.*

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
 *@date : 2020/7/8 10:18
 *@since : lightingxin@qq.com
 *@desc :
 */
class AboutUsActivity : BaseActivity() {

    override fun initLayoutView(): Int = R.layout.activity_about_us

    override fun initEvent() {
        tvToolbarTitle.text = "关于我们"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        StatusBarUtils.setStatusColor(
            window,
            ContextCompat.getColor(this, R.color.blue),
            1f
        )
        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        val versionName = "软件当前版本:" + AppUtils.getAppVersionName()
        tvVersion.text = versionName
        rlPrivacy.setOnClickListener {
            startActivity(Intent(this, PrivacyActivity::class.java))
        }
        rlDisclaimer.setOnClickListener {
            startActivity(Intent(this, DisclaimerActivity::class.java))
        }
    }

    override fun initData() {

    }
}