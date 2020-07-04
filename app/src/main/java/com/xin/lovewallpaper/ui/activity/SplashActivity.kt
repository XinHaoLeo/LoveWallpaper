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
import android.graphics.Bitmap
import android.os.CountDownTimer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.gyf.immersionbar.ImmersionBar
import com.xin.lovewallpaper.R
import com.xin.lovewallpaper.base.BaseMvpActivity
import com.xin.lovewallpaper.contract.SplashContract
import com.xin.lovewallpaper.http.bean.ContentData
import com.xin.lovewallpaper.presenter.SplashPresenter
import kotlinx.android.synthetic.main.activity_splash.*

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
 *@date : 2020/7/4 9:47
 *@since : lightingxin@qq.com
 *@desc :
 */
class SplashActivity : BaseMvpActivity<SplashContract.View, SplashPresenter>(),
    SplashContract.View {

    private lateinit var countDownTimer: CountDownTimer

    override fun initPresenter(): SplashPresenter = SplashPresenter()

    override fun initLayoutView(): Int = R.layout.activity_splash

    override fun initEvent() {
        super.initEvent()
        ImmersionBar.with(this).init()
    }

    override fun initData() {
        mPresenter.getFirstWallpaper(1)
        countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                jumpToMainActivity()
            }

            override fun onTick(millisUntilFinished: Long) {
                val text = "点击跳过\r\r" + millisUntilFinished / 1000
                tvJump.text = text
            }
        }
        //启动倒计时
        countDownTimer.start()

        tvJump.setOnClickListener {
            jumpToMainActivity()
        }
    }

    private fun jumpToMainActivity(){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    override fun showFirstWallpaper(list: ArrayList<ContentData>) {
        if (list.size > 1) {
            val contentImg = list[0].contentImg
//            mSPUtils.put(SPKey.FIRST_WALLPAPER_URL,contentImg)

            //加载大图
            Glide.with(this@SplashActivity).asBitmap().load(contentImg)
                .into(object : BitmapImageViewTarget(ivSplash) {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        ivSplash.setImageBitmap(resource)
                    }
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

}