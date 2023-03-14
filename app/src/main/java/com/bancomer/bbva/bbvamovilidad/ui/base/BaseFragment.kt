package com.bancomer.bbva.bbvamovilidad.ui.base

import androidx.fragment.app.Fragment
import com.bancomer.bbva.bbvamovilidad.utils.Preferences
import javax.inject.Inject

abstract class BaseFragment: Fragment() {

    @Inject
    protected lateinit var preferences: Preferences
}