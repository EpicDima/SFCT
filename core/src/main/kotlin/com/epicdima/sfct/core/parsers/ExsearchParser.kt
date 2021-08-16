package com.epicdima.sfct.core.parsers

import com.epicdima.sfct.core.model.Institution
import com.epicdima.sfct.core.model.Specialty
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * @author EpicDima
 */
internal class ExsearchParser : HtmlParser<List<Institution>> {

    override fun parse(html: String) = Jsoup.parse(html)
        ?.selectFirst("div.catalog")
        ?.select("div.catalog_item")
        ?.map(::parseInstitution)
        ?.filter { it.name.isNotEmpty() }
        ?: emptyList()

    private fun parseInstitution(element: Element) = Institution(
        name = element.selectFirst("h3").text(),
        specialties = element.select("table.search_result > tbody > tr")
            .filterIndexed { index, item -> (index % 2 == 1) && (item.childrenSize() == 3) }
            .map(::parseSpecialty)
    )

    private fun parseSpecialty(element: Element): Specialty {
        val children = element.children()
        return Specialty(
            id = children[2]
                .child(0)
                .attr("href")
                .substring(12)
                .toInt(),
            name = children[1].text(),
            faculty = children[0].text()
        )
    }
}