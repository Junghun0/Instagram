package com.example.instagram.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R
import com.example.instagram.databinding.ItemPostBinding
import com.example.instagram.model.Post
import com.example.instagram.util.findParentNavController
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {
    private lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val query = FirebaseFirestore.getInstance()
            .collection("insta_posts")

        print(query.toString())

        val options = FirestoreRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java)
            .build()

        adapter = PostAdapter(options){view , post ->
            val extras = FragmentNavigatorExtras(
                view to "image"
            )
            val action = TabFragmentDirections.actionTabFragmentToPostDetailFragment(post)
            findParentNavController()?.navigate(action, extras)
        }
        recycler_view.adapter = adapter

        create_button.setOnClickListener {
            findParentNavController()
                ?.navigate(R.id.action_tabFragment_to_createPostFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    class PostAdapter(options: FirestoreRecyclerOptions<Post>
                      , private val callback: (View, Post) -> Unit)
        : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(options){
        class PostViewHolder(val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_post, parent, false)
            val binding = ItemPostBinding.bind(view)
            return PostViewHolder(binding)
        }

        override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
            model.uid = snapshots.getSnapshot(position).id
            holder.binding.post = model
            holder.binding.root.setOnClickListener {

                callback.invoke(holder.binding.imageViewSquare, model)
            }
        }

    }

}
