package es.uc3m.android.traveltales

data class Trip(
    val tripName: String,
    val tripDescription: String,
    val tripStartDate: String,
    val tripEndDate: String,
    val tripImageUri: String
)