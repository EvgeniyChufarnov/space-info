package com.example.spaceinfo.ui.picturesFromMars

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.spaceinfo.R
import com.example.spaceinfo.ROVERS_NAMES
import com.example.spaceinfo.databinding.FragmentPicturesFromMarsBinding
import dagger.hilt.android.AndroidEntryPoint

private const val ROVER_NAME_KEY = "rover_name"

@AndroidEntryPoint
class PicturesFromMarsFragment : Fragment(R.layout.fragment_pictures_from_mars) {
    companion object {
        fun getInstance(roverName: String) = PicturesFromMarsFragment().apply {
            arguments = Bundle().apply {
                putString(ROVER_NAME_KEY, roverName)
            }
        }
    }

    private val binding: FragmentPicturesFromMarsBinding by viewBinding(
        FragmentPicturesFromMarsBinding::bind
    )
    private val viewModel: PicturesFromMarsViewModel by viewModels()

    private lateinit var adapter: PicturesFromMarsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        viewModel.roverName = arguments?.getString(ROVER_NAME_KEY) ?: ROVERS_NAMES.first()

        viewModel.pictures.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, {
            val message = it ?: getString(R.string.connection_error)

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                PicturesFromMarsScreenState.LOADING -> {
                    binding.picturesFromMarsRecyclerView.isVisible = false
                    binding.errorTextView.isVisible = false
                    binding.loadingProgressBar.isVisible = true
                }
                PicturesFromMarsScreenState.SHOW_PICTURES -> {
                    binding.picturesFromMarsRecyclerView.isVisible = true
                    binding.errorTextView.isVisible = false
                    binding.loadingProgressBar.isVisible = false
                }
                PicturesFromMarsScreenState.ERROR -> {
                    binding.loadingProgressBar.isVisible = false
                    binding.errorTextView.isVisible = true
                    binding.loadingProgressBar.isVisible = false
                }
                PicturesFromMarsScreenState.IDLE -> {
                    binding.picturesFromMarsRecyclerView.isVisible = false
                    binding.errorTextView.isVisible = false
                    binding.loadingProgressBar.isVisible = false
                }
                else -> {
                    throw RuntimeException("Unknown pictures from mars state")
                }
            }
        })
    }

    private fun initRecyclerView() {
        adapter = PicturesFromMarsAdapter()
        binding.picturesFromMarsRecyclerView.adapter = adapter
        binding.picturesFromMarsRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }
}