package com.epicdima.sfct.exsearch

import android.os.Bundle
import android.view.*
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.epicdima.sfct.R
import com.epicdima.sfct.databinding.FragmentParametersBinding
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

        fun newInstance() = ParametersFragment()
    }

    private val viewModel: ParametersViewModel by viewModels()

    private lateinit var binding: FragmentParametersBinding

    private var resetMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParametersBinding.inflate(inflater)
        return binding.root
    }

    override fun getSharedViewModel(): ViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireAppCompatActivity().supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.close_icon)
            title = getText(R.string.parameters_title)
        }
        showBackButton()

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
                    .setAction(getString(R.string.ok)) {}
                    .show()
            } else {
                viewModel.save()
                Navigation.findNavController(it).popBackStack()
            }
        }
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
        binding.apply {
            exams = ParameterViewItem(getString(R.string.exam_title))
            region = ParameterViewItem(getString(R.string.region_title))
            teachForm = ParameterViewItem(getString(R.string.teach_form_title))
            typeOfInstitution = ParameterViewItem(getString(R.string.type_of_institution_title))
            paymentForm = ParameterViewItem(getString(R.string.payment_form_title))
            rangeOfPoints = ParameterViewItem(getString(R.string.range_of_points_title))
            points = ParameterViewItem(getString(R.string.points_title))
            dormitory = ParameterViewItem(getString(R.string.dormitory_title))
        }
    }

    private fun setObservers() {
        setExamsObserver()
        viewLifecycleOwner.apply {
            observeParameter(
                requireContext(),
                viewModel.exams,
                binding.exams!!,
                this@ParametersFragment::checkDefaultParameters
            )

            observeParameter(
                requireContext(),
                viewModel.region,
                binding.region!!,
                this@ParametersFragment::checkDefaultParameters
            )

            observeParameter(
                requireContext(),
                viewModel.teachForm,
                binding.teachForm!!,
                this@ParametersFragment::checkDefaultParameters
            )

            observeParameter(
                requireContext(),
                viewModel.typeOfInstitution,
                binding.typeOfInstitution!!,
                this@ParametersFragment::checkDefaultParameters
            )

            observeParameter(
                requireContext(),
                viewModel.paymentForm,
                binding.paymentForm!!,
                this@ParametersFragment::checkDefaultParameters
            )

            observeParameter(
                requireContext(),
                viewModel.rangeOfPoints,
                binding.rangeOfPoints!!,
                this@ParametersFragment::checkDefaultParameters
            )

            observeParameter(
                requireContext(),
                viewModel.points,
                binding.points!!,
                this@ParametersFragment::checkDefaultParameters
            )

            observeParameter(
                requireContext(),
                viewModel.dormitory,
                binding.dormitory!!,
                this@ParametersFragment::checkDefaultParameters
            )
        }
    }

    private fun setExamsObserver() {
        viewModel.exams.currentValue.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.searchButton.gone()
            } else {
                binding.searchButton.show()
            }
        }
    }

    private fun setListeners() {
        bindListener(
            binding.examsLayout, viewModel.exams,
            ExamsBottomSheetDialog::javaClass.name, (ExamsBottomSheetDialog)::newInstance
        )
        bindListener(
            binding.regionLayout, viewModel.region,
            RegionBottomSheetDialog::javaClass.name, (RegionBottomSheetDialog)::newInstance
        )
        bindListener(
            binding.teachFormLayout, viewModel.teachForm,
            TeachFormBottomSheetDialog::javaClass.name, (TeachFormBottomSheetDialog)::newInstance
        )
        bindListener(
            binding.typeOfInstitutionLayout,
            viewModel.typeOfInstitution,
            TypeOfInstitutionBottomSheetDialog::javaClass.name,
            (TypeOfInstitutionBottomSheetDialog)::newInstance
        )
        bindListener(
            binding.paymentFormLayout,
            viewModel.paymentForm,
            PaymentFormBottomSheetDialog::javaClass.name,
            (PaymentFormBottomSheetDialog)::newInstance
        )
        bindListener(
            binding.rangeOfPointsLayout,
            viewModel.rangeOfPoints,
            RangeOfPointsBottomSheetDialog::javaClass.name,
            (RangeOfPointsBottomSheetDialog)::newInstance
        )
        bindListener(
            binding.pointsLayout, viewModel.points,
            PointsBottomSheetDialog::javaClass.name, (PointsBottomSheetDialog)::newInstance
        )
        bindListener(
            binding.dormitoryLayout, viewModel.dormitory,
            DormitoryBottomSheetDialog::javaClass.name, (DormitoryBottomSheetDialog)::newInstance
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