package com.epicdima.sfct.information.specialty

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
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
import com.epicdima.sfct.core.model.PassingScore
import com.epicdima.sfct.core.model.Specialty
import com.epicdima.sfct.databinding.ItemScoreBinding
import com.epicdima.sfct.databinding.SpecialtyFragmentBinding
import com.epicdima.sfct.network.RetrofitApiService
import com.epicdima.sfct.utils.*
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author EpicDima
 */
@AndroidEntryPoint
class SpecialtyFragment : Fragment() {

    companion object {

        fun newInstance() = SpecialtyFragment()
    }

    private lateinit var binding: SpecialtyFragmentBinding

    private val args: SpecialtyFragmentArgs by navArgs()

    private val viewModel: SpecialtyViewModel by viewModels()

    private lateinit var adapter: ScoresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SpecialtyFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureCommons()
        setObservers()
        viewModel.downloadSpecialty(args.specialtyId)
    }

    private fun configureCommons() {
        requireAppCompatActivity().supportActionBar?.apply {
            title = getText(R.string.specialty_title)
        }
        showBackButton()

        adapter = ScoresAdapter()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            status = viewModel.status
            scoresRecyclerView.setStandardProperties(requireContext(), adapter)
        }
    }

    private fun setObservers() {
        viewModel.institution.observe(viewLifecycleOwner) { institution ->
            institution?.also { onInstitutionDownload(it) }
        }
        viewModel.specialty.observe(viewLifecycleOwner) { specialty ->
            specialty?.also { onSpecialtyDownload(specialty) }
        }
    }

    private fun onInstitutionDownload(institution: Institution) {
        binding.apply {
            this.institution = institution
            institutionNameTextview.setOnClickListener {
                Navigation.findNavController(it).navigate(
                    R.id.action_specialtyFragment_to_institutionFragment,
                    bundleOf("institutionId" to institution.id)
                )
            }
        }
    }

    private fun onSpecialtyDownload(specialty: Specialty) {
        binding.apply {
            this.specialty = specialty
            if (specialty.scores.isEmpty()) {
                scoresLayout.gone()
            } else {
                adapter.submitList(specialty.scores)
                scoresLayout.show()
            }
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
                    Uri.parse("${RetrofitApiService.BASE_URL}/zavdata/id/${args.specialtyId}")
                )
            )
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private class ScoresAdapter :
        ListAdapter<PassingScore, ScoresAdapter.ScoreHolder>(ScoreDiffUtil()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreHolder {
            val binding =
                ItemScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ScoreHolder(binding)
        }

        override fun onBindViewHolder(holder: ScoreHolder, position: Int) {
            holder.bind(getItem(position))
        }

        private class ScoreHolder(
            private val binding: ItemScoreBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(score: PassingScore) {
                binding.score = score
            }
        }


        private class ScoreDiffUtil : DiffUtil.ItemCallback<PassingScore>() {

            override fun areItemsTheSame(oldItem: PassingScore, newItem: PassingScore): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PassingScore, newItem: PassingScore): Boolean {
                return oldItem == newItem
            }
        }
    }
}