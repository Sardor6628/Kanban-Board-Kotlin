package com.example.tempstructure.data.response


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class TimeResponse(
    @SerializedName("abbreviation")
    var abbreviation: String? = null,
    @SerializedName("client_ip")
    var clientIp: String? = null,
    @SerializedName("datetime")
    var datetime: String? = null,
    @SerializedName("day_of_week")
    var dayOfWeek: Int? = null,
    @SerializedName("day_of_year")
    var dayOfYear: Int? = null,
    @SerializedName("dst")
    var dst: Boolean? = null,
    @SerializedName("dst_from")
    var dstFrom: String? = null,
    @SerializedName("dst_offset")
    var dstOffset: Int? = null,
    @SerializedName("dst_until")
    var dstUntil: String? = null,
    @SerializedName("raw_offset")
    var rawOffset: Int? = null,
    @SerializedName("timezone")
    var timezone: String? = null,
    @SerializedName("unixtime")
    var unixtime: Int? = null,
    @SerializedName("utc_datetime")
    var utcDatetime: String? = null,
    @SerializedName("utc_offset")
    var utcOffset: String? = null,
    @SerializedName("week_number")
    var weekNumber: Int? = null
) : Parcelable