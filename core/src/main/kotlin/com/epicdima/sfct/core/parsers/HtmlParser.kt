package com.epicdima.sfct.core.parsers

/**
 * @author EpicDima
 */
internal interface HtmlParser<T> {

    fun parse(html: String): T
}