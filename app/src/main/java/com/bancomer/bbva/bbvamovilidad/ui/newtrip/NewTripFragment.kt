package com.bancomer.bbva.bbvamovilidad.ui.newtrip

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.databinding.FragmentNewTripBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class NewTripFragment : Fragment() {

    private lateinit var binding: FragmentNewTripBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTripBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initView()
        initG()

        var bottomSheet = BottomSheetDialog(requireContext())

        //inflating layout

        //inflating layout
        val view = View.inflate(context, R.layout.dialog_bottomsheet, null)

        //binding views to data binding.

        //binding views to data binding.


        //setting layout with bottom sheet

        //setting layout with bottom sheet
        bottomSheet.setContentView(view)

//        bottomSheet = BottomSheetBehavior.from(view.parent as View)


//        var view = layoutInflater.inflate(R.layout.dialog_bottomsheet, null)
//        var bottonSheetView = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_bottomsheet, null)
//        var behavior = BottomSheetBehavior.from(bottonSheetView.parent as View)
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//        dialog.show()
//        val btnClose = view.findViewById<Button>(R.id.btnCancel)
//        btnClose.setOnClickListener { dialog.dismiss() }
//        dialog.setCancelable(false)
//        dialog.setContentView(view)
//        var ttt = BottomSheetBehavior<View>()
//        ttt = BottomSheetBehavior.from(behavior.parent as View)
//        ttt.state = BottomSheetBehavior.STATE_EXPANDED


    }

    private fun initG() {
        var fullScreen: Boolean = true
        val dialg = BottomSheetDialog(requireContext())
        dialg.setOnShowListener {
            val btSheet: FrameLayout =
                dialg.findViewById(com.google.android.material.R.id.design_bottom_sheet)
                    ?: return@setOnShowListener
            val bottomSheetBehavior = BottomSheetBehavior.from(btSheet)
            if (fullScreen && btSheet.layoutParams != null) {
                showFullScreenBottomSheet(btSheet)
            }
            btSheet.setBackgroundResource(android.R.color.background_dark)
            expandBottomSheet(bottomSheetBehavior)
        }
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_bottomsheet, null)
        val btnCancelButton = view.findViewById<Button>(R.id.btnCancel)
        btnCancelButton.setOnClickListener { dialg.dismiss() }
        val btnCancelIcon = view.findViewById<ImageView>(R.id.ibCanel)
        btnCancelIcon.setOnClickListener { dialg.dismiss() }

        dialg.setContentView(view)
        dialg.show()
    }


    private fun expandBottomSheet(bottomSheetBehavior: BottomSheetBehavior<FrameLayout>) {
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun showFullScreenBottomSheet(bottomSheet: FrameLayout) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = Resources.getSystem().displayMetrics.heightPixels
        bottomSheet.layoutParams = layoutParams
    }

//    private fun initView() {
//        val dialg = BottomSheetDialog(requireContext())
//        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_bottomsheet, null)
//        dialg.setContentView(view)
//        dialg.show()
//    }


}