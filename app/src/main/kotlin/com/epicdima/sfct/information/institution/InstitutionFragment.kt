package com.epicdima.sfct.information.institution

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.epicdima.sfct.R
import com.epicdima.sfct.core.model.Institution
import com.epicdima.sfct.core.model.Specialty
import com.epicdima.sfct.databinding.FragmentInstitutionBinding
import com.epicdima.sfct.databinding.ItemSpecialtyBinding
import com.epicdima.sfct.network.RetrofitApiService
import com.epicdima.sfct.utils.*
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class InstitutionFragment : Fragment() {

    companion object {

        fun newInstance() = InstitutionFragment()
    }

    private lateinit var binding: FragmentInstitutionBinding

    private val args: InstitutionFragmentArgs by navArgs()

    private val viewModel: InstitutionViewModel by viewModels()

    private lateinit var adapter: SpecialtiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInstitutionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureCommons()
        createAdapter()
        setObservers()
        viewModel.downloadInstitution(args.institutionId)
    }

    private fun configureCommons() {
        requireAppCompatActivity().supportActionBar?.apply {
            title = getText(R.string.institution_title)
        }
        showBackButton()

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            status = viewModel.status
            loadingDormitory = viewModel.loadingDormitory
            siteLayout.infoTextview.apply {
                setTextIsSelectable(false)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.linkColor))
            }
        }
    }

    private fun createAdapter() {
        adapter = SpecialtiesAdapter { id ->
            Navigation.findNavController(requireView())
                .navigate(
                    R.id.action_institutionFragment_to_specialtyFragment,
                    bundleOf("specialtyId" to id)
                )
        }
        binding.specialtiesRecyclerview.setStandardProperties(requireContext(), adapter)
    }

    private fun setObservers() {
        viewModel.institution.observe(viewLifecycleOwner) { institution ->
            institution?.also { onInstitutionDownload(it) }
        }
        viewModel.dormitory.observe(viewLifecycleOwner) { dormitory ->
            if (dormitory.isNullOrEmpty()) {
                binding.dormitoryInfoTextview.text = getText(R.string.no_dormitory_info)
            } else {
                onDormitoryDownload(dormitory)
            }
        }
    }

    private fun onInstitutionDownload(institution: Institution) {
        binding.apply {
            this.institution = institution
            siteLayout.root.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(institution.site)))
            }
        }

        if (institution.specialties.isEmpty()) {
            binding.specialtiesLayout.gone()
        } else {
            adapter.submitList(institution.specialties)
        }
    }

    private fun onDormitoryDownload(dormitory: String) {
        binding.apply {
            dormitoryInfoTextview.text = dormitory
            dormitoryInfoTextview.onPreDraw()
            if (dormitoryInfoTextview.layout.getEllipsisCount(dormitoryInfoTextview.lineCount - 1) != 0) {
                dormitoryInfoLayout.setOnClickListener { showMoreClickListener() }
                readMoreButton.show()
            }
        }
    }

    private fun showLessClickListener() {
        binding.apply {
            readMoreButton.text = getText(R.string.read_more)
            dormitoryInfoTextview.maxLines = 5
            readMoreButton.setOnClickListener { showMoreClickListener() }
            dormitoryInfoLayout.setOnClickListener { showMoreClickListener() }
            if (scrollView.scrollY > dormitoryInfoLayout.top) {
                scrollView.scrollTo(0, dormitoryInfoLayout.top)
            }
        }
    }

    private fun showMoreClickListener() {
        binding.apply {
            dormitoryInfoLayout.setOnClickListener(null)
            dormitoryInfoTextview.maxLines = Int.MAX_VALUE
            readMoreButton.text = getText(R.string.roll_up)
            readMoreButton.setOnClickListener { showLessClickListener() }
        }
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
                    Uri.parse("${RetrofitApiService.BASE_URL}/zavedenie/id/${args.institutionId}")
                )
            )
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private class SpecialtiesAdapter(
        private val clickListener: (Int) -> Unit
    ) : ListAdapter<Specialty, SpecialtiesAdapter.SpecialtyHolder>(SpecialtyDiffUtil()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialtyHolder {
            val binding =
                ItemSpecialtyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SpecialtyHolder(binding)
        }

        override fun onBindViewHolder(holder: SpecialtyHolder, position: Int) {
            holder.bind(getItem(position), clickListener)
        }


        private class SpecialtyHolder(
            private val binding: ItemSpecialtyBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(specialty: Specialty, clickListener: (Int) -> Unit) {
                binding.specialtyNameTextview.text = specialty.name
                binding.root.setOnClickListener {
                    clickListener(specialty.id)
                }
            }
        }


        private class SpecialtyDiffUtil : DiffUtil.ItemCallback<Specialty>() {

            override fun areItemsTheSame(oldItem: Specialty, newItem: Specialty): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Specialty, newItem: Specialty): Boolean {
                return oldItem == newItem
            }
        }
    }
}