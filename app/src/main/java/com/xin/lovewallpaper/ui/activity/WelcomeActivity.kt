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

import android.Manifest
import android.content.Intent
import android.os.Build
import android.util.Log
import com.xin.lovewallpaper.R
import com.xin.lovewallpaper.base.BaseActivity
import com.xin.lovewallpaper.widget.AgreementDialog
import pub.devrel.easypermissions.EasyPermissions

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
 *@date : 2020/7/3 14:49
 *@since : lightingxin@qq.com
 *@desc :
 */
class WelcomeActivity : BaseActivity(), EasyPermissions.PermissionCallbacks {
    override fun initLayoutView(): Int = R.layout.activity_welcome

    companion object {
        const val TAG = "WelcomeActivity"
        const val REQUEST_CODE = 99
        val PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA
        )
    }

    override fun initEvent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (EasyPermissions.hasPermissions(this, *PERMISSIONS)) {
                startSplashActivity()
            } else {
                AgreementDialog.Builder()
                    .setContext(this)
                    .setDialogTitle(getString(R.string.agreement_dialog_title))
                    .setDialogMessage(getString(R.string.welcome_use_love_wallpaper))
                    .setOnDialogClickListener(object :
                        AgreementDialog.OnDialogClickListener {
                        override fun onConfirmClick() {
                            EasyPermissions.requestPermissions(
                                this@WelcomeActivity,
                                resources.getString(R.string.app_name) + getString(R.string.permission_message),
                                REQUEST_CODE,
                                *PERMISSIONS
                            )
                        }

                        override fun onCancelClick() {
                            finish()
                        }
                    }).build().show()
            }
        } else {
            startSplashActivity()
        }
    }

    override fun initData() {

    }

    private fun startSplashActivity() {
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        perms.forEach {
            Log.d(TAG, it)
        }
        startSplashActivity()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}