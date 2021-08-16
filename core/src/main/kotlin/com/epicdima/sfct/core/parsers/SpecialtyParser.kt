package com.epicdima.sfct.core.parsers

import com.epicdima.sfct.core.model.Institution
import com.epicdima.sfct.core.model.PassingScore
import com.epicdima.sfct.core.model.Specialty
import com.epicdima.sfct.core.utils.getGroupInt
import com.epicdima.sfct.core.utils.getGroupString
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

/**
 * @author EpicDima
 */
internal class SpecialtyParser : HtmlParser<Institution> {

    companion object {
        private val SCORE_KEY_REGEX = Regex("(score\\s\\^\\s)(\\d+)")
        private val SCORE_VALUE_REGEX = Regex("\\('(.+)'\\)")
    }

    override fun parse(html: String): Institution {
        val content = Jsoup.parse(html).selectFirst("div.content_wrap > .content")
        val heading = content.selectFirst(".heading") ?: return Institution()
        val institutionElement = heading.selectFirst(".breadcrumps").child(3)
        val contextText = content.selectFirst("div.content_text")
        val pairs = createPairs(contextText)
        println()
        return Institution(
            id = institutionElement.attr("href").substring(14).toInt(),
            name = institutionElement.text(),
            address = pairs["Адрес"] ?: "",
            phone = pairs["Телефон"] ?: "",
            specialties = listOf(createSpecialty(content, contextText, pairs))
        )
    }

    private fun createPairs(contextText: Element) = contextText
        .select("p")
        .filter { it.className().isEmpty() && it.childNodeSize() == 2 }
        .map {
            Pair(
                it.child(0).text().trim().dropLast(1),
                (it.childNode(1) as TextNode).text().trim()
            )
        }
        .toMap()

    private fun createSpecialty(
        content: Element,
        contextText: Element,
        pairs: Map<String, String>
    ): Specialty {
        val table = contextText.selectFirst("table.exams > tbody")
        return Specialty(
            faculty = pairs["Факультет"] ?: "",
            name = pairs["Специальность"] ?: "",
            qualification = pairs["Квалификация"] ?: "",
            periodOfStudy = pairs["Срок обучения"] ?: "",
            teachForm = pairs["Форма обучения"] ?: "",
            paymentForm = pairs["Бюджет/Платно"] ?: "",
            receptionPlan = pairs["План приёма"] ?: "",
            notes = pairs["Примечания"] ?: "",
            exams = getExams(table),
            profileExams = getProfileExams(table),
            scores = getScoreList(content)
        )
    }

    private fun getExams(table: Element) = table
        .child(0)
        .child(1)
        .text()
        .trim()

    private fun getProfileExams(table: Element): Pair<String, String> {
        val list = table
            .selectFirst("td.prof-exams")
            .text().split(";")
            .filter(String::isNotBlank)
            .map { line -> line.takeLastWhile { it != '-' }.trim() }
        return if (list.size == 2) {
            Pair(list[0], list[1])
        } else {
            Pair("", "")
        }
    }

    private fun getScoreList(content: Element): List<PassingScore> {
        val key = content.html().getGroupInt(SCORE_KEY_REGEX, 2)
        return content
            .select("div.accordion")
            .filter { it.selectFirst("h3") != null }
            .map { accordion ->
                val year = accordion.selectFirst("h3").text().drop(16).dropLast(1).toInt()
                val trs = accordion.select("div > table > tbody > tr")
                PassingScore(
                    year,
                    getScore(trs[1], key),
                    getScore(trs[2], key),
                    getScore(trs[4], key),
                    getScore(trs[5], key)
                )
            }
            .filter { it.fullTimeBudget != -1 || it.fullTimePaid != -1 || it.partTimeBudget != -1 || it.partTimePaid != -1 }
    }

    private fun getScore(element: Element, key: Int): Int {
        var child = element.child(1)
        if (child.childrenSize() != 0) {
            child = child.child(0)
        }
        val innerText = child.html().replace("&nbsp;", "").trim()
        val rawNumber = innerText.takeWhile { it.isDigit() }
        return if (rawNumber.isEmpty()) {
            val strNumber = innerText.getGroupString(SCORE_VALUE_REGEX, 1).filter { it.isDigit() }
            val result = if (strNumber.isEmpty()) -1 else strNumber.toInt()
            if (result != -1) result xor key else -1
        } else {
            rawNumber.toInt()
        }
    }
}