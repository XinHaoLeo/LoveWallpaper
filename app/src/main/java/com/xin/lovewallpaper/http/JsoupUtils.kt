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

package com.xin.lovewallpaper.http

import android.util.Log
import com.xin.lovewallpaper.http.bean.ContentData
import org.jsoup.nodes.Document

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
 *@date : 2020/7/4 10:15
 *@since : lightingxin@qq.com
 *@desc :
 */

fun parseContentData(document: Document): ArrayList<ContentData> {
    val list = ArrayList<ContentData>()
    //获取第6个div为class的内容
    val element = document.select("div[class]")[5]
//    val elements = element.select("div[class]")
    val elements = element.getElementsByClass("item col-xs-6 col-sm-4 col-md-3 col-lg-3")
    for (data in elements) {
        val contentData = ContentData()
        for (imgData in data.select("img[class]")) {
            val contentImg = data.select("img[data-original]").first().attr("data-original")
            val contentTitle = data.select("img[alt]").first().attr("alt")
            contentData.contentImg = contentImg
            contentData.contentTitle = contentTitle
//            Log.v(TAG, "paresContentData: contentImg=$contentImg")
//            Log.d(TAG, "paresContentData: contentTitle=$contentTitle")
        }
        for (urlData in data.select("a[class]")) {
            val contentUrl = urlData.select("a[href]").attr("href")
            contentData.contentUrl = contentUrl
//            Log.i(TAG, "paresContentData: contentUrl=$contentUrl")
        }
        for (imgNumData in data.getElementsByClass("item-num")) {
            val html = imgNumData.select("span[class]").html()
            val indexOf = html.indexOf(" ")
            val contentImgNum = html.substring(0, indexOf)
            contentData.contentImgNum = contentImgNum
//            Log.w(TAG, "paresContentData: imgNumData=$contentImgNum")
        }
        list.add(contentData)
    }
    return list
}

fun parseBigImageData(document: Document): ArrayList<String> {
    val list = ArrayList<String>()
    val elements =
        document.getElementsByClass("post-item col-xs-6 col-sm-4 col-md-3 col-lg-3")
    for (data in elements) {
        list.add(data.select("div[data-src]").attr("data-src"))
//        Log.w(TAG, "parseBigImageData: data="+data.select("div[data-src]").attr("data-src"))
    }
    return list
}