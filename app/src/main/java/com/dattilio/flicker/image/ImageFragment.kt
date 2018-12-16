package com.dattilio.flicker.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dattilio.flicker.databinding.FragmentImageBinding

class ImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val image = ImageFragmentArgs.fromBundle(arguments).image
        val binding = FragmentImageBinding.inflate(inflater, container, false)
        binding.image = image
        return binding.root
    }
}
