package ca.sheridancollege.prog39402_assignment1.ui.mapFragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ca.sheridancollege.prog39402_assignment1.R
import ca.sheridancollege.prog39402_assignment1.base.BaseFragment
import ca.sheridancollege.prog39402_assignment1.databinding.FragmentMapBinding
import ca.sheridancollege.prog39402_assignment1.util.PermissionUtils
import com.google.android.gms.maps.SupportMapFragment


class MapFragment : BaseFragment() {

    private lateinit var binding: FragmentMapBinding
    private lateinit var viewModel: MapFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MapFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initViews()
        initObservables()
    }

    private fun init() {
        binding.vm = viewModel
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(viewModel.onMapReadyCallback)
    }

    private fun initViews() {

    }

    private fun initObservables() {
        viewModel.apply {
            this.requestPermissionEvent.observe(this@MapFragment, Observer {

                if (ContextCompat.checkSelfPermission(this@MapFragment.context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    PermissionUtils.requestPermission(
                            this@MapFragment,
                            LOCATION_PERMISSION_REQUEST_CODE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            true)
                } else {
                    viewModel.enableMyLocation()
                }
            })
            this.myLocationClickEvent.observe(this@MapFragment, Observer { _ ->
                this@MapFragment.let {
                    Toast.makeText(it.context, "Going to your location", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            viewModel.enableMyLocation()
        } else {
            // Display the missing permission error dialog when the fragments resume.
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = MapFragment()

        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
