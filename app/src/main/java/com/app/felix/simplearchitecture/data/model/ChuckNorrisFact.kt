package com.app.felix.simplearchitecture.data.model

import com.google.gson.annotations.SerializedName

data class ChuckNorrisFact (
	@SerializedName("icon_url") val icon_url : String,
	@SerializedName("id") val id : String,
	@SerializedName("url") val url : String,
	@SerializedName("value") val value : String
)