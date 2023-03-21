package com.bancomer.bbva.bbvamovilidad.ui.base

import android.widget.Toast
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
}