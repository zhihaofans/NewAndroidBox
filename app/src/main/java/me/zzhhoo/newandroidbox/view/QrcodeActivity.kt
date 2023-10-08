package me.zzhhoo.newandroidbox.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.orhanobut.logger.Logger
import io.zhihao.library.android.kotlinEx.isNotNullAndEmpty
import io.zhihao.library.android.util.ShareUtil
import me.zzhhoo.newandroidbox.util.QrcodeUtil
import me.zzhhoo.newandroidbox.view.ui.theme.NewAndroidBoxTheme

class QrcodeActivity : ComponentActivity() {
    private val shareUtil = ShareUtil(this@QrcodeActivity)
    private val scanOptions = ScanOptions().setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        .setPrompt("竖着也能扫描二维码")
        .setBeepEnabled(false)
        .setBarcodeImageEnabled(true)
        .setOrientationLocked(true)

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            showToast("扫码取消")
        } else {
            setContent {
                initView(result.contents)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO:分享到本应用生成二维码
        when {
            intent?.action == Intent.ACTION_SEND && "text/plain" == intent.type -> {
                intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                    setContent {
                        initView(it ?: "")
                        showToast("分享了内容")
                    }
                }
            }
            else -> {
                setContent {
                    initView()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    private fun initView(defaultText: String? = "Hello World") {

        val text = remember { mutableStateOf(defaultText ?: "") }
        val qrcodeBitmap = remember {
            mutableStateOf(QrcodeUtil().createQRCode(text.value)!!.asImageBitmap())
        }
        NewAndroidBoxTheme {
            Scaffold(
                Modifier.layoutId("appBar"),
                topBar = {
                    TopAppBar(
                        modifier = Modifier
                            .padding(bottom = 0.dp) // 这里为 TopAppBar 添加上边距
                            .fillMaxWidth(),
                        title = {
                            Text(text = "二维码")
                        },
                        actions = {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        },
                    )
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
                            if (it.isNotNullAndEmpty()) {
                                qrcodeBitmap.value =
                                    QrcodeUtil().createQRCode(text.value)!!.asImageBitmap()
                            }
                        },
                        label = { Text("二维码内容") }
                    )
                    Image(
                        bitmap = qrcodeBitmap.value,
                        contentDescription = "这里应该有个二维码",
                    )
                    getButton(id = "button_sharetext", title = "分享文本") {
                        if (text.value.isNotNullAndEmpty()) {
//                        shareText(text.value)
                            shareUtil.shareText(text.value)
                        }
                    }
                    getButton(id = "button_scan", title = "扫码") {
                        barcodeLauncher.launch(scanOptions)
                    }

                }
            }
        }
    }

    private fun startToScan() {
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
        Toast.makeText(this@QrcodeActivity, text, Toast.LENGTH_SHORT).show()
    }
}
