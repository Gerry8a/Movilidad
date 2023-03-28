package com.bancomer.bbva.bbvamovilidad.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.ui.MainActivity
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseActivity
import com.bancomer.bbva.bbvamovilidad.ui.home.CatalogViewModel
import com.bancomer.bbva.bbvamovilidad.ui.onboarding.OnboardingActivity
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.ONBOARDING_FINISHED
import com.bbva.login.EnvironmentType
import com.bbva.login.OAuthManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private val viewModel: CatalogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val screenSplash = installSplashScreen()
        setContentView(R.layout.activity_spalsh)
        val onboardingFinished = preferences.get(ONBOARDING_FINISHED, false) as Boolean

        Thread.sleep(2000)
        if (!onboardingFinished) {
            val intent = Intent(this@SplashActivity, OnboardingActivity::class.java)
            startActivity(intent)
            screenSplash.setKeepOnScreenCondition { false }
            finish()
        } else {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            screenSplash.setKeepOnScreenCondition { false }
            finish()
        }


    }
}