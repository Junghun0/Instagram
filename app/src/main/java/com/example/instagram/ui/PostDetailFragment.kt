package com.example.instagram.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Explode
import com.example.instagram.R
import com.example.instagram.databinding.FragmentPostDetailBinding
import com.example.instagram.model.CreatePostViewModel
import kotlinx.android.synthetic.main.fragment_post_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostDetailFragment : Fragment() {
    private val args by navArgs<PostDetailFragmentArgs>()
    private lateinit var viewModel: CreatePostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedElementEnterTransition = Explode()
        sharedElementReturnTransition = Explode()

        viewModel = ViewModelProviders.of(this).get(CreatePostViewModel::class.java)

        return inflater.inflate(R.layout.fragment_post_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPostDetailBinding.bind(view)
        binding.viewModel = viewModel
        binding.post = args.post
        binding.lifecycleOwner = this

        binding.description.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val post = args.post
                post.description = description.text.toString()

                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.updatePost(post) {
                        findNavController().navigateUp()
                    }
                }

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }
}
