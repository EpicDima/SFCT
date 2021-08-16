package com.epicdima.sfct.exsearch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.epicdima.sfct.R
import com.epicdima.sfct.core.model.Institution
import com.epicdima.sfct.databinding.ExsearchFragmentBinding
import com.epicdima.sfct.databinding.ItemInstitutionBinding
import com.epicdima.sfct.exsearch.parameters.observeParameter
import com.epicdima.sfct.network.RetrofitApiService
import com.epicdima.sfct.utils.SharedViewModelFragment
import com.epicdima.sfct.utils.hideBackButton
import com.epicdima.sfct.utils.requireAppCompatActivity
import com.epicdima.sfct.utils.setStandardProperties
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class ExsearchFragment : Fragment(), SharedViewModelFragment {

    companion object {

        fun newInstance() = ExsearchFragment()
    }

    private lateinit var binding: ExsearchFragmentBinding

    private val viewModel: ExsearchViewModel by viewModels()

    private lateinit var adapter: InstitutionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExsearchFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureCommons()
        prepareExamsLayout()
        prepareParametersLayout()
        prepareAdapter()
        prepareObservers()
    }

    private fun configureCommons() {
        requireAppCompatActivity().supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.arrow_back_icon)
            title = getText(R.string.exsearch_title)
        }
        hideBackButton()

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            status = viewModel.status
            refreshLayout.setColorSchemeResources(R.color.colorPrimary)
            refreshLayout.setOnRefreshListener { search(true) }
        }
    }

    private fun prepareExamsLayout() {
        binding.apply {
            exams = ParametersFragment.ParameterViewItem(getString(R.string.exam_title))
            viewLifecycleOwner.observeParameter(requireContext(), viewModel.exams, binding.exams!!)
            examsLayout.root.setOnClickListener {
                OuterExamsBottomSheetDialog.newInstance().show(childFragmentManager, null)
            }
            examsLayout.resetButton.setOnClickListener {
                if (!viewModel.rawStatus.loading) {
                    viewModel.resetExams()
                    showExamsSnackbar()
                }
            }
        }
    }

    private fun prepareParametersLayout() {
        binding.apply {
            parametersSectionLayout.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_exsearchFragment_to_parametersFragment)
            }

            parametersResetButton.setOnClickListener {
                if (!viewModel.rawStatus.loading) {
                    viewModel.resetParamsWithoutExams()
                    if (viewModel.isExamsDefault()) {
                        showExamsSnackbar()
                    }
                }
            }
        }
    }

    private fun prepareAdapter() {
        adapter = InstitutionsAdapter { index ->
            InstitutionSpecialtiesBottomSheet.newInstance(index)
                .show(childFragmentManager, null)
        }
        binding.recyclerView.setStandardProperties(requireContext(), adapter)
    }

    private fun prepareObservers() {
        viewModel.anyChangeObservable.observe(viewLifecycleOwner) {
            viewModel.exams.setActualToCurrent()
            val count = viewModel.notDefaultParamsCountWithoutExams()
            binding.notDefaultParameterCount = count
            if (count != 0 || !viewModel.exams.isDefault()) {
                search()
            }
        }
        viewModel.resultList.observe(viewLifecycleOwner) {
            binding.empty = it.isEmpty()
            adapter.submitList(it)
        }
    }

    override fun getSharedViewModel(): ExsearchViewModel {
        return viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.open_site_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.open_in_browser -> {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("${RetrofitApiService.BASE_URL}/exsearch")
                )
            )
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun search(forced: Boolean = false) {
        binding.refreshLayout.isRefreshing = false
        if (viewModel.isExamsDefault()) {
            viewModel.setEmptyList()
            showExamsSnackbar()
            return
        }
        if (!viewModel.isSavedParams() || forced) {
            viewModel.search()
        }
    }

    private fun showExamsSnackbar() {
        Snackbar.make(binding.root, getString(R.string.please_choose_exams), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.ok)) {}
            .show()
    }


    private class InstitutionsAdapter(
        private val clickListener: (Int) -> Unit
    ) : ListAdapter<Institution, InstitutionsAdapter.InstitutionHolder>(InstitutionDiffUtil()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstitutionHolder {
            val binding =
                ItemInstitutionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return InstitutionHolder(binding)
        }

        override fun onBindViewHolder(holder: InstitutionHolder, position: Int) {
            holder.bind(getItem(position).name, position, clickListener)
        }


        private class InstitutionHolder(
            private val binding: ItemInstitutionBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(name: String, index: Int, clickListener: (Int) -> Unit) {
                binding.institutionNameTextview.text = name
                binding.root.setOnClickListener {
                    clickListener(index)
                }
            }
        }


        private class InstitutionDiffUtil : DiffUtil.ItemCallback<Institution>() {
            override fun areItemsTheSame(oldItem: Institution, newItem: Institution): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Institution, newItem: Institution): Boolean {
                return oldItem == newItem
            }
        }
    }
}