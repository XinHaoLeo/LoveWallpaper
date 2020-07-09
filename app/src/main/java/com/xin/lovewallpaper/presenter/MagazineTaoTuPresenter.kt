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

package com.xin.lovewallpaper.presenter

import com.xin.lovewallpaper.base.BasePresenter
import com.xin.lovewallpaper.contract.MagazineTaoTuContract
import com.xin.lovewallpaper.http.OnHttpListener
import com.xin.lovewallpaper.http.getContentData
import com.xin.lovewallpaper.http.parseContentData
import org.jsoup.nodes.Document

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
 * @date : 2020/7/4 23:08
 * @desc :
 * @since : xinxiniscool@gmail.com
 */
class MagazineTaoTuPresenter : BasePresenter<MagazineTaoTuContract.View>(),
    MagazineTaoTuContract.Presenter {
    override fun getListContentData(page: Int) {
        getContentData(page, object : OnHttpListener {
            override fun onSuccess(document: Document) {
                getView()?.showListContentData(parseContentData(document))
            }

            override fun onError(errorMsg: String) {
                getView()?.showShortToast(errorMsg)
            }

        })
    }
}