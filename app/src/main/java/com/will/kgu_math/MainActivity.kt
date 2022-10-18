package com.will.kgu_math

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.will.kgu_math.fragments.subjects.SubjectsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val prefs = getSharedPreferences("LOGIN", MODE_PRIVATE)
//        val isRegistered = prefs.getBoolean("registered", false)
//
//        supportFragmentManager.commit {
//            add(
//                R.id.nav_host_fragment,
//                if (isRegistered) SubjectsFragment() else RegisterFragment()
//            )
//        }

        supportFragmentManager.commit {
            add(R.id.nav_host_fragment, SubjectsFragment())
        }
    }
}