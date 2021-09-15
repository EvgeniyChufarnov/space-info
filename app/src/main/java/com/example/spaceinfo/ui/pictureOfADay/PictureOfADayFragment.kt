package com.example.spaceinfo.ui.pictureOfADay

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.spaceinfo.R
import com.example.spaceinfo.databinding.FragmentPictureOfADayBinding
import com.example.spaceinfo.domain.data.entities.PictureOfADayEntity
import com.example.spaceinfo.ui.toFirstLettersColoredSpannable
import dagger.hilt.android.AndroidEntryPoint

private const val SHARE_INTENT_TYPE = "text/plain"
private const val SHARED_IMAGE_VIEW_NAME = "full_screen_image"

@AndroidEntryPoint
class PictureOfADayFragment : Fragment(R.layout.fragment_picture_of_a_day) {

    interface Contract {
        fun showPictureFullScreen(path: String, sharedView: View, sharedViewName: String)
    }

    private val binding: FragmentPictureOfADayBinding by viewBinding(FragmentPictureOfADayBinding::bind)
    private val viewModel: PictureOfADayViewModel by viewModels()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_picture_of_a_day, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        postponeEnterTransition()

        binding.pictureOfADayImageView.setOnClickListener {
            viewModel.onPictureClicked()
        }

        viewModel.picture.observe(viewLifecycleOwner, {
            when {
                it.isImage() -> loadImage(it)
                it.isVideo() -> {
                    binding.linkToAVideoImageView.text = it.mediaPath
                    setDescription(it)
                    startPostponedEnterTransition()
                }
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, {
            val message = it ?: getString(R.string.connection_error)

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        viewModel.shareEvent.observe(viewLifecycleOwner, { pictureOfADay ->
            if (pictureOfADay != null) {
                share(pictureOfADay)
                viewModel.onShareFinished()
            }
        })

        viewModel.pictureClickedEvent.observe(viewLifecycleOwner, { path ->
            if (path != null) {
                (requireActivity() as Contract).showPictureFullScreen(
                    path,
                    binding.pictureOfADayImageView,
                    SHARED_IMAGE_VIEW_NAME
                )

                viewModel.onPictureClickedFinished()
            }
        })

        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                PictureOfADayScreenState.LOADING -> {
                    binding.loadingProgressBar.isVisible = true
                    binding.contentLayout.isVisible = false
                    binding.errorTextView.isVisible = false
                }
                PictureOfADayScreenState.SHOW_PICTURE -> {
                    binding.loadingProgressBar.isVisible = false
                    binding.contentLayout.isVisible = true
                    binding.errorTextView.isVisible = false
                    binding.linkToAVideoImageView.isVisible = false
                    binding.pictureOfADayImageView.isVisible = true
                }
                PictureOfADayScreenState.SHOW_VIDEO -> {
                    binding.loadingProgressBar.isVisible = false
                    binding.contentLayout.isVisible = true
                    binding.errorTextView.isVisible = false
                    binding.pictureOfADayImageView.isVisible = false
                    binding.linkToAVideoImageView.isVisible = true
                }
                PictureOfADayScreenState.ERROR -> {
                    binding.loadingProgressBar.isVisible = false
                    binding.contentLayout.isVisible = false
                    binding.errorTextView.isVisible = true
                }
                PictureOfADayScreenState.IDLE -> {
                    binding.loadingProgressBar.isVisible = false
                    binding.contentLayout.isVisible = false
                    binding.errorTextView.isVisible = false
                }
                else -> {
                    throw RuntimeException("Unknown picture of a day state")
                }
            }
        })

        binding.pictureDateChoiceChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.today_chip -> {
                    viewModel.onAnotherDatePictureClicked(PictureDateChoice.TODAY)
                }
                R.id.yesterday_chip -> {
                    viewModel.onAnotherDatePictureClicked(PictureDateChoice.YESTERDAY)
                }
                R.id.two_days_before_chip -> {
                    viewModel.onAnotherDatePictureClicked(PictureDateChoice.TWO_DAYS_AGO)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.share_item -> {
            viewModel.onShareClicked()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun share(pictureOfADay: PictureOfADayEntity) {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${pictureOfADay.title} \n ${pictureOfADay.mediaPath}")
            type = SHARE_INTENT_TYPE
        }
        startActivity(Intent.createChooser(shareIntent, pictureOfADay.title))
    }

    private fun loadImage(pictureOfADay: PictureOfADayEntity) {
        Glide.with(requireContext())
            .asBitmap()
            .load(pictureOfADay.mediaPath)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.pictureOfADayImageView.setImageBitmap(resource)
                    setDescription(pictureOfADay)

                    viewModel.onPictureReady()
                    startPostponedEnterTransition()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    startPostponedEnterTransition()
                }
            })
    }

    private fun setDescription(pictureOfADay: PictureOfADayEntity) {
        binding.titleTextView.text = pictureOfADay.title.toFirstLettersColoredSpannable(
            resources.getColor(R.color.pink_600, null)
        )
        binding.descriptionTextView.text = pictureOfADay.explanation
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireActivity() !is Contract) {
            throw IllegalStateException("Activity must implement PictureOfADay.Contract")
        }
    }
}