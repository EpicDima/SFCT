package com.epicdima.sfct.utils

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epicdima.sfct.R
import java.util.*

/**
 * @author EpicDima
 */
fun <E : Enum<E>> Context.getEnumStrings(enumClass: Class<E>): EnumMap<E, String> {
    val clsName = enumClass.simpleName.lowercase(Locale.ROOT)
    return EnumMap(enumClass.enumConstants!!.associateWith {
        getString(
            resources.getIdentifier(
                clsName + "_" + it.name.lowercase(Locale.ROOT),
                "string",
                packageName
            )
        )
    })
}

fun <E : Enum<E>> Context.getEnumString(enum: E): String {
    val clsName = enum.javaClass.simpleName.lowercase(Locale.ROOT)
    return getString(
        resources.getIdentifier(
            clsName + "_" + enum.name.lowercase(Locale.ROOT),
            "string",
            packageName
        )
    )
}

fun View.show() {
    this.changeVisibility(View.VISIBLE)
}

fun View.hide() {
    this.changeVisibility(View.INVISIBLE)
}

fun View.gone() {
    this.changeVisibility(View.GONE)
}

private fun View.changeVisibility(newVisibility: Int) {
    if (visibility != newVisibility) {
        visibility = newVisibility
    }
}

fun Fragment.requireAppCompatActivity(): AppCompatActivity {
    return requireActivity() as AppCompatActivity
}

fun Fragment.showBackButton() {
    requireAppCompatActivity().supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    requireAppCompatActivity().supportActionBar!!.setDisplayShowHomeEnabled(true)
}

fun Fragment.hideBackButton() {
    requireAppCompatActivity().supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    requireAppCompatActivity().supportActionBar!!.setDisplayShowHomeEnabled(false)
}

fun RecyclerView.addDelimiter(context: Context) {
    addItemDecoration(
        ImpovedDividerItemDecoration(
            ContextCompat.getDrawable(
                context,
                R.drawable.divider
            )!!
        )
    )
}

fun <VH : RecyclerView.ViewHolder> RecyclerView.setStandardProperties(
    context: Context,
    adapter: RecyclerView.Adapter<VH>
) {
    addDelimiter(context)
    itemAnimator = null
    layoutManager = LinearLayoutManager(context)
    this.adapter = adapter
}