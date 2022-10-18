package com.will.kgu_math.fragments.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

class RegisterViewModel : ViewModel() {
    var firstName = ""
        set(value) {
            field = value
            checkCompleted()
        }

    var secondName = ""
        set(value) {
            field = value
            checkCompleted()
        }

    var fileSaved = false
        set(value) {
            field = value
            checkCompleted()
        }

    var filePath = ""
    var fileName = ""


    private val _btnEnabledFlow = MutableStateFlow(false)
    val btnEnabledStateFlow = _btnEnabledFlow.asStateFlow()


    suspend fun saveVariantsFile(dir: String, name: String, inStream: InputStream) = withContext(Dispatchers.IO) {
        val workDir = File(dir)

        if (!workDir.exists())
            workDir.mkdir()

        val file = File(workDir, name)

        if (file.exists())
            file.delete()

        file.createNewFile()

        file.outputStream().use { out ->
            inStream.use {
                it.copyTo(out)
            }
        }

        filePath = file.path
        fileName = file.path.substringAfterLast('\\')
        fileSaved = true

        return@withContext true
    }

    private fun checkCompleted() {
        _btnEnabledFlow.value = firstName.isNotEmpty() && secondName.isNotEmpty() && fileSaved
    }
}