package me.zzhhoo.newandroidbox.util

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import com.orhanobut.logger.Logger
import java.io.BufferedOutputStream
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import io.zhihao.library.android.util.FileUtil


class ImageUtil {
    companion object {
        fun isEmpty(bitmap: Bitmap?): Boolean {
            return bitmap == null || bitmap.width == 0 || bitmap.height == 0
        }

        /**
         * 保存图片到 SDCard
         * @param bitmap   待保存图片
         * @param filePath 存储路径
         * @param format   如 Bitmap.CompressFormat.PNG
         * @param quality  质量
         * @return `true` success, `false` fail
         */
        fun saveImage(
            bitmap: Bitmap?,
            filePath: String,
            format: CompressFormat?,
            quality: Int = 100
        ): Boolean {
            return saveImage(bitmap, FileUtil.getFileByPath(filePath), format, quality)
        }

        /**
         * 保存图片到 SDCard
         * @param bitmap  待保存图片
         * @param file    存储路径
         * @param format  如 Bitmap.CompressFormat.PNG
         * @param quality 质量
         * @return {@code true} success, {@code false} fail
         */
        fun saveImage(
            bitmap: Bitmap?,
            file: File?,
            format: CompressFormat?,
            quality: Int = 100
        ): Boolean {
            if (bitmap == null || file == null || format == null) return false
            // 防止 Bitmap 为 null, 或者创建文件夹失败 ( 文件存在则删除 )
            if (isEmpty(bitmap) || !FileUtil.createFileByDeleteOldFile(file)) return false
            var os: OutputStream? = null
            return try {
                os = BufferedOutputStream(FileOutputStream(file))
                bitmap.compress(format, quality, os)
            } catch (e: Exception) {
                Logger.e("ImageUtil", e, "saveBitmapToSDCard")
                false
            } finally {
                closeIOQuietly(os)
            }
//            return true
        }

        /**
         * 安静关闭 IO
         * @param closeables Closeable[]
         */
        fun closeIOQuietly(vararg closeables: Closeable?) {
            for (closeable in closeables) {
                if (closeable != null) {
                    try {
                        closeable.close()
                    } catch (ignore: java.lang.Exception) {
                    }
                }
            }
        }
    }
}