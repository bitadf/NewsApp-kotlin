import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val image: String?,
    val title: String,
    val description: String?,
    val date: String,
    val source: String,
    val author: String?,
    val category: String = "",
    val content : String ?
) : Parcelable