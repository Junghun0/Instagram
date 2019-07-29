package com.example.instagram.util

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_tab.*

fun Fragment.findParentNavController(): NavController? {
   return (requireActivity().tab_nav_host_fragment as? NavHostFragment)
        ?.navController
}