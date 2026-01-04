package com.example.boardtogo

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class UnionDeparturesResponse(
    @SerialName("Metadata"  ) val metadataInfo  : MetadataInfo?  = MetadataInfo(),
    @SerialName("AllDepartures" ) val allDepartures : AllDepartures? = AllDepartures()
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class MetadataInfo (
    @SerialName("TimeStamp"    ) val timeStamp    : String = "",
    @SerialName("ErrorCode"    ) val errorCode    : String = "",
    @SerialName("ErrorMessage" ) val errorMessage : String = ""

)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Stops (
    @SerialName("Name" ) val name : String? = null,
    @SerialName("Code" ) val code : String? = null

)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Trip (
    @SerialName("Info"        ) val info        : String?          = null,
    @SerialName("TripNumber"  ) val tripNumber  : String?          = null,
    @SerialName("Platform"    ) val platform    : String?          = null,
    @SerialName("Service"     ) val service     : String?          = null,
    @SerialName("ServiceType" ) val serviceType : String?          = null,
    @SerialName("Time"        ) val time        : String?          = null,
    @SerialName("Stops"       ) val stops       : List<Stops> = listOf()

)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AllDepartures (
    @SerialName("Trip" ) val trip : List<Trip> = listOf()

)
