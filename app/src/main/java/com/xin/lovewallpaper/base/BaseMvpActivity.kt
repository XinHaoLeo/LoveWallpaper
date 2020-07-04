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

package com.xin.lovewallpaper.base

import android.view.View
import androidx.annotation.StringRes
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.snackbar.Snackbar

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
 *@date : 2020/7/3 15:08
 *@since : lightingxin@qq.com
 *@desc :
 */
abstract class BaseMvpActivity<in V : IView, P : IPresenter<in V>> : BaseActivity(), IView {

    protected lateinit var mPresenter: P

    override fun initEvent() {
        mPresenter = initPresenter()
        mPresenter.attachView(this as V)
    }


    protected abstract fun initPresenter(): P

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView(this as V)
    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun showShortToast(msg: String) {
        ToastUtils.showShort(msg)
    }

    override fun showShortToast(@StringRes msg: Int) {
        ToastUtils.showShort(msg)
    }

    override fun showLongToast(msg: String) {
        ToastUtils.showLong(msg)
    }

    override fun showLongToast(@StringRes msg: Int) {
        ToastUtils.showLong(msg)
    }

    override fun showSnackBar(view: View, msg: String) {
        showSnackBar(view, msg, "知道了")
    }

    override fun showSnackBar(view: View, msg: String, actionText: String) {
        val snackBar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        snackBar.setAction(actionText) {
            snackBar.dismiss()
        }
        snackBar.show()
    }
}