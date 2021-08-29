package com.example.spaceinfo.ui.pictureOfADay

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.spaceinfo.R
import com.example.spaceinfo.databinding.PictureOfADayFragmentBinding
import com.example.spaceinfo.domain.data.entities.PictureOfADayEntity
import dagger.hilt.android.AndroidEntryPoint

private const val SHARE_INTENT_TYPE = "text/plain"

@AndroidEntryPoint
class PictureOfADayFragment : Fragment() {
    private val binding: PictureOfADayFragmentBinding by viewBinding(PictureOfADayFragmentBinding::bind)
    private val viewModel: PictureOfADayViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.picture_of_a_day_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.picture_of_a_day_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        viewModel.picture.observe(viewLifecycleOwner, {
            when {
                it.isImage() -> loadImage(it)
                it.isVideo() -> {
                    setDescription(it)
                    //todo not yet implemented
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

        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                PictureDateState.LOADING -> {
                    binding.contentLayout.isVisible = false
                    binding.errorTextView.isVisible = false
                }
                PictureDateState.SHOW_PICTURE -> {
                    binding.contentLayout.isVisible = true
                    binding.errorTextView.isVisible = false
                    binding.pictureOfADayImageView.isVisible = true
                }
                PictureDateState.SHOW_VIDEO -> {
                    binding.contentLayout.isVisible = true
                    binding.errorTextView.isVisible = false
                    binding.pictureOfADayImageView.isVisible = false
                }
                PictureDateState.ERROR -> {
                    binding.contentLayout.isVisible = false
                    binding.errorTextView.isVisible = true
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
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun setDescription(pictureOfADay: PictureOfADayEntity) {
        binding.titleTextView.text = pictureOfADay.title
        binding.descriptionTextView.text = pictureOfADay.explanation
    }
}