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
import com.xin.lovewallpaper.app.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Connection
import org.jsoup.Jsoup
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
 *@date : 2020/7/4 10:51
 *@since : lightingxin@qq.com
 *@desc :
 */

const val TAG = "Utils"
private fun obtainAgent(): String = Constant.UA[Random().nextInt(15)]

fun createConnection(url: String): Connection = Jsoup.connect(url)
    .userAgent(obtainAgent())
    .timeout(30 * 1000)
    .ignoreHttpErrors(true)

fun getContentData(page: Int, listener: OnHttpListener) {
    val url = Constant.contentUrl.replace("indexPage", page.toString())
    GlobalScope.launch(Dispatchers.Main) {
        val statusCode = withContext(Dispatchers.IO) {
            createConnection(url).execute().statusCode()
        }
        Log.d(TAG, "getContentData: statusCode=$statusCode")
        if (statusCode == 200) {
            listener.onSuccess(withContext(Dispatchers.IO) {
                createConnection(url).get()
            })
        } else {
            listener.onError("获取失败")
        }
    }
}
fun getBigImageData(url: String, listener: OnHttpListener) {
    GlobalScope.launch(Dispatchers.Main) {
        val statusCode = withContext(Dispatchers.IO) {
            createConnection(url).execute().statusCode()
        }
        Log.d(TAG, "getBigImageData: statusCode=$statusCode")
        if (statusCode == 200) {
            listener.onSuccess(withContext(Dispatchers.IO) {
                createConnection(url).get()
            })
        } else {
            listener.onError("获取失败")
        }
    }
}

fun getDouYinData(listener: OnHttpListener) {
    val url = Constant.douYinUrl
    GlobalScope.launch(Dispatchers.Main) {
        val statusCode = withContext(Dispatchers.IO) {
            createConnection(url).execute().statusCode()
        }
        Log.d(TAG, "getDouYinData: statusCode=$statusCode")
        if (statusCode == 200) {
            listener.onSuccess(withContext(Dispatchers.IO) {
                createConnection(url).get()
            })
        } else {
            listener.onError("获取失败")
        }
    }
}

fun getTwitterData(page: Int, listener: OnHttpListener) {
    val url = Constant.twitterUrl.replace("indexPage", page.toString())
    GlobalScope.launch(Dispatchers.Main) {
        val statusCode = withContext(Dispatchers.IO) {
            createConnection(url).execute().statusCode()
        }
        Log.d(TAG, "getTwitterData: statusCode=$statusCode")
        if (statusCode == 200) {
            listener.onSuccess(withContext(Dispatchers.IO) {
                createConnection(url).get()
            })
        } else {
            listener.onError("获取失败")
        }
    }
}

fun getSearchData(name:String,listener: OnHttpListener){
    val url = Constant.searchUrl.replace("name", name)
    GlobalScope.launch(Dispatchers.Main) {
        val statusCode = withContext(Dispatchers.IO) {
            createConnection(url).execute().statusCode()
        }
        Log.d(TAG, "getSearchData: statusCode=$statusCode")
        if (statusCode == 200) {
            listener.onSuccess(withContext(Dispatchers.IO) {
                createConnection(url).get()
            })
        } else {
            listener.onError("获取失败")
        }
    }
}