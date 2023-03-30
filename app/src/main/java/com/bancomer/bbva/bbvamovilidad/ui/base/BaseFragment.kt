package com.bancomer.bbva.bbvamovilidad.ui.base

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bancomer.bbva.bbvamovilidad.utils.Preferences
import javax.inject.Inject

abstract class BaseFragment: Fragment() {

    @Inject
    protected lateinit var preferences: Preferences

    fun longToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun shortToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                shortToast("Permisos aceptados")
            //setUpMap()
            } else {
                /**
                 * Se explica porqué se tiene que aceptar los permisos
                 */


                Log.d("ggg", "Acepta, prro")
                //  showShortToast(getString(R.string.alert_permission_needed))
            }
        }

    fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                //setUpMap()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                /**
                 * Descripción de porqué debe aceptar el permiso
                 */
                Log.d("ggg", "requestLocationPermission: da permiso, perro")
                showAlertdialog("Atención", "Es neesario aceptar los permisos para usar la aplicación")
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    fun showAlertdialog(title: String, message: String){
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar"){_, _ ->
                requestPermissionLauncher
            }
            .setNegativeButton("Cancelar"){_, _ ->
                shortToast("Es necesario aceptar los permisos para usar la aplicación")
            }
    }
}