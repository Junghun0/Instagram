package com.example.instagram.ui


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.LoginActivity
import com.example.instagram.R
import com.example.instagram.databinding.AccountItemLayoutBinding
import com.example.instagram.databinding.FragmentAccountBinding
import com.example.instagram.model.CreatePostViewModel
import com.example.instagram.model.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: FragmentAccountBinding
    private lateinit var viewModel: CreatePostViewModel
    private lateinit var email: String
    private lateinit var adapter: AccountAdapter
    private val user = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        setHasOptionsMenu(true)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        binding = FragmentAccountBinding.bind(view)

        FirebaseAuth.getInstance().currentUser?.let { user ->
            binding.user = user
            email = user.email.toString()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreatePostViewModel::class.java)

        val query = FirebaseFirestore.getInstance()
            .collection("insta_posts")
            .whereEqualTo("email",user.currentUser?.email)

        val options = FirestoreRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java)
            .build()

        query.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            binding.postCount = querySnapshot?.size()
        }

        adapter = AccountAdapter(options)
        account_recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.account, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {

            googleSignInClient.signOut()
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    class AccountAdapter(
        options: FirestoreRecyclerOptions<Post>
    ) : FirestoreRecyclerAdapter<Post, AccountAdapter.AccountViewHolder>(options) {

        class AccountViewHolder(val binding: AccountItemLayoutBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.account_item_layout, parent, false)
            val binding = AccountItemLayoutBinding.bind(view)
            return AccountViewHolder(binding)
        }

        override fun onBindViewHolder(holder: AccountViewHolder, position: Int, model: Post) {
            holder.binding.post = model
        }
    }
}


