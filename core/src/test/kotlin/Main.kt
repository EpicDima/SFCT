import com.epicdima.sfct.core.parsers.SpecialtyParser
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * @author EpicDima
 */
fun main() = runBlocking {
    val service = createApiService()
    println(SpecialtyParser().parse(service.getSpecialtyById(69)).specialties.first().scores)
}

fun createRetrofit(): Retrofit = Retrofit.Builder()
    .baseUrl(RetrofitApiService.BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

fun createApiService(): RetrofitApiService = createRetrofit().create(RetrofitApiService::class.java)
