package com.epicdima.sfct.core.parsers

import org.jsoup.Jsoup

/**
 * @author EpicDima
 */
internal class DormitoryParser : HtmlParser<String> {

    override fun parse(html: String): String = Jsoup.parse(html)
        .selectFirst("#service_brief_box")
        .select("p")
        .joinToString("\n") { it.text().trim() }
}