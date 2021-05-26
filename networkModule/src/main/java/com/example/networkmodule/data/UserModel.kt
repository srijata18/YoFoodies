package com.example.networkmodule.data

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class UserDetails(
    val u_email: String,
    val u_img: String?,
    val u_name: String,
    val u_phone: Long,
    val u_preference_unit: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(u_email)
        parcel.writeString(u_img)
        parcel.writeString(u_name)
        parcel.writeLong(u_phone)
        parcel.writeString(u_preference_unit)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserDetails> {
        override fun createFromParcel(parcel: Parcel): UserDetails {
            return UserDetails(parcel)
        }

        override fun newArray(size: Int): Array<UserDetails?> {
            return arrayOfNulls(size)
        }
    }
}