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

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Environment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.kongzue.dialog.v3.TipDialog
import com.xin.lovewallpaper.R
import com.xin.lovewallpaper.base.BaseMvpActivity
import com.xin.lovewallpaper.contract.IssueContract
import com.xin.lovewallpaper.presenter.IssuePresenter
import com.xin.lovewallpaper.util.*
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.activity_issue.*
import kotlinx.android.synthetic.main.toolbar.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.*


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
 *@date : 2020/7/8 15:10
 *@since : lightingxin@qq.com
 *@desc :
 */
class IssueActivity : BaseMvpActivity<IssueContract.View, IssuePresenter>(),
    IssueContract.View {


    companion object {
        private const val REQUEST_CODE_SELECT = 99
    }

    private var imageURI: Uri? = null
    private var compressDirPath: String? = null

    override fun initPresenter(): IssuePresenter = IssuePresenter()

    override fun initLayoutView(): Int = R.layout.activity_issue

    override fun initData() {
        tvToolbarTitle.text = "问题反馈"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        StatusBarUtils.setStatusColor(
            window,
            ContextCompat.getColor(this, R.color.blue),
            1f
        )
        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        val compressDir = File(Environment.DIRECTORY_PICTURES)
        if (!compressDir.exists()) {
            compressDir.mkdir()
        }
        compressDirPath = compressDir.toString()
        etInputIssue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //编辑框内容变化之后会调用该方法，s为编辑框内容变化后的内容
                if (s != null) {
                    val inputContent = "${s.length}/150"
                    tvInputLength.text = inputContent
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        llSelectPicture.setOnClickListener {
            Matisse.from(this)
                .choose(MimeType.ofImage())
//                .capture(true)
                .countable(true) //是否显示数字
                .showSingleMediaType(true)
                .countable(true)
                .maxSelectable(1)
                //选择方向
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                //界面中缩略图的质量
                .thumbnailScale(0.85f)
                //蓝色主题
                .theme(R.style.Matisse_Zhihu)
                .imageEngine(NewGlideEngine())
                .forResult(REQUEST_CODE_SELECT)
        }
        btSubmitIssue.setOnClickListener {
            //发送邮件
            val emailText = etInputIssue.text.toString().trim()
            if (emailText == "") {
                showShortToast("您还没有描述任何问题或建议哦")
                return@setOnClickListener
            }
            if (imageURI == null) {
                MailSendUtil.sendTextEmail(emailText, object : EmailStatusListener {
                    override fun onEmailSend(result: Boolean) {
                        if (result) {
                            TipDialog.show(this@IssueActivity, "提交成功，非常感谢！", TipDialog.TYPE.SUCCESS)
                            onBackPressed()
                        } else {
                            TipDialog.show(this@IssueActivity, "问题反馈失败，请稍后重试", TipDialog.TYPE.ERROR)
                        }
                    }
                })
            } else {
                //图片需要压缩才能上传，不然很费流量
                compressImage(
                    FileUtils.getRealFilePath(this, imageURI)!!,
                    object : ImageCompressListener {
                        override fun onSuccess(filePath: String) {
                            MailSendUtil.sendAttachFileEmail(emailText, filePath,
                                object : EmailStatusListener {
                                    override fun onEmailSend(result: Boolean) {
                                        if (result) {
                                            TipDialog.show(
                                                this@IssueActivity,
                                                "提交成功，非常感谢！",
                                                TipDialog.TYPE.SUCCESS
                                            )
                                            onBackPressed()
                                        } else {
                                            TipDialog.show(
                                                this@IssueActivity,
                                                "问题反馈失败，请稍后重试",
                                                TipDialog.TYPE.ERROR
                                            )
                                        }
                                    }
                                })
                        }

                        override fun onFailure(throwable: Throwable) {

                        }
                    })
            }
        }
    }

    //图片压缩
    private fun compressImage(imagePath: String, listener: ImageCompressListener) {
        Luban.with(this).load(imagePath).ignoreBy(100)
            .setTargetDir(compressDirPath).filter { path ->
                !(TextUtils.isEmpty(path) || path?.toLowerCase(Locale.ROOT)
                    ?.endsWith(".gif") as Boolean)
            }.setCompressListener(object : OnCompressListener {
                override fun onSuccess(file: File?) {
                    if (file == null) {
                        return
                    }
                    listener.onSuccess(file.absolutePath)
                }

                override fun onError(e: Throwable?) {
                    listener.onFailure(e!!)
                }

                override fun onStart() {

                }
            }).launch()
    }


}