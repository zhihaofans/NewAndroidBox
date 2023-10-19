package me.zzhhoo.newandroidbox.view

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.orhanobut.logger.Logger
import io.zhihao.library.android.kotlinEx.getAppIcon
import io.zhihao.library.android.kotlinEx.getAppName
import io.zhihao.library.android.kotlinEx.isEmpty
import io.zhihao.library.android.util.AlertUtil
import io.zhihao.library.android.util.FileUtil
import io.zhihao.library.android.util.ShareUtil
import io.zhihao.library.android.util.ToastUtil
import me.zzhhoo.newandroidbox.R
import me.zzhhoo.newandroidbox.util.ImageUtil
import me.zzhhoo.newandroidbox.util.ViewUtil
import me.zzhhoo.newandroidbox.view.ui.theme.NewAndroidBoxTheme


class AppActivity : ComponentActivity() {
    private val alertUtil = AlertUtil(this)
    private val shareUtil = ShareUtil(this)
    private val toastUtil = ToastUtil(this)
    private val viewUtil = ViewUtil(this)
    private var appList = mutableListOf<Map<String, Any>>()
    private val onlyUserApp = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewAndroidBoxTheme {
                MaterialTheme {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Loading",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
        init()
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
                        viewUtil.getListView(packs.sortedBy { it.getAppName() }.map {
                            it.getAppName()
                        }) { idx, name ->
                            val app = packs.get(idx)
                            toastUtil.showShortToast(app.loadLabel(packageManager).toString())
                            app.loadIcon(packageManager)
                            alertUtil.showListAlert(
                                app.getAppName(),
                                arrayOf("保存图标")
                            ) { dialog, index ->
                                when (index) {
                                    0 -> getAppIcon(app)
                                }
                            }
                        }
                    }
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

                if (pi.flags and (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP or ApplicationInfo.FLAG_SYSTEM) > 0) {
                    if (!onlyUserApp) {
                        appList.add(map)//如果系统应用，当 onlyUserApp==false 时添加至appList
                    }
                } else {
                    appList.add(map)//如果非系统应用，则添加至appList
                }
                pi
                //循环读取存到HashMap,再增加到ArrayList.一个HashMap就是一项
            }
            runOnUiThread {
                initView(packs)
            }
        }.start()
    }

    private fun getAppIcon(appInfo: ApplicationInfo) {
        val appName = appInfo.getAppName()
        val appPackageName = appInfo.packageName
        val appIcon = appInfo.getAppIcon()?.toBitmap()
        val saveTo = FileUtil.getDownloadPathString() + "NewAndroidBox/"
        val savePath = "$saveTo$appPackageName-icon.png"
        val success = if (appIcon.isEmpty()) {
            false
        } else {
            FileUtil.saveImagePng(appIcon!!, savePath)
        }

        val title = "保存：" + if (success) {
            "成功"
        } else {
            "失败"
        }

        alertUtil.showInputAlert(title, savePath) { text, dialog ->

        }
    }
}