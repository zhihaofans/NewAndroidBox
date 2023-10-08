package me.zzhhoo.newandroidbox.service

import com.google.gson.Gson
import com.orhanobut.logger.Logger
import io.zhihao.library.android.kotlinEx.isNotNullAndEmpty
import io.zhihao.library.android.util.DatetimeUtil
import io.zhihao.library.android.util.SharedPreferencesUtil
import me.zzhhoo.newandroidbox.gson.FavoritesDataGson
import me.zzhhoo.newandroidbox.gson.FavoritesItemGson

class FavoritesService {
    private val dbKey = "me.zzhhoo.newandroidbox.favorites"
    private val sp = SharedPreferencesUtil(dbKey)
    private val gson = Gson()
    fun getFavoritesList(): FavoritesDataGson {
        val dataStr = sp.getString("favorites")
        val data = if (dataStr.isNotNullAndEmpty()) {
            try {
                gson.fromJson(dataStr, FavoritesDataGson::class.java) ?: FavoritesDataGson(
                    mutableListOf()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                FavoritesDataGson(mutableListOf())
            }
        } else {
            FavoritesDataGson(mutableListOf())
        }
        Logger.d(data)
        return data
    }

    fun setFavoritesList(data: MutableList<FavoritesItemGson>): Boolean {
        val oldData = FavoritesDataGson(data = data)
        Logger.d(oldData)
        val dataStr =
            try {
                gson.toJson(oldData) ?: "{data:[]}"
            } catch (e: Exception) {
                e.printStackTrace()
                "{data:[]}"
            }
        return sp.putString("favorites", dataStr)
    }

    fun addItem(text: String): Boolean {
        val oldData = getFavoritesList()
        oldData.data.add(
            FavoritesItemGson(
                text = text,
                type = "text",
                timeStamp = DatetimeUtil.unixTimeStampMill().toInt()
            )
        )
        return setFavoritesList(oldData.data)
    }

    fun deleteIndex(idx: Int): Boolean {
        val oldData = getFavoritesList()
        return if (idx >= oldData.data.size) {
            false
        } else {
            oldData.data.removeAt(idx)
            setFavoritesList(oldData.data)
        }
    }
}