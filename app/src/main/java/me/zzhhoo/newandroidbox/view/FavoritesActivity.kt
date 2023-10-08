package me.zzhhoo.newandroidbox.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import io.zhihao.library.android.kotlinEx.isNotNullAndEmpty
import io.zhihao.library.android.util.AlertUtil
import io.zhihao.library.android.util.ClipboardUtil
import io.zhihao.library.android.util.ShareUtil
import me.zzhhoo.newandroidbox.R
import me.zzhhoo.newandroidbox.service.FavoritesService
import me.zzhhoo.newandroidbox.view.ui.theme.NewAndroidBoxTheme

class FavoritesActivity : ComponentActivity() {
    private val alertUtil = AlertUtil(this@FavoritesActivity)
    private val shareUtil = ShareUtil(this@FavoritesActivity)
    private val favoritesService = FavoritesService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            initView()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun initView() {
        NewAndroidBoxTheme {
            MaterialTheme {
                Scaffold(
                    Modifier.layoutId("appBar"),
                    topBar = {
                        TopAppBar(
                            modifier = Modifier
                                .padding(bottom = 0.dp) // 这里为 TopAppBar 添加上边距
                                .fillMaxWidth(),
                            title = {
                                Text(text = getString(R.string.title_activity_favorites))
                            },
                            actions = {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null
                                )
                            },
                        )
                    },
                    floatingActionButton = {
                        getFab(Icons.Filled.Add) {
                            alertUtil.showInputAlert("加个收藏夹内容", "") { text, _ ->
                                if (text.isNotNullAndEmpty()) {
                                    val addResult = favoritesService.addItem(text)
                                    showToast(
                                        "添加" + if (addResult) {
                                            "成功"
                                        } else {
                                            "失败"
                                        }
                                    )
                                } else {
                                    showToast("没输入内容捏")
                                }
                            }
                        }
                    }) {}
                Column(
                    modifier = Modifier
                        .padding(top = 56.dp) // 这里为 内容 添加上边距，与TopAppBar隔开
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    getButton(id = "button_fav_show", title = "给俺看收藏夹") {
                        val favData = favoritesService.getFavoritesList()

                        alertUtil.showListAlert(
                            getString(R.string.title_activity_favorites) + "(${favData.data.size}个)",
                            favData.data.map {
                                it.text
                            }.toTypedArray()
                        ) { _, idx ->
                            val selectTextData = favData.data[idx]
                            alertUtil.showListAlert(
                                "你要干嘛",
                                arrayOf("分享", "复制", "删除")
                            ) { dialog, index ->
                                when (index) {
                                    0 -> shareUtil.shareText(selectTextData.text)
                                    1 -> ClipboardUtil.copy(selectTextData.text)
                                    2 -> favoritesService.deleteIndex(idx)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun getFab(icon: ImageVector? = Icons.Filled.Add, onClick: () -> Unit) {
        FloatingActionButton(
            onClick = { onClick.invoke() },
        ) {
            Icon(icon ?: Icons.Filled.Add, "Floating action button.")
        }
    }

    @Composable
    private fun getButton(id: String, title: String, onClick: () -> Unit) {
        Button(modifier = Modifier.layoutId(id),
            onClick = { onClick.invoke() }
        ) {
            Text(title)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this@FavoritesActivity, text, Toast.LENGTH_SHORT).show()
    }
}
