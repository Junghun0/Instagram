package com.example.instagram.util

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*

fun Fragment.findParentNavController(): NavController? {
   return (requireActivity().nav_host_fragment as? NavHostFragment)
        ?.navController
}