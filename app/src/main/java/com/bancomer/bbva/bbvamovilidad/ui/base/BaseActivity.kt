package com.bancomer.bbva.bbvamovilidad.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.bancomer.bbva.bbvamovilidad.utils.Preferences
import javax.inject.Inject

abstract class BaseActivity: AppCompatActivity() {

    @Inject
    protected lateinit var preferences: Preferences

}