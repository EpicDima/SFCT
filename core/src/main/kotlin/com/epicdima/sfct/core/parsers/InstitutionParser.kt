package com.epicdima.sfct.core.parsers

import com.epicdima.sfct.core.model.Institution
import com.epicdima.sfct.core.model.Specialty
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

/**
 * @author EpicDima
 */
internal class InstitutionParser : HtmlParser<Institution> {

    override fun parse(html: String): Institution {
        val content = Jsoup.parse(html).selectFirst("div.content_wrap > .content")
        val heading = content?.selectFirst(".heading") ?: return Institution()
        val pairs = createPairs(content)
        var site = pairs["Сайт"] ?: ""
        if (site.isNotEmpty()) {
            if (!site.startsWith("http://") && !site.startsWith("https://")) {
                site = "http://$site"
            }
        }
        return Institution(
            name = heading.selectFirst("h1")?.text() ?: "",
            address = pairs["Адрес"] ?: "",
            phone = pairs["Телефон"] ?: "",
            email = pairs["E-mail"] ?: "",
            site = site,
            specialties = getSpecialties(content)
        )
    }

    private fun createPairs(content: Element): Map<String, String> = content
        .selectFirst("div.content_text > .univer_desc")
        ?.select("p")
        ?.map { it.text().split(":") }
        ?.filter { it.size == 2 }
        ?.associate { line ->
            Pair(
                line.first().trim(),
                line.last().trim()
            )
        } ?: emptyMap()

    private fun getSpecialties(content: Element): List<Specialty> =
        content.selectFirst("table.special_table > tbody")
            ?.select("tr > td")
            ?.filterIndexed { index, _ -> (index % 4 == 1) }
            ?.map {
                Specialty(
                    name = (it.childNode(0) as TextNode).text().trim(),
                    id = it.child(0).attr("href").drop(12).toInt()
                )
            }
            ?.distinctBy { it.id } ?: emptyList()
}