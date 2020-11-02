package com.wardabbass.redit.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

const val DEFAULT_THUMB = "default"

@Parcelize
class ReditPost(
        @SerializedName("title") val title: String = "",
        @SerializedName("subreddit_name_prefixed") val subredditNamePrefixed: String = "",
        @SerializedName("thumbnail_height") val thumbnailHeight: Int = -1,
        @SerializedName("name") val name: String = "",
        @SerializedName("thumbnail_width") val thumbnailWidth: Int = -1,
        @SerializedName("thumbnail") val thumbnail: String = "",
        @SerializedName("author_flair_text") val authorFlairText: String = "",
        @SerializedName("id") val id: String = "",
        @SerializedName("permalink") val permalink: String = "",
        @SerializedName("created_utc") val createdUtc: Int = 0
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReditPost

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}