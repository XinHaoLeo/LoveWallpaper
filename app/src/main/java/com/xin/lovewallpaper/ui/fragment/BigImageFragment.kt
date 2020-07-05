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

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.kongzue.dialog.util.DialogSettings
import com.kongzue.dialog.v3.BottomMenu
import com.kongzue.dialog.v3.TipDialog
import com.xin.lovewallpaper.R
import com.xin.lovewallpaper.base.BaseFragment
import com.xin.lovewallpaper.util.FileUtils
import kotlinx.android.synthetic.main.fragment_big_image.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
 * @date : 2020/7/5 13:47
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class BigImageFragment : BaseFragment() {

    private var mUrl: String = ""
    override fun initLayoutView(): Int = R.layout.fragment_big_image


    fun setUrl(url: String): BigImageFragment {
        mUrl = url
        return this
    }

    override fun initEvent() {
        //加载大图 try catch防止挂了
        try {
            Glide.with(mActivity).asBitmap()
                .load(mUrl)
                .into(object : BitmapImageViewTarget(photoView) {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        photoView.setImageBitmap(resource)
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun initData() {
        photoView.setOnLongClickListener {
            BottomMenu.build(mActivity)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setMenuTextList(arrayOf("保存图片", "设为桌面壁纸", "设为锁屏壁纸"))
                .setOnMenuItemClickListener { _, index ->
                    when (index) {
                        0 -> {
                            GlobalScope.launch(Dispatchers.Main) {
                                val drawable = withContext(Dispatchers.IO) {
                                    Glide.with(mActivity).load(mUrl)
                                        .apply(RequestOptions().centerCrop())
                                        .submit()
                                        .get() as BitmapDrawable
                                }
                                if (FileUtils.saveImage(mActivity, drawable)) {
                                    TipDialog.show(mActivity, "保存成功", TipDialog.TYPE.SUCCESS)
                                } else {
                                    TipDialog.show(mActivity, "保存失败", TipDialog.TYPE.ERROR)
                                }
                            }
                        }
                        1 -> {
                            Glide.with(mActivity).asBitmap().load(mUrl)
                                .into(object : SimpleTarget<Bitmap>() {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                    ) {
                                        try {
                                            val wallpaperManager =
                                                WallpaperManager.getInstance(mActivity)
                                            val screenWidth = ScreenUtils.getScreenWidth()
                                            val screenHeight = ScreenUtils.getScreenHeight()
                                            wallpaperManager.suggestDesiredDimensions(
                                                screenWidth,
                                                screenHeight
                                            )
                                            GlobalScope.launch(Dispatchers.Main) {
                                                withContext(Dispatchers.IO) {
                                                    wallpaperManager.setBitmap(resource)
                                                }
                                            }
                                            TipDialog.show(
                                                mActivity,
                                                "设置成功",
                                                TipDialog.TYPE.SUCCESS
                                            )
                                        } catch (e: Exception) {
                                            TipDialog.show(
                                                mActivity,
                                                "设置失败",
                                                TipDialog.TYPE.ERROR
                                            )
                                            e.printStackTrace()
                                        }
                                    }
                                })
                        }
                        2 -> {
                            Glide.with(mActivity).asBitmap().load(mUrl)
                                .into(object : SimpleTarget<Bitmap>() {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                    ) {
                                        try {
                                            val wallpaperManager =
                                                WallpaperManager.getInstance(mActivity)
                                            val clazz: Class<*> =
                                                wallpaperManager.javaClass
                                            val method = clazz.getMethod(
                                                "setBitmapToLockWallpaper",
                                                Bitmap::class.java
                                            )
                                            method.invoke(wallpaperManager, resource)
                                            TipDialog.show(
                                                mActivity,
                                                "设置成功",
                                                TipDialog.TYPE.SUCCESS
                                            )
                                        } catch (e: Exception) {
                                            showShortToast(e.toString())
                                            TipDialog.show(
                                                mActivity,
                                                "设置失败",
                                                TipDialog.TYPE.ERROR
                                            )
                                            e.printStackTrace()
                                        }
                                    }
                                })
                        }
                    }
                }.show()
            true
        }
    }
}