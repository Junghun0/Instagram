package com.example.instagram.ui


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.LoginActivity
import com.example.instagram.R
import com.example.instagram.databinding.FragmentAccountBinding
import com.example.instagram.model.CreatePostViewModel
import com.example.instagram.model.Post
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
    private val db = FirebaseFirestore.getInstance()
    private val list = arrayListOf<Post>()

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

        adapter = AccountAdapter{
            Toast.makeText(requireContext(),"click",Toast.LENGTH_SHORT)
        }
        account_recyclerView.layoutManager = GridLayoutManager(requireContext(),3)
        account_recyclerView.adapter = adapter
    }

    private fun getAllPosts(email: String){
        var posts: Post
        db.collection("insta_posts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data["email"] == email) {
                        posts = Post(
                            document.data["email"].toString(),
                            document.data["nickName"].toString(),
                            document.data["profileUrl"].toString(),
                            document.data["imageUrl2"].toString(),
                            document.data["description"].toString()
                        )
                        list.add(posts)
                    }
                }
                adapter.item = list
                Log.e("size123123",""+list.size)
            }
            .addOnFailureListener { exception ->
                Log.e("getallposts", "Error getting documents: ", exception)
            }
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

    class AccountAdapter(private val clickListener: (accountPost: Post) -> Unit) :
        RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {
        var item = arrayListOf<Post>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
            val viewHolder = AccountViewHolder(FragmentAccountBinding.bind(view))
            view.setOnClickListener {
                clickListener.invoke(item[viewHolder.adapterPosition])
            }
            return viewHolder
        }

        override fun getItemCount() = item.size

        override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
            holder.binding.post = item[position]
        }

        class AccountViewHolder(val binding: FragmentAccountBinding): RecyclerView.ViewHolder(binding.root)
    }
}


