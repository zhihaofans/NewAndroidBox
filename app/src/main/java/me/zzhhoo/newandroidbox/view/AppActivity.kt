package me.zzhhoo.newandroidbox.view

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orhanobut.logger.Logger
import io.zhihao.library.android.kotlinEx.getAppName
import io.zhihao.library.android.util.AppUtil
import me.zzhhoo.newandroidbox.R
import me.zzhhoo.newandroidbox.view.ui.theme.NewAndroidBoxTheme


class AppActivity : ComponentActivity() {
    private var appList = mutableListOf<Map<String, Any>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            init()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    private fun initView(packs: List<ApplicationInfo>) {
        setContent {
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
                                    Text(text = getString(R.string.title_activity_app))
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
                            FloatingActionButton(onClick = {
                                /* TODO:fab按钮 */
                            }) {
                                Icons.Default.Search
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
                        getListView(list = packs.map { it.loadLabel(packageManager).toString() })
                    }
                }
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this@AppActivity, text, Toast.LENGTH_SHORT).show()
    }


    @Composable
    private fun getButton(id: String, title: String?, onClick: () -> Unit) {
        Button(modifier = Modifier.layoutId(id),
            onClick = { onClick.invoke() }
        ) {
            Logger.d(title)
            Text(title ?: "未知")
        }
    }

    @Composable
    private fun getListView(list: List<String>) {
        LazyColumn {
            items(list) { item ->
                getButton(id = "button_app_${item}", title = item) {

                }
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun init() {
        val pm = packageManager
        //得到PackageManager对象
        appList = mutableListOf()
        Thread {
            // 排序
            val packs: List<ApplicationInfo> =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pm.getInstalledApplications(
                        PackageManager.ApplicationInfoFlags.of(
                            PackageManager.GET_META_DATA.toLong()
                        )
                    )
                } else {
                    pm.getInstalledApplications(0)
                }.sortedBy {
                    it.loadLabel(pm).toString()
                }
            //得到系统 安装的所有程序包的PackageInfo对象
            Logger.d("appList\nlist------>${packs.size}")
            packs.map { pi ->
                val map = hashMapOf<String, Any>()
                map["icon"] = pi.loadIcon(pm)
                //图标
                map["appName"] = pi.loadLabel(pm)
                //应用名
                map["packageName"] = pi.packageName
                map["packageInfo"] = pi
                //包名
                if (pi.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                    appList.add(map)//如果非系统应用，则添加至appList
                }/* else if (!onlyUserApp) {
                    appList.add(map)//如果系统应用，当 onlyUserApp==false 时添加至appList

                }*/
                pi
                //循环读取存到HashMap,再增加到ArrayList.一个HashMap就是一项
            }
            runOnUiThread {
                initView(packs)
            }
        }.start()
    }
}