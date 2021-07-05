package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.launch.LaunchFragmentDirections

class ElectionsFragment : Fragment() {

    //: Declare ViewModel
    private lateinit var viewModel: ElectionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //: Add ViewModel values and create ViewModel
        val database = ElectionDatabase.getInstance(requireContext())
        viewModel = ViewModelProvider(
            this,
            ElectionsViewModelFactory(database)
        ).get(ElectionsViewModel::class.java)

        //: Add binding values
        val binding = FragmentElectionBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        //: Link elections to voter info
        viewModel.navigateToVoterInfoFragment.observe(viewLifecycleOwner, {
            it?.let { election ->
                this.findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        election.id,
                        election.division
                    )
                )
                viewModel.onNavigationComplete()

            }
        })

        //TODO: Initiate recycler adapters
        binding.upcomingElectionRc.adapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.onElectionClicked(election)
        })
        //TODO: Populate recycler adapters


        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}