package me.zzhhoo.newandroidbox.util

import com.orhanobut.logger.Logger
import java.io.File
import java.io.IOException


class FileUtil {
    companion object {
        /**
         * 判断文件是否存在, 存在则在创建之前删除
         * @param file 文件
         * @return `true` 创建成功, `false` 创建失败
         */
        fun createFileByDeleteOldFile(file: File?): Boolean {
            if (file == null) return false
            // 文件存在并且删除失败返回 false
            if (file.exists() && !file.delete()) return false
            // 创建目录失败返回 false
            return if (!createOrExistsDir(file.parentFile)) false else try {
                file.createNewFile()
            } catch (e: IOException) {
                Logger.e("FileUtil", e, "createFileByDeleteOldFile")
                false
            }
        }

        /**
         * 判断目录是否存在, 不存在则判断是否创建成功
         * @param dirPath 目录路径
         * @return `true` 存在或创建成功, `false` 不存在或创建失败
         */
        fun createOrExistsDir(dirPath: String?): Boolean {
            return createOrExistsDir(getFileByPath(dirPath))
        }

        /**
         * 判断目录是否存在, 不存在则判断是否创建成功
         * @param file 文件
         * @return `true` 存在或创建成功, `false` 不存在或创建失败
         */
        fun createOrExistsDir(file: File?): Boolean {
            // 如果存在, 是目录则返回 true, 是文件则返回 false, 不存在则返回是否创建成功
            return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
        }

        /**
         * 获取文件
         * @param filePath 文件路径
         * @return 文件 [File]
         */
        fun getFileByPath(filePath: String?): File? {
            return if (filePath != null) File(filePath) else null
        }
    }
}