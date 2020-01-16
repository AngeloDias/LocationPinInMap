package self.training.locationpininmap

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import self.training.locationpininmap.utils.PinLocation

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var prefs: SharedPreferences
    private lateinit var locationsFromFile: List<PinLocation>
    private lateinit var mapMarkers: MutableMap<String, Marker>
    private lateinit var mapToolbar: Toolbar
    private lateinit var checkedArray: BooleanArray

    private val _debugTag = "debugLocations"
    private val _permissionID = 700
    private val _prefsName = "self.training.locationpininmap"
    private val _firstRunKey = "firstRun"
    private val _saveCheckArrayInstance = "checkArrayInstanceTag"

    companion object {
        const val fragmentPinsDialogTag = "fragment_pin_categories"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        prefs = getSharedPreferences(_prefsName, Context.MODE_PRIVATE)
        mapMarkers = HashMap()
        checkedArray = booleanArrayOf(true, true, true, true, true)

        mapFragment.getMapAsync(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mapToolbar = findViewById(R.id.map_toolbar)
            mapToolbar.elevation = 4.0f
            mapToolbar.title = ""

            setSupportActionBar(mapToolbar)
        }

    }

    fun showPinInSelectedCategories(selectedItems: BooleanArray) {
        selectedItems.copyInto(checkedArray)

        for (i in checkedArray.indices) {

            if (!checkedArray[i]) {
                locationsFromFile.map {
                    if (it.categoria == i + 1) {
                        val tempMarker = mapMarkers[it.nome] as Marker
                        tempMarker.remove()
                    }
                }

            } else {

                locationsFromFile.map {
                    if (it.categoria == i + 1) {
                        val tempMarker = mapMarkers[it.nome] as Marker
                        val latLng =
                            LatLng(tempMarker.position.latitude, tempMarker.position.longitude)
                        tempMarker.remove()

                        it.nome.let { itNome ->
                            val marker =
                                mMap.addMarker(MarkerOptions().position(latLng).title(itNome))

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                mapMarkers.replace(itNome, marker)
                            } else {
                                mapMarkers.remove(itNome)
                                mapMarkers[itNome] = marker
                            }
                        }
                    }
                }

            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_filter -> {
            PinCategoriesDialogFragment(checkedArray).show(
                supportFragmentManager,
                fragmentPinsDialogTag
            )
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getLastLocation()
        getLocationsFromFile()
    }

    private fun getLocationsFromFile() {

        try {
            val input = assets.open("pontosref.json")
            val inputAsString = input.bufferedReader().use { it.readText() }

            this.locationsFromFile =
                Gson().fromJson(inputAsString, Array<PinLocation>::class.java).toList()
            this.locationsFromFile.forEach {
                pinInMap(LatLng(it.latitude, it.longitude), it.nome, 12.0f)
            }

            input.close()

        } catch (e: NoSuchFileException) {
            e.printStackTrace()
        }

    }

    private fun pinInMap(latLng: LatLng, pinTitle: String, zoomLevel: Float) {
        val marker = mMap.addMarker(MarkerOptions().position(latLng).title(pinTitle))
        mapMarkers[pinTitle] = marker

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo

        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun onResume() {
        super.onResume()

        if (prefs.getBoolean(_firstRunKey, true)) {
            prefs.edit().putBoolean(_firstRunKey, false).apply()

            if (!isNetworkAvailable()) {
                Toast.makeText(this, "Plase, enable internet", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == _permissionID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                acquireFusedLocation()
            }
        }
    }

    private fun acquireFusedLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    pinInMap(
                        LatLng(location.latitude, location.longitude),
                        "Last know location",
                        11.0f
                    )
                }
            }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            _permissionID
        )
    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                acquireFusedLocation()
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

}
