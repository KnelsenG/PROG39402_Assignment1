package ca.sheridancollege.prog39402_assignment1.ui.mapFragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import ca.sheridancollege.prog39402_assignment1.model.MapMarker
import ca.sheridancollege.prog39402_assignment1.util.SingleLiveEvent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt


class MapFragmentViewModel @Inject constructor() : ViewModel() {

    val showPane = ObservableBoolean(false)
    val campusName = ObservableField<String>()
    val campusDescription = ObservableField<String>()
    val campusPopulation = ObservableField<String>()

    val requestPermissionEvent = SingleLiveEvent<Boolean>()
    val myLocationClickEvent = SingleLiveEvent<Boolean>()

    private val markers = listOf(
            MapMarker("Davis", "The Engineering Campus", 13100, 43.656366, -79.738941),
            MapMarker("HMC", "The Business Campus", 5300, 43.591131, -79.647091),
            MapMarker("Trafalgar", "The Arts Campus", 8300, 43.469012, -79.698705)
    )

    private var map: GoogleMap? = null

    val onMapReadyCallback: OnMapReadyCallback by lazy {
        OnMapReadyCallback {
            initMap(it)
            updateMap()
        }
    }

    private val onMarkerClickCallback by lazy {
        GoogleMap.OnMarkerClickListener {
            it?.let { marker ->

                if (marker.tag != null) {

                    val mm = markers[marker.tag as Int]
                    showPane.set(true)
                    campusName.set(mm.title)
                    campusDescription.set(mm.description)
                    campusPopulation.set(mm.population.toString(10))

                    map?.let { it.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 14.0F)) }
                }
                true
            } ?: false
        }
    }

    private val onMapClickListener by lazy {
        GoogleMap.OnMapClickListener {
            showPane.set(false)
        }
    }

    private val onCameraMoveListener by lazy {
        GoogleMap.OnCameraMoveListener {
            showPane.set(false)
        }
    }

    private val onMyLocationButtonClickListener by lazy {
        GoogleMap.OnMyLocationButtonClickListener {
            myLocationClickEvent.value = true
            false
        }
    }

    private fun initMap(map: GoogleMap) {
        this.map = map
        this.map?.let {
            it.setOnMarkerClickListener(onMarkerClickCallback)
            it.setOnMapClickListener(onMapClickListener)
            it.setOnCameraMoveListener(onCameraMoveListener)
            it.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener)
        }
        requestPermissionEvent.call()
    }

    private fun updateMap() {
        map?.let { map ->

            map.clear()

            markers.forEachIndexed { i, mm ->
                map.addMarker(MarkerOptions().title(mm.title).position(LatLng(mm.lat, mm.long))).tag = i
            }

            map.addPolyline(
                    PolylineOptions()
                            .add(LatLng(markers[0].lat, markers[0].long))
                            .add(LatLng(markers[1].lat, markers[1].long))
                            .add(LatLng(markers[2].lat, markers[2].long))
                            .add(LatLng(markers[0].lat, markers[0].long))
                            .color(Color.BLUE)
                            .width(5F)
            )

            val center = LatLng(
                    markers.sumByDouble { it.lat } / markers.size,
                    markers.sumByDouble { it.long } / markers.size
            )

            val maxMarker = markers
                    .asSequence()
                    .maxBy {
                        sqrt(
                                (center.latitude - it.lat).pow(2) + (center.longitude - it.long).pow(2)
                        )
                    }

            val radius = FloatArray(1)

            Location.distanceBetween(
                    center.latitude, center.longitude,
                    maxMarker!!.lat, maxMarker.long,
                    radius
            )

            map.addCircle(
                    CircleOptions()
                            .center(center)
                            .radius(radius[0].toDouble())
                            .strokeColor(Color.BLACK)
                            .fillColor(0x30ff0000)
                            .strokeWidth(2f)
            )

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 10.0F))
        }
    }

    @SuppressLint("MissingPermission")
    fun enableMyLocation() {
        map?.let { it.isMyLocationEnabled = true }
    }

}

