package com.epicdima.sfct.exsearch

import android.os.Bundle
import android.view.*
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import com.epicdima.sfct.R
import com.epicdima.sfct.databinding.ParametersFragmentBinding
import com.epicdima.sfct.databinding.ParametersSectionBinding
import com.epicdima.sfct.exsearch.parameters.*
import com.epicdima.sfct.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class ParametersFragment : Fragment(), SharedViewModelFragment {
    companion object {
        const val TAG = "ParametersFragment"

        fun newInstance() = ParametersFragment()
    }

    private val viewModel: ParametersViewModel by viewModels()

    private lateinit var binding: ParametersFragmentBinding

    private var resetMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ParametersFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun getSharedViewModel(): ViewModel = viewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createParameters()
        setObservers()
        setListeners()

        binding.searchButton.setOnClickListener {
            if (viewModel.exams.isDefault()) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.please_choose_exams),
                    Snackbar.LENGTH_SHORT
                )
                    .setAction(getString(R.string.ok), {})
                    .show()
            } else {
                viewModel.save()
                Navigation.findNavController(it).popBackStack()
            }
        }

        requireAppCompatActivity().supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.close_icon)
            title = getText(R.string.parameters_title)
        }
        showBackButton()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.parameters_menu, menu)
        resetMenuItem = menu.findItem(R.id.reset)
        checkDefaultParameters()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.reset -> {
            viewModel.resetAll()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun checkDefaultParameters() {
        resetMenuItem?.isVisible = !viewModel.isAllDefault()
    }

    private fun createParameters() {
        binding.exams = ParameterViewItem(getString(R.string.exam_title))
        binding.region = ParameterViewItem(getString(R.string.region_title))
        binding.teachForm = ParameterViewItem(getString(R.string.teach_form_title))
        binding.typeOfInstitution = ParameterViewItem(getString(R.string.type_of_institution_title))
        binding.paymentForm = ParameterViewItem(getString(R.string.payment_form_title))
        binding.rangeOfPoints = ParameterViewItem(getString(R.string.range_of_points_title))
        binding.points = ParameterViewItem(getString(R.string.points_title))
        binding.dormitory = ParameterViewItem(getString(R.string.dormitory_title))
    }

    private fun setObservers() {
        setExamsObserver()
        viewLifecycleOwner.observeParameter(
            requireContext(),
            viewModel.exams,
            binding.exams!!,
            this::checkDefaultParameters
        )
        viewLifecycleOwner.observeParameter(
            requireContext(),
            viewModel.region,
            binding.region!!,
            this::checkDefaultParameters
        )
        viewLifecycleOwner.observeParameter(
            requireContext(),
            viewModel.teachForm,
            binding.teachForm!!,
            this::checkDefaultParameters
        )
        viewLifecycleOwner.observeParameter(
            requireContext(),
            viewModel.typeOfInstitution,
            binding.typeOfInstitution!!,
            this::checkDefaultParameters
        )
        viewLifecycleOwner.observeParameter(
            requireContext(),
            viewModel.paymentForm,
            binding.paymentForm!!,
            this::checkDefaultParameters
        )
        viewLifecycleOwner.observeParameter(
            requireContext(),
            viewModel.rangeOfPoints,
            binding.rangeOfPoints!!,
            this::checkDefaultParameters
        )
        viewLifecycleOwner.observeParameter(
            requireContext(),
            viewModel.points,
            binding.points!!,
            this::checkDefaultParameters
        )
        viewLifecycleOwner.observeParameter(
            requireContext(),
            viewModel.dormitory,
            binding.dormitory!!,
            this::checkDefaultParameters
        )
    }

    private fun setExamsObserver() {
        viewModel.exams.currentValue.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.searchButton.gone()
            } else {
                binding.searchButton.show()
            }
        }
//        viewModel.exams.currentValue.observe(viewLifecycleOwner) {
//            checkDefaultParameters()
//            binding.region!!.apply {
//                viewModel.exams.apply {
//                    selected.set(convertToStringOfSelected(requireContext()))
//                    defaultValue.set(isDefault())
//                }
//            }
//        }
    }

    private fun setListeners() {
        bindListener(
            binding.examsLayout, viewModel.exams,
            ExamsBottomSheetDialog.TAG, (ExamsBottomSheetDialog)::newInstance
        )
        bindListener(
            binding.regionLayout, viewModel.region,
            RegionBottomSheetDialog.TAG, (RegionBottomSheetDialog)::newInstance
        )
        bindListener(
            binding.teachFormLayout, viewModel.teachForm,
            TeachFormBottomSheetDialog.TAG, (TeachFormBottomSheetDialog)::newInstance
        )
        bindListener(
            binding.typeOfInstitutionLayout,
            viewModel.typeOfInstitution,
            TypeOfInstitutionBottomSheetDialog.TAG,
            (TypeOfInstitutionBottomSheetDialog)::newInstance
        )
        bindListener(
            binding.paymentFormLayout, viewModel.paymentForm,
            PaymentFormBottomSheetDialog.TAG, (PaymentFormBottomSheetDialog)::newInstance
        )
        bindListener(
            binding.rangeOfPointsLayout, viewModel.rangeOfPoints,
            RangeOfPointsBottomSheetDialog.TAG, (RangeOfPointsBottomSheetDialog)::newInstance
        )
        bindListener(
            binding.pointsLayout, viewModel.points,
            PointsBottomSheetDialog.TAG, (PointsBottomSheetDialog)::newInstance
        )
        bindListener(
            binding.dormitoryLayout, viewModel.dormitory,
            DormitoryBottomSheetDialog.TAG, (DormitoryBottomSheetDialog)::newInstance
        )
    }

    private fun <T> bindListener(
        sectionLayout: ParametersSectionBinding,
        singleParameterItem: SingleParameterItem<T>,
        tag: String,
        instantiator: () -> BottomSheetDialogFragment
    ) {
        sectionLayout.root.setOnClickListener {
            instantiator().show(childFragmentManager, tag)
        }
        sectionLayout.resetButton.setOnClickListener { singleParameterItem.reset() }
    }


    class ParameterViewItem(
        val title: String,
        val selected: ObservableField<String> = ObservableField(""),
        val defaultValue: ObservableBoolean = ObservableBoolean(true)
    )
}