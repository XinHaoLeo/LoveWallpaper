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

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.snackbar.Snackbar

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
 * @date : 2020/7/4 22:51
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
abstract class BaseFragment : Fragment() {

    protected lateinit var mActivity: BaseActivity

    private var mParentView: View? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as BaseActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mParentView == null) {
            mParentView = inflater.inflate(initLayoutView(), container, false)
        } else {
            if (mParentView?.parent != null) {
                val viewGroup = mParentView?.parent as ViewGroup
                viewGroup.removeView(mParentView)
            }
        }
        return mParentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initData()
    }

    @LayoutRes
    protected abstract fun initLayoutView(): Int

    protected abstract fun initEvent()

    protected abstract fun initData()


    protected open fun showShortToast(msg: String) {
        ToastUtils.showShort(msg)
    }

    protected open fun showShortToast(@StringRes msg: Int) {
        ToastUtils.showShort(msg)
    }

    protected open fun showLongToast(msg: String) {
        ToastUtils.showLong(msg)
    }

    protected open fun showLongToast(@StringRes msg: Int) {
        ToastUtils.showLong(msg)
    }

    protected open fun showSnackBar(view: View, msg: String) {
        showSnackBar(view, msg, "知道了")
    }

    protected open fun showSnackBar(view: View, msg: String, actionText: String) {
        val snackBar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        snackBar.setAction(actionText) {
            snackBar.dismiss()
        }
        snackBar.show()
    }

}