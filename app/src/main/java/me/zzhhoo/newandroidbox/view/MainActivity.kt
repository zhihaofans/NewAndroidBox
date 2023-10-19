package me.zzhhoo.newandroidbox.view

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.zhihao.library.android.util.AlertUtil
import io.zhihao.library.android.util.ClipboardUtil
import io.zhihao.library.android.util.ShareUtil
import io.zhihao.library.android.util.ToastUtil
import me.zzhhoo.newandroidbox.R
import me.zzhhoo.newandroidbox.util.ViewUtil
import me.zzhhoo.newandroidbox.view.ui.theme.NewAndroidBoxTheme

class MainActivity : ComponentActivity() {
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
    @Preview(showBackground = true)
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
                                Text(text = getString(R.string.app_name))
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
                    initContentView()
                }
            }
        }
    }

    @Composable
    private fun initContentView() {
        val listItem = listOf(
            "Android SDK",
            getString(R.string.title_activity_app),
            getString(R.string.title_activity_qrcode),
            getString(R.string.title_activity_favorites),
            getString(R.string.title_activity_calculatetool)
        )
        viewUtil.getListView(listItem) { idx, text ->
            when (idx) {
                0 -> showAndroidSDK()
                1 -> startActivity(Intent(this, AppActivity::class.java))
                2 -> startActivity(Intent(this, QrcodeActivity::class.java))
                3 -> startActivity(Intent(this, FavoritesActivity::class.java))
                4 -> startActivity(Intent(this, CalculatetoolActivity::class.java))
            }
        }
    }

    private fun showAndroidSDK() {
        val sdks = arrayOf(
            "Android 1.0 (API 1)",
            "Android 1.1 (API 2, Petit Four 花式小蛋糕)",
            "Android 1.5 (API 3, Cupcake 纸杯蛋糕)",
            "Android 1.6 (API 4, Donut 甜甜圈)",
            "Android 2.0 (API 5, Eclair 松饼)",
            "Android 2.0.1 (API 6, Eclair 松饼)",
            "Android 2.1 (API 7, Eclair 松饼)",
            "Android 2.2.x (API 8, Froyo 冻酸奶)",
            "Android 2.3-2.3.2 (API 9, Gingerbread 姜饼)",
            "Android 2.3.3-2.3.7 (API 10, Gingerbread 姜饼)",
            "Android 3.0 (API 11, Honeycomb 蜂巢)",
            "Android 3.1 (API 12, Honeycomb 蜂巢)",
            "Android 3.2.x (API 13, Honeycomb 蜂巢)",
            "Android 4.0-4.0.2 (API 14, Ice Cream Sandwich 冰激凌三明治)",
            "Android 4.0.3-4.0.4 (API 15, Ice Cream Sandwich 冰激凌三明治)",
            "Android 4.1.x (API 16, Jelly Bean  果冻豆)",
            "Android 4.2.x (API 17, Jelly Bean  果冻豆)",
            "Android 4.3.x (API 18, Jelly Bean  果冻豆)",
            "Android 4.4.x (API 19, KitKat 奇巧巧克力棒)",
            "Android 4.4w.x (API 20, KitKat 奇巧巧克力棒)",
            "Android 5.0.x (API 21, Lollipop 棒棒糖)",
            "Android 5.1.x (API 22, Lollipop 棒棒糖)",
            "Android 6.0.x (API 23, Marshmallow 棉花糖)",
            "Android 7.0 (API 24, Nougat 牛轧糖)",
            "Android 7.1.x (API 25, Nougat 牛轧糖)",
            "Android 8.0 (API 26, Oreo 奥利奥)",
            "Android 8.1 (API 27, Oreo 奥利奥)",
            "Android 9（API 28, Pie 派)",
            "Android 10（API 29, Quince Tart(Q))",
            "Android 11（API 30, Red Velvet Cake(R))",
            "Android 12（API 31, Snow Cone)",
            "Android 12L（API 32, Snow Cone，大屏幕)",
            "Android 13（API 33, Tiramisu)",
            "Android 14（API 34, 待更新)"
        )
        val nowSdk = Build.VERSION.SDK_INT
        val alertTitle = "你是" + if (nowSdk <= sdks.size) sdks[nowSdk - 1] else "UNKNOWN"
        alertUtil.showListAlert(alertTitle, sdks) { _, index ->
            val title = sdks[index]
            alertUtil.showListAlert(title, arrayOf("复制", "分享")) { _, idx ->
                when (idx) {
                    0 -> ClipboardUtil.copy(title)
                    1 -> shareUtil.shareText(title)
                }
            }
        }
    }

}
