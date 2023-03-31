package bg.dabulgaria.tibroish.presentation.ui.registration

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Countries (
        var countries: List<CountryCode>? = null): Serializable

data class CountryCode (
        var name: String? = null,
        var code: String? = null) : Parcelable, Serializable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(code)
    }

    companion object CREATOR : Parcelable.Creator<CountryCode> {
        override fun createFromParcel(parcel: Parcel): CountryCode {
            return CountryCode(parcel)
        }

        override fun newArray(size: Int): Array<CountryCode?> {
            return arrayOfNulls(size)
        }
    }
}