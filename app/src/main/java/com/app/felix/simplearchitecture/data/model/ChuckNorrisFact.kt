package com.app.felix.simplearchitecture.data.model

import com.google.gson.annotations.SerializedName

data class ChuckNorrisFact (
	@SerializedName("icon_url") val iconUrl : String,
	@SerializedName("id") val id : String,
	@SerializedName("url") val url : String,
	@SerializedName("value") val value : String
)