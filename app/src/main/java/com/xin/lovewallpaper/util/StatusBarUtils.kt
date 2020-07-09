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

package com.xin.lovewallpaper.util

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.FloatRange

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
 *@date : 2020/7/6 9:47
 *@since : lightingxin@qq.com
 *@desc :
 */
object StatusBarUtils {
    /**
     * 设置状态栏颜色
     */
    fun setStatusColor(
        window: Window,
        color: Int,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float
    ) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = mixtureColor(
                color,
                alpha
            )
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    private fun mixtureColor(color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        val a = if (color and -0x1000000 == 0) 0xff else color ushr 24
        return color and 0x00ffffff or ((a * alpha).toInt() shl 24)
    }

    /**
     * 生成一个与状态栏高度一样并且自定义颜色的矩形
     */
    fun setColor(activity: Activity, color: Int) {
        //限制android系统的版本
        // 设置状态栏透明
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // 生成一个状态栏大小的矩形
        val statusView = createStatusView(activity, color)
        // 添加 statusView 到布局中
        val decorView = activity.window.decorView as ViewGroup
        decorView.addView(statusView)
        // 设置根布局的参数
        val rootView =
            (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(
                0
            ) as ViewGroup
        rootView.fitsSystemWindows = true
        rootView.clipToPadding = true
    }

    /**
     * 生成一个和状态栏大小相同的矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    private fun createStatusView(activity: Activity, color: Int): View {
        // 获得状态栏高度
        val resourceId =
            activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = activity.resources.getDimensionPixelSize(resourceId)
        // 绘制一个和状态栏一样高的矩形
        val statusView = View(activity)
        val params =
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight)
        statusView.layoutParams = params
        statusView.setBackgroundColor(color)
        return statusView
    }
}