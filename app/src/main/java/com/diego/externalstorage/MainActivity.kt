package com.diego.externalstorage

import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    lateinit var inputText: EditText
    lateinit var response: TextView
    lateinit var saveButton: Button
    lateinit var readButton:Button
    private val filename = "SampleFile.txt"
    private val filepath = "MyFileStorage"
    var myExternalFile: File? = null
    var myData = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputText = findViewById(R.id.myInputText)
        response =  findViewById(R.id.response)
        saveButton = findViewById(R.id.saveExternalStorage);
        saveButton.setOnClickListener {
            try {
                val fos = FileOutputStream(myExternalFile)
                fos.write(inputText.text.toString().toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            inputText.text.clear()
            response.text = "SampleFile.txt guardado en almacenamiento externo..."
        }
        readButton =  findViewById(R.id.getExternalStorage)
        readButton.setOnClickListener {
            try {
                val fis = FileInputStream(myExternalFile)
                val inStream = DataInputStream(fis)
                val br = BufferedReader(InputStreamReader(inStream))
                val stringBuilder = StringBuilder()
                var strLine: String?
                while (br.readLine().also { strLine = it } != null) {
                    stringBuilder.append(strLine)
                }
                inStream.close()
                inputText.setText(stringBuilder.toString())
                response.text = "SampleFile.txt data retrieved from Internal Storage..."
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            saveButton.isEnabled = false
        } else {
            myExternalFile = File(getExternalFilesDir(filepath), filename)
        }
    }
    private fun isExternalStorageReadOnly(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState) {
            true
        } else false
    }

    private fun isExternalStorageAvailable(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED == extStorageState) {
            true
        } else false
    }




}