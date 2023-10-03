package me.zzhhoo.newandroidbox.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.zhihao.library.android.kotlinEx.isNotNullAndEmpty
import io.zhihao.library.android.util.ShareUtil
import me.zzhhoo.newandroidbox.util.QrcodeUtil
import me.zzhhoo.newandroidbox.view.ui.theme.NewAndroidBoxTheme

class QrcodeActivity : ComponentActivity() {
    private val shareUtil = ShareUtil(this@QrcodeActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewAndroidBoxTheme {
                // A surface container using the 'background' color from the theme
                initView()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    private fun initView() {

        val text = remember { mutableStateOf("Hello World") }
        val qrcodeBitmap = remember {
            mutableStateOf(QrcodeUtil().createQRCode(text.value)!!.asImageBitmap())
        }
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

            }
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
}
