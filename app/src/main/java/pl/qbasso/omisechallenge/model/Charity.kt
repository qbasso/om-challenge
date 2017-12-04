package pl.qbasso.omisechallenge.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Charity : Serializable {

    var id: Int? = null

    var name: String? = null

    @SerializedName("logo_url")
    var logoUrl: String? = null
}