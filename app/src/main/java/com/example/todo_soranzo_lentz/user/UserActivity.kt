package com.example.todo_soranzo_lentz.user

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.example.todo_soranzo_lentz.data.Api
import com.example.todo_soranzo_lentz.detail.ui.theme.ToDoSoranzoLentzTheme
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoSoranzoLentzTheme {
                var bitmap: Bitmap? by remember { mutableStateOf(null) }
                var uri: Uri? by remember { mutableStateOf(null) }

                val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
                    bitmap = it
                    lifecycleScope.launch {
                        bitmap?.toRequestBody() }
                    }

                val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                    uri = it
                    lifecycleScope.launch {
                        uri?.toRequestBody()
                    }
                }

                val pickPhotoPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                    pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                Column {
                    AsyncImage(
                        modifier = Modifier.fillMaxHeight(.2f),
                        model = bitmap ?: uri,
                        contentDescription = null
                    )
                    Button(
                        onClick = {pickPhotoPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)},
                        content = { Text("Take picture") }
                    )
                    Button(
                        onClick = {takePicture.launch()},
                        content = { Text("Pick photo") }
                    )
                }
            }
        }
    }
    private fun Bitmap.toRequestBody(): MultipartBody.Part {
        val tmpFile = File.createTempFile("avatar", "jpg")
        tmpFile.outputStream().use { // *use* se charge de faire open et close
            this.compress(Bitmap.CompressFormat.JPEG, 100, it) // *this* est le bitmap ici
        }
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = tmpFile.readBytes().toRequestBody()
        )
    }

    private fun Uri.toRequestBody(): MultipartBody.Part {
        val fileInputStream = contentResolver.openInputStream(this)!!
        val fileBody = fileInputStream.readBytes().toRequestBody()
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = fileBody
        )
    }

    private suspend fun avatar(bitmap: MultipartBody.Part) {
        Api.userWebService.updateAvatar(bitmap)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ToDoSoranzoLentzTheme() {
        Greeting("Android")
    }
}