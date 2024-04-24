package com.example.tripy.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripy.R
import com.example.tripy.databinding.FragmentMainBinding
import com.example.tripy.databinding.ToolBarBinding
import com.example.tripy.presentation.Adapters.TripAdapter
import com.example.tripy.presentation.ui.main2.TripActivity
import com.example.tripy.room.TripTable
import com.example.tripy.utils.Constants
import com.example.tripy.viewmodels.TripViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(), TripAdapter.OnItemClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var _toolbarBinding: ToolBarBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    private lateinit var tripAdapter: TripAdapter
    private lateinit var tripList: List<TripTable>

    private val viewModel: TripViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater)
      //  _toolbarBinding = ToolBarBinding.bind(binding.toolbar.root)

        Log.e("onCreateView-->", "main fragment")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tripAdapter = TripAdapter(emptyList(), this)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = tripAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allTrips.collect { trips ->
                Log.e(" collect trip -->", trips.toString())
                tripList = trips
                tripAdapter.updateData(tripList)
            }
        }
    //    toolbarBinding.title.text = getString(R.string.app_name)
    //    toolbarBinding.title.autoSizeTextAvailableSizes
    //    toolbarBinding.icon.visibility = View.GONE
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.e("onViewStateRestored-->", "main fragment")
    }

    override fun onResume() {
        super.onResume()
       (activity as HomeActivity?)?.showBottomNavigationBar()
       // requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }


    override fun onStart() {
        super.onStart()

        Log.e("onStart-->", "main fragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _toolbarBinding = null
    }

    override fun onItemClick(trip: TripTable) {
        Log.d("item click-->", "itemview clicked ")


        //   bundle.putParcelable("selected_trip", trip)

        //   val fragment = TripFragment()
        //  fragment.arguments = bundle
        /*
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left)
                transaction.replace(R.id.fragContainer, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
                */

        val bundle = Bundle()
        bundle.putParcelable(Constants.ARG_SELECTED_TRIP, trip)

        val intent = Intent(requireActivity(), TripActivity::class.java)
        intent.putExtras(bundle)
      //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        requireActivity().startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_in,R.anim.slide_out)

    }
}
