package me.zzhhoo.newandroidbox.gson

data class FavoritesDataGson(
    val data: MutableList<FavoritesItemGson>
)

data class FavoritesItemGson(
    val text: String,
    val type: String,
    val timeStamp: Int,
)