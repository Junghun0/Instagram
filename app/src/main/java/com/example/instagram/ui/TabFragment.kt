package com.example.instagram.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.instagram.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class TabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = (childFragmentManager.findFragmentById(R.id.tab_nav_host_fragment) as NavHostFragment).navController
        view.findViewById<BottomNavigationView>(R.id.bottom_Navigation)
            .setupWithNavController(navController)
    }


}
