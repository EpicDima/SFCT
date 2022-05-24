package com.epicdima.sfct.exsearch.parameters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epicdima.sfct.R
import com.epicdima.sfct.core.usecases.ExsearchParams
import com.epicdima.sfct.databinding.BottomSheetListHeaderBinding
import com.epicdima.sfct.exsearch.ParametersViewModel
import com.epicdima.sfct.utils.EnumRadioAdapter
import com.epicdima.sfct.utils.ExtendedBottomSheetDialog
import com.epicdima.sfct.utils.parentViewModel
import com.epicdima.sfct.utils.setStandardProperties
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
abstract class SingleSelectedBottomSheetDialog<E : Enum<E>> :
    ExtendedBottomSheetDialog<BottomSheetListHeaderBinding>() {

    protected val viewModel: ParametersViewModel by parentViewModel()

    protected lateinit var adapter: SingleSelectedAdapter

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        BottomSheetListHeaderBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = createAdapter()
        configureAdapter()
        binding.recyclerView.setStandardProperties(requireContext(), adapter)
    }

    override fun onCloseButtonClick() {
        dismiss()
    }

    protected abstract fun createAdapter(): SingleSelectedAdapter

    protected abstract fun configureAdapter()


    inner class SingleSelectedAdapter(
        context: Context,
        enumClass: Class<E>,
        selectListener: (E) -> Unit
    ) : EnumRadioAdapter<E, EnumRadioAdapter.EnumViewHolder<E>>(
        context, selectListener, enumClass
    ) {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): EnumViewHolder<E> {
            return EnumViewHolder(createBinding(parent))
        }
    }
}


/**
 * @author EpicDima
 */
@AndroidEntryPoint
class RegionBottomSheetDialog : SingleSelectedBottomSheetDialog<ExsearchParams.Region>() {

    companion object {

        fun newInstance() = RegionBottomSheetDialog()
    }

    override fun getTitleStringId(): Int = R.string.region_title

    override fun createAdapter(): SingleSelectedAdapter {
        return SingleSelectedAdapter(requireContext(), ExsearchParams.Region::class.java) {
            viewModel.region.currentValue.value = it
            dismiss()
        }
    }

    override fun configureAdapter() {
        adapter.select(viewModel.region.currentValue.value!!)
    }
}


/**
 * @author EpicDima
 */
@AndroidEntryPoint
class TeachFormBottomSheetDialog : SingleSelectedBottomSheetDialog<ExsearchParams.TeachForm>() {

    companion object {

        fun newInstance() = TeachFormBottomSheetDialog()
    }

    override fun getTitleStringId(): Int = R.string.teach_form_title

    override fun createAdapter(): SingleSelectedAdapter {
        return SingleSelectedAdapter(requireContext(), ExsearchParams.TeachForm::class.java) {
            viewModel.teachForm.currentValue.value = it
            dismiss()
        }
    }

    override fun configureAdapter() {
        adapter.select(viewModel.teachForm.currentValue.value!!)
    }
}


/**
 * @author EpicDima
 */
@AndroidEntryPoint
class TypeOfInstitutionBottomSheetDialog :
    SingleSelectedBottomSheetDialog<ExsearchParams.TypeOfInstitution>() {

    companion object {

        fun newInstance() = TypeOfInstitutionBottomSheetDialog()
    }

    override fun getTitleStringId(): Int = R.string.type_of_institution_title

    override fun createAdapter(): SingleSelectedAdapter {
        return SingleSelectedAdapter(
            requireContext(),
            ExsearchParams.TypeOfInstitution::class.java
        ) {
            viewModel.typeOfInstitution.currentValue.value = it
            dismiss()
        }
    }

    override fun configureAdapter() {
        adapter.select(viewModel.typeOfInstitution.currentValue.value!!)
    }
}


/**
 * @author EpicDima
 */
@AndroidEntryPoint
class PaymentFormBottomSheetDialog : SingleSelectedBottomSheetDialog<ExsearchParams.PaymentForm>() {

    companion object {

        fun newInstance() = PaymentFormBottomSheetDialog()
    }

    override fun getTitleStringId(): Int = R.string.payment_form_title

    override fun createAdapter(): SingleSelectedAdapter {
        return SingleSelectedAdapter(requireContext(), ExsearchParams.PaymentForm::class.java) {
            viewModel.paymentForm.currentValue.value = it
            dismiss()
        }
    }

    override fun configureAdapter() {
        adapter.select(viewModel.paymentForm.currentValue.value!!)
    }
}


/**
 * @author EpicDima
 */
@AndroidEntryPoint
class RangeOfPointsBottomSheetDialog :
    SingleSelectedBottomSheetDialog<ExsearchParams.RangeOfPoints>() {

    companion object {

        fun newInstance() = RangeOfPointsBottomSheetDialog()
    }

    override fun getTitleStringId(): Int = R.string.range_of_points_title

    override fun createAdapter(): SingleSelectedAdapter {
        return SingleSelectedAdapter(requireContext(), ExsearchParams.RangeOfPoints::class.java) {
            viewModel.rangeOfPoints.currentValue.value = it
            dismiss()
        }
    }

    override fun configureAdapter() {
        adapter.select(viewModel.rangeOfPoints.currentValue.value!!)
    }
}


/**
 * @author EpicDima
 */
@AndroidEntryPoint
open class DormitoryBottomSheetDialog :
    SingleSelectedBottomSheetDialog<ExsearchParams.Dormitory>() {

    companion object {

        fun newInstance() = DormitoryBottomSheetDialog()
    }

    override fun getTitleStringId(): Int = R.string.dormitory_title

    override fun createAdapter(): SingleSelectedAdapter {
        return SingleSelectedAdapter(requireContext(), ExsearchParams.Dormitory::class.java) {
            viewModel.dormitory.currentValue.value = it
            dismiss()
        }
    }

    override fun configureAdapter() {
        adapter.select(viewModel.dormitory.currentValue.value!!)
    }
}