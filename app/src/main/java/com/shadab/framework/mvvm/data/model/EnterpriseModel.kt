package com.shadab.framework.mvvm.data.model

import com.google.gson.annotations.SerializedName

data class EnterpriseModel(
    @SerializedName("s.no")
    val sNo: Int = 0,
    @SerializedName("amt.pledged")
    val amtPledged: Int = 0,
    @SerializedName("blurb")
    val blurb: String = "",
    @SerializedName("by")
    val by: String = "",
    @SerializedName("country")
    val country: String = "",
    @SerializedName("currency")
    val currency: String = "",
    @SerializedName("end.time")
    val endTime: String = "",
    @SerializedName("location")
    val location: String = "",
    @SerializedName("percentage.funded")
    val percentageFunded: Int = 0,
    @SerializedName("num.backers")
    val numBackers: String = "",
    @SerializedName("state")
    val state: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("url")
    val url: String = ""

)