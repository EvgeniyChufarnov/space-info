package com.example.spaceinfo.ui.pictureOfADayFullscreen

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.spaceinfo.R
import com.example.spaceinfo.databinding.FragmentFullScreenPictureBinding
import dagger.hilt.android.AndroidEntryPoint

private const val PICTURE_PATH_KEY = "picture_path"

@AndroidEntryPoint
class FullScreenPictureFragment : Fragment(R.layout.fragment_full_screen_picture) {
    companion object {
        fun getInstance(picturePath: String) = FullScreenPictureFragment().apply {
            arguments = Bundle().apply {
                putString(PICTURE_PATH_KEY, picturePath)
            }
        }
    }

    interface Contract {
        fun exitFullScreenState()
        fun closeFullScreenFragment()
    }

    private val binding: FragmentFullScreenPictureBinding by viewBinding(
        FragmentFullScreenPictureBinding::bind
    )

    private var picturePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TransitionInflater.from(requireContext()).let {
            enterTransition = it.inflateTransition(R.transition.fade)
            sharedElementEnterTransition =
                it.inflateTransition(R.transition.picture_of_a_day_transition)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.run {
            if (containsKey(PICTURE_PATH_KEY)) {
                picturePath = getString(PICTURE_PATH_KEY)
            }
        }

        postponeEnterTransition()
        loadImage()

        binding.fullscreenContainer.setOnClickListener {
            (requireActivity() as Contract).closeFullScreenFragment()
        }
    }

    private fun loadImage() {
        Glide.with(this)
            .load(picturePath)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            })
            .into(binding.fullScreenPictureImageView)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireActivity() !is Contract) {
            throw IllegalStateException("Activity must implement FullScreenPictureFragment.Contract")
        }
    }

    override fun onDetach() {
        (requireActivity() as Contract).exitFullScreenState()

        super.onDetach()
    }
}