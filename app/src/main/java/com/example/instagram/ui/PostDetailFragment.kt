package com.example.instagram.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.Explode
import com.example.instagram.R
import com.example.instagram.databinding.FragmentPostDetailBinding

class PostDetailFragment : Fragment() {
    private val args by navArgs<PostDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedElementEnterTransition = Explode()
        sharedElementReturnTransition = Explode()

        return inflater.inflate(R.layout.fragment_post_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPostDetailBinding.bind(view)
        binding.post = args.post
    }
}
