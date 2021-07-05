package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter

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
        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters
//        binding.upcomingElectionRc.adapter = ElectionAdapter(ElectionListener { election ->
//            //viewModel.onAsteroidClicked(asteroidId)
//        })
        //TODO: Populate recycler adapters

        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}