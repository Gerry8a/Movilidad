package com.bancomer.bbva.bbvamovilidad.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentOnboardingBinding
import com.bancomer.bbva.bbvamovilidad.ui.home.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var viewPager: ViewPager2
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpview()
        buildObservers()
    }

    private fun buildObservers() {
//        viewModel.getUserInfo()
    }

    private fun setUpview() {
        val fragmentList = arrayListOf<Fragment>(
            FirstOnboardingFragment(),
            SecondOnboardingFragment(),
            FourthOnboardingFragment(),
            ThirdOnboardingFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList, requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.viewPager.adapter = adapter
    }


}