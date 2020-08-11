import com.epicdima.sfct.core.network.ApiService
import retrofit2.http.*

/**
 * @author EpicDima
 */
interface RetrofitApiService : ApiService {
    companion object {
        const val BASE_URL = "https://kudapostupat.by"
    }

    @FormUrlEncoded
    @POST("/exsearch")
    override suspend fun postExsearch(
        @Field("exs[]") exams: Array<String>,
        @Field("oblast") region: String,
        @Field("teach_form") teachForm: String,
        @Field("type") typeOfInstitution: String,
        @Field("payment_form") paymentForm: String,
        @Field("ball_between") rangeOfPoints: String,
        @Field("ball_own") points: String,
        @Field("hostel") dormitory: String
    ): String

    @GET("/zavdata/id/{id}")
    override suspend fun getSpecialtyById(
        @Path("id") id: Int
    ): String

    @GET("/zavedenie/id/{id}")
    override suspend fun getInstitutionById(
        @Path("id") id: Int
    ): String

    @GET("/zavedenie/id/{id}/hostel")
    override suspend fun getDormitoryById(
        @Path("id") id: Int
    ): String
}