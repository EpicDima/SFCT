package com.epicdima.sfct.core.usecases

import com.epicdima.sfct.core.model.Institution
import com.epicdima.sfct.core.network.ApiService
import com.epicdima.sfct.core.network.DataResult
import com.epicdima.sfct.core.parsers.ExsearchParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author EpicDima
 */
class GetExsearchUseCase @Inject constructor(
    private val service: ApiService
) {
    suspend fun execute(params: ExsearchParams): DataResult<List<Institution>> =
        withContext(Dispatchers.IO) {
            try {
                DataResult.Success(
                    ExsearchParser().parse(
                        service.postExsearch(
                            params.exams.map(ExsearchParams.Exam::value).toTypedArray(),
                            params.region.value,
                            params.teachForm.value,
                            params.typeOfInstitution.value,
                            params.paymentForm.value,
                            params.rangeOfPoints.value,
                            params.points,
                            params.dormitory.value
                        )
                    )
                )
            } catch (e: Exception) {
                DataResult.Failure(e)
            }
        }
}

data class ExsearchParams(
    val exams: List<Exam> = emptyList(),
    val region: Region = Region.ALL,
    val teachForm: TeachForm = TeachForm.UNSPECIFIED,
    val typeOfInstitution: TypeOfInstitution = TypeOfInstitution.ALL,
    val paymentForm: PaymentForm = PaymentForm.UNSPECIFIED,
    val rangeOfPoints: RangeOfPoints = RangeOfPoints.UNSPECIFIED,
    val points: String = "",
    val dormitory: Dormitory = Dormitory.UNSPECIFIED
) {

    enum class Exam(val value: String) {
        RUSSIAN_BELARUSIAN_LANGUAGE("русский"),
        HISTORY_OF_BELARUS("беларуси"),
        GLOBAL_HISTORY("всемирная"),
        GEOGRAPHY("география"),
        MATHEMATICS("математика"),
        PHYSICS("физика"),
        SOCIAL_SCIENCE("обществоведение"),
        FOREIGN_LANGUAGE("иностр"),
        CHEMISTRY("химия"),
        BIOLOGY("биология"),
        CREATIVITY("творчество"),
        SPECIALTY("специальность"),
        RUSSIAN_BELARUSIAN_LITERATURE("литература"),
        INTERVIEW("собеседование"),
        COMPLETITION_DOCUMENTS("конкурс"),
        PHYSICAL_TRAINING("физическая")
    }

    enum class Region(val value: String) {
        ALL("all"),
        MINSK("minsk"),
        BREST_REGION("brest"),
        VITEBSK_REGION("vitebsk"),
        GOMEL_REGION("gomel"),
        GRODNO_REGION("grodno"),
        MINSK_REGION("minskaya"),
        MOGILEV_REGION("mogilev")
    }

    enum class TeachForm(val value: String) {
        UNSPECIFIED(""),
        FULLTIME("дн"),
        PARTTIME("заоч")
    }

    enum class TypeOfInstitution(val value: String) {
        ALL("all"),
        UNIVERSITY("vuz"),
        COLLEGE("suz")
    }

    enum class PaymentForm(val value: String) {
        UNSPECIFIED(""),
        BUDGET("б"),
        PAID("пл")
    }

    enum class RangeOfPoints(val value: String) {
        UNSPECIFIED(""),
        NO_COMPLETITION("--"),
        UP_TO_NINE("до 90"),
        BETWEEN_91_AND_100("91 - 100"),
        BETWEEN_101_AND_125("101 - 125"),
        BETWEEN_126_AND_150("126 - 150"),
        BETWEEN_151_AND_175("151 - 175"),
        BETWEEN_176_AND_200("176 - 200"),
        BETWEEN_201_AND_225("201 - 225"),
        BETWEEN_226_AND_250("226 - 250"),
        BETWEEN_251_AND_275("251 - 275"),
        BETWEEN_276_AND_300("276 - 300"),
        BETWEEN_301_AND_325("301 - 325"),
        BETWEEN_326_AND_350("326 - 350"),
        BETWEEN_351_AND_375("351 - 375"),
        BETWEEN_376_AND_400("376 - 400")
    }

    enum class Dormitory(val value: String) {
        UNSPECIFIED(""),
        YES("1"),
        NOT_ALL("2"),
        NO("3")
    }
}