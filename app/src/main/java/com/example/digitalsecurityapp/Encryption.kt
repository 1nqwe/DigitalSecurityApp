package com.example.digitalsecurityapp

import android.R.attr.clipToPadding
import android.R.attr.label
import android.R.attr.text
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import java.util.Base64
import kotlin.random.Random




private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class Encryption : Fragment() {
    private lateinit var spinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_encryption, container, false)

    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requireActivity()
        val message: EditText = view.findViewById(R.id.input_enc)
        val button: Button = view.findViewById(R.id.button_enc)
        var enc: String = ""
        var encCodeMessage = ""
        val textMessage: TextView = view.findViewById(R.id.enc_result_text)

        spinner = view?.findViewById(R.id.spinner_encryption)!!;
        val spinnerList: List<String> =
            listOf("To Base64", "From Base64", "URL Encode", "URL Decode")
        val arrayAdapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, spinnerList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem: String = parent?.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "To Base64" -> enc = "To Base64"
                    "From Base64" -> enc = "From Base64"
                    "URL Encode" -> enc = "URL Encode"
                    "URL Decode" -> enc = "URL Decode"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        button.setOnClickListener {
            encCodeMessage = ""
            if (enc == "To Base64") {
                encCodeMessage = encodeToBase64(message.toString())
                textMessage.text = encCodeMessage
            }
            if (enc == "From Base64") {
                encCodeMessage = decodeFromBase64(message.toString())
                textMessage.text = encCodeMessage
            }
        }





    }




    /*
    private fun encodeToBase64(message: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(message.toByteArray(Charsets.UTF_8))
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    private fun decodeFromBase64(encodedMessage: String): String {
        val bytes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           Base64.getDecoder().decode(encodedMessage)
        } else {
            TODO("VERSION.SDK_INT < O")
       }
        return String(bytes, Charsets.UTF_8)
    }
*/


    private fun encodeToBase64(message: String): String {
        val salt = Random.nextBytes(16)
        val saltedMessage = message + salt.toString(Charsets.UTF_8)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(saltedMessage.toByteArray(Charsets.UTF_8))
        } else {
            android.util.Base64.encodeToString(
                saltedMessage.toByteArray(Charsets.UTF_8),
                android.util.Base64.NO_WRAP
            )
        }
    }

    private fun decodeFromBase64(encodedMessage: String): String {
        val cleanedMessage = encodedMessage.replace("[^A-Za-z0-9+/=]".toRegex(), "")

        val saltMessage = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String(Base64.getDecoder().decode(cleanedMessage), Charsets.UTF_8)
            } else {
                String(
                    android.util.Base64.decode(cleanedMessage, android.util.Base64.NO_WRAP),
                    Charsets.UTF_8
                )
            }
        } catch (e: IllegalArgumentException) {
             TODO("")
        }
        return saltMessage.dropLast(16)
    }



    private fun copyText(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Скопировать текст", text)
        clipboardManager.setPrimaryClip(clipData)
    }



}
