package me.zzhhoo.newandroidbox.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.zhihao.library.android.kotlinEx.isNotNullAndEmpty
import io.zhihao.library.android.util.AlertUtil
import io.zhihao.library.android.util.ShareUtil
import io.zhihao.library.android.util.ToastUtil
import me.zzhhoo.newandroidbox.R
import me.zzhhoo.newandroidbox.util.ViewUtil
import me.zzhhoo.newandroidbox.view.ui.theme.NewAndroidBoxTheme

class TodoActivity : ComponentActivity() {
    private val alertUtil = AlertUtil(this)
    private val shareUtil = ShareUtil(this)
    private val toastUtil = ToastUtil(this)
    private val viewUtil = ViewUtil(this)
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
        val text = remember { mutableStateOf("") }
        NewAndroidBoxTheme {
            Scaffold(
                Modifier.layoutId("appBar"),
                topBar = {
                    TopAppBar(
                        modifier = Modifier
                            .padding(bottom = 0.dp) // 这里为 TopAppBar 添加上边距
                            .fillMaxWidth(),
                        title = {
                            Text(text = getString(R.string.title_activity_todo))
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
                    viewUtil.getFab(Icons.Filled.Add) {
                        alertUtil.showInputAlert("加个收藏夹内容", "") { inputText, _ ->

                        }
                    }
                }
            ) {
                Spacer(modifier = Modifier.requiredHeight(10.dp))
                Column(
                    modifier = Modifier
                        .padding(top = 56.dp) // 这里为 内容 添加上边距，与TopAppBar隔开
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = text.value,
                        maxLines = 1,
                        onValueChange = {
                            text.value = it
                        },
                        label = { Text("二维码内容") }
                    )
                    viewUtil.getButton(id = "button_sharetext", title = "分享文本") {
                        if (text.value.isNotNullAndEmpty()) {
//                        shareText(text.value)
                            shareUtil.shareText(text.value)
                        }
                    }

                }
            }
        }
    }

}
