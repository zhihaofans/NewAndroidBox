package me.zzhhoo.newandroidbox.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.zhihao.library.android.util.AlertUtil
import io.zhihao.library.android.util.ShareUtil
import io.zhihao.library.android.util.ToastUtil
import me.zzhhoo.newandroidbox.R
import me.zzhhoo.newandroidbox.util.ViewUtil
import me.zzhhoo.newandroidbox.view.ui.theme.NewAndroidBoxTheme

class CalculatetoolActivity : ComponentActivity() {
    private val alertUtil = AlertUtil(this)
    private val shareUtil = ShareUtil(this)
    private val toastUtil = ToastUtil(this)
    private val viewUtil = ViewUtil(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InitView()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Preview(showBackground = true)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun InitView() {
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
                                Text(text = getString(R.string.title_activity_calculatetool))
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
                        viewUtil.getFab() {
                            toastUtil.showShortToast("fab.onClick")
                        }
                    }) {

                }
                Spacer(modifier = Modifier.requiredHeight(10.dp))
                Column(
                    modifier = Modifier
                        .padding(top = 56.dp) // 这里为 内容 添加上边距，与TopAppBar隔开
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InitContentView()
                }
            }
        }
    }

    @Composable
    private fun InitContentView() {
        val listItem = listOf("计算器")
        viewUtil.getListView(listItem) { idx, text ->
            toastUtil.showShortToast("$idx-$text")
        }
    }
}
