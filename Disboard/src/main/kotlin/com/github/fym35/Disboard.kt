package com.github.fym35

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.aliucord.Utils.openPageWithProxy
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI
import com.aliucord.fragments.SettingsPage
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import com.aliucord.Constants
import com.aliucord.Utils
import com.aliucord.entities.Plugin
import com.aliucord.patcher.after
import com.discord.utilities.color.ColorCompat
import com.discord.widgets.settings.WidgetSettings
import com.lytefast.flexinput.R


@AliucordPlugin
class Disboard : Plugin() {
    @Suppress("SetTextI18n", "Deprecation")
    override fun start(context: Context) {

        commands.registerCommand("disboard", "Open disboard") {
            openPageWithProxy(
                Utils.appActivity,
                DisboardPage(

                )
            )
            CommandsAPI.CommandResult()
        }

        patcher.after<WidgetSettings>("onViewBound", View::class.java) {
            val context = requireContext()
            val root = it.args[0] as CoordinatorLayout
            val view = (root.getChildAt(1) as NestedScrollView).getChildAt(0) as LinearLayoutCompat
            val baseIndex = view.getChildAt(4) as TextView
            val font = ResourcesCompat.getFont(context, Constants.Fonts.whitney_medium)
            val icon = ContextCompat.getDrawable(context, R.e.ic_event_24dp)
                ?.apply {
                    mutate()
                }
            val bcs = TextView(context, null, 0, R.i.UiKit_Settings_Item_Icon).apply {
                text = "Disboard"
                typeface = font
                setCompoundDrawablesRelativeWithIntrinsicBounds(icon, null, null, null)
            }
            view.addView(bcs, view.indexOfChild(baseIndex))
            bcs.setOnClickListener {
                openPageWithProxy(
                    Utils.appActivity,
                    DisboardPage(
    
                    )
                )                
           }
        }
    }
           

    @SuppressLint("SetJavaScriptEnabled")
    inner class Trolley(context: Context) : WebView(context) {
        @SuppressLint("ClickableViewAccessibility")
        override fun onTouchEvent(event: MotionEvent): Boolean {
            requestDisallowInterceptTouchEvent(true)
            return super.onTouchEvent(event)
        }

        init {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    return false
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                }
            }
        }
    }

    lateinit var disboard: Trolley

    inner class DisboardPage() : SettingsPage() {
        override fun onViewBound(view: View) {
            super.onViewBound(view)
            setPadding(0)
            disboard = Trolley(view.context)
            disboard.loadUrl("https://disboard.org")
            disboard.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            linearLayout.addView(disboard)
        }
    }

    override fun stop(context: Context) {
        patcher.unpatchAll()
        commands.unregisterAll()
    }
}