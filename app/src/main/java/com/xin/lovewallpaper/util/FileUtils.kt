package com.xin.lovewallpaper.util

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images.ImageColumns
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.math.RoundingMode
import java.text.DecimalFormat


/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO
 * @date: 2020/6/15 16:08
 */
class FileUtils {
    companion object {
        private const val Tag = "FileUtil"

        /**
         * 保存壁纸
         *
         * Android 10 创建文件夹需要声明 android:requestLegacyExternalStorage="true"
         * */
        fun saveImage(context: Context, drawable: BitmapDrawable): Boolean {
            val bitmap = drawable.bitmap
            //创建保存壁纸的文件夹
            val wallpaperDir = File(Environment.getExternalStorageDirectory(), "LoveWallpaper")
            if (!wallpaperDir.exists()) {
                wallpaperDir.mkdir()
            }
            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(wallpaperDir, fileName)
            try {
                //保存图片
                val fos = FileOutputStream(file)
                val isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                fos.close()

                //保存图片后发送广播通知更新数据库
                try {
                    MediaStore.Images.Media.insertImage(
                        context.contentResolver,
                        file.absolutePath,
                        fileName,
                        null
                    )
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                // 通知图库更新
                context.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + "/sdcard/namecard/")
                    )
                )
                return isSuccess
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        //获取文件路径下文件大小
        fun getFileSize(file: File?): Long {
            var size = 0L
            if (file == null) {
                return size
            }
            val files = file.listFiles()
            if (files!=null){
                for (i in files.indices) {
                    size += if (files[i].isDirectory) {
                        getFileSize(files[i])
                    } else {
                        files[i].length()
                    }
                }
            }
            return size
        }

        fun formatFileSize(size: Long?): String {
            val fileSizeString: String
            val decimalFormat = DecimalFormat("0.00")
            decimalFormat.roundingMode = RoundingMode.HALF_UP;
            fileSizeString = when {
                size == null -> {
                    decimalFormat.format(0) + "B"
                }
                size < 1024 -> {
                    decimalFormat.format(size) + "B"
                }
                size < 1048576 -> {
                    decimalFormat.format((size.toDouble() / 1024)) + "K"
                }
                size < 1073741824 -> {
                    decimalFormat.format((size.toDouble() / 1048576)) + "M"
                }
                else -> {
                    decimalFormat.format((size.toDouble() / 1073741824)) + "G"
                }
            }
            return fileSizeString
        }

        //file：要删除的文件夹的所在位置
        fun delete(file: File): Boolean {
            if (file.isDirectory) {
                return deleteDir(file)
            }
            return deleteFile(file)
        }

        /**
         * Delete the directory.
         *
         * @param dir The directory.
         * @return `true`: success<br></br>`false`: fail
         */
        private fun deleteDir(dir: File?): Boolean {
            if (dir == null) return false
            // dir doesn't exist then return true
            if (!dir.exists()) return true
            // dir isn't a directory then return false
            if (!dir.isDirectory) return false
            val files = dir.listFiles()
            if (files != null && files.isNotEmpty()) {
                for (file in files) {
                    if (file.isFile) {
                        if (!file.delete()) return false
                    } else if (file.isDirectory) {
                        if (!deleteDir(file)) return false
                    }
                }
            }
            return dir.delete()
        }

        /**
         * Delete the file.
         *
         * @param file The file.
         * @return `true`: success<br></br>`false`: fail
         */
        private fun deleteFile(file: File?): Boolean {
            return file != null && (!file.exists() || file.isFile && file.delete())
        }

        fun getRealFilePath(context: Context, uri: Uri?): String? {
            if (null == uri) return null
            val scheme = uri.scheme
            var data: String? = null
            if (scheme == null) data =
                uri.path else if (ContentResolver.SCHEME_FILE == scheme) {
                data = uri.path
            } else if (ContentResolver.SCHEME_CONTENT == scheme) {
                val cursor: Cursor? = context.contentResolver
                    .query(uri, arrayOf(ImageColumns.DATA), null, null, null)
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        val index: Int = cursor.getColumnIndex(ImageColumns.DATA)
                        if (index > -1) {
                            data = cursor.getString(index)
                        }
                    }
                    cursor.close()
                }
            }
            return data
        }
    }
}