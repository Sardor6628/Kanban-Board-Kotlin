package com.example.tempstructure.utils.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.tempstructure.R
import com.example.tempstructure.app.AppConstant


fun String.openLink(context: Context) {
    val uri = Uri.parse(this)
    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
}

fun String.shareMsj(context: Context) {
    val appName = context.resources.getString(R.string.app_name)
    val sharing = Intent(Intent.ACTION_SEND)
    sharing.type = "text/plain"
    sharing.putExtra(Intent.EXTRA_SUBJECT, appName)
    sharing.putExtra(
        Intent.EXTRA_TEXT,
        """$appName $this via:${AppConstant.APP_LINK}""".trimIndent()
    )
    context.startActivity(Intent.createChooser(sharing, "Share via"))
}

fun String.shareMsjInstagram(context: Context) {
    val appName = context.resources.getString(R.string.app_name)
    try {
        val sharing = Intent(Intent.ACTION_SEND)
        sharing.action = Intent.ACTION_SEND
        sharing.putExtra(Intent.EXTRA_SUBJECT, appName)
        sharing.putExtra(
            Intent.EXTRA_TEXT,
            """$appName $this via:${AppConstant.APP_LINK}""".trimIndent()
        )
        sharing.type = "text/plain"
        sharing.setPackage("com.instagram.android");
        context.startActivity(sharing)
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "App is not installed", Toast.LENGTH_LONG).show()
    }
}

fun String.shareMsjWhatsapp(context: Context) {
    val appName = context.resources.getString(R.string.app_name)
    try {
        val sharing = Intent(Intent.ACTION_SEND)
        sharing.action = Intent.ACTION_SEND
        sharing.putExtra(Intent.EXTRA_SUBJECT, appName)
        sharing.putExtra(
            Intent.EXTRA_TEXT,
            """$appName $this via:${AppConstant.APP_LINK}""".trimIndent()
        )
        sharing.type = "text/plain"
        sharing.setPackage("com.whatsapp");
        context.startActivity(sharing)
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "App is not installed", Toast.LENGTH_LONG).show()
    }
}

fun String.shareMsjWhatsappNumber(context: Context, number: String) {
    val appName = context.resources.getString(R.string.app_name)
    try {
        val msj = """$appName \n\n $this via:${AppConstant.APP_LINK}""".trimIndent()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("http://api.whatsapp.com/send?phone=$number&text=$msj")
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun String.shareMsjEmail(context: Context, addresses: String) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, addresses)
    intent.putExtra(Intent.EXTRA_SUBJECT, this)
    context.startActivity(intent)
}
