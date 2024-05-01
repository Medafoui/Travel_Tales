package es.uc3m.android.traveltales

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class GeoHelper : Geocoder.GeocodeListener {
    override fun onGeocode(p0: MutableList<Address>) {
        TODO("Not yet implemented")
    }
}