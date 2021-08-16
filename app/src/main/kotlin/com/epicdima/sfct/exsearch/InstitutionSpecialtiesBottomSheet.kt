package com.epicdima.sfct.exsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.epicdima.sfct.R
import com.epicdima.sfct.core.model.Specialty
import com.epicdima.sfct.databinding.ItemSpecialtyBottomSheetBinding
import com.epicdima.sfct.databinding.ListHeaderBottomSheetBinding
import com.epicdima.sfct.utils.ExtendedBottomSheetDialog
import com.epicdima.sfct.utils.parentViewModel
import com.epicdima.sfct.utils.setStandardProperties
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class InstitutionSpecialtiesBottomSheet :
    ExtendedBottomSheetDialog<ListHeaderBottomSheetBinding>() {

    companion object {

        fun newInstance(index: Int) = InstitutionSpecialtiesBottomSheet().apply {
            this.index = index
        }
    }

    private val viewModel: ExsearchViewModel by parentViewModel()

    private var index = -1

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        ListHeaderBottomSheetBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (index == -1 || viewModel.resultList.value == null
            || viewModel.resultList.value!!.lastIndex < index
        ) {
            dismiss()
            return
        }
        binding.headerLayout.titleTextview.text = viewModel.resultList.value!![index].name
        val adapter = SpecialtiesAdapter(viewModel.resultList.value!![index].specialties) { id ->
            Navigation.findNavController(requireParentFragment().requireView())
                .navigate(
                    R.id.action_exsearchFragment_to_specialtyFragment,
                    bundleOf("specialtyId" to id)
                )
        }
        binding.recyclerView.setStandardProperties(requireContext(), adapter)
    }

    override fun onCloseButtonClick() {
        dismiss()
    }


    private class SpecialtiesAdapter(
        private val list: List<Specialty>,
        private val selectListener: (Int) -> Unit
    ) : RecyclerView.Adapter<SpecialtiesAdapter.SpecialtyHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialtyHolder {
            val binding = ItemSpecialtyBottomSheetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return SpecialtyHolder(binding)
        }

        override fun onBindViewHolder(holder: SpecialtyHolder, position: Int) {
            holder.bind(list[position], selectListener)
        }

        override fun getItemCount() = list.size

        class SpecialtyHolder(
            private val binding: ItemSpecialtyBottomSheetBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(specialty: Specialty, selectListener: (Int) -> Unit) {
                binding.value = specialty
                binding.root.setOnClickListener { selectListener(specialty.id) }
            }
        }
    }
}