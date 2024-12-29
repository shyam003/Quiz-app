package com.example.loginfirebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.loginfirebase.fragments.HomeFragment
import com.example.loginfirebase.fragments.ProfileFragment
import com.example.loginfirebase.fragments.RankFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity2 : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.hide()
        val homeFragment= HomeFragment()
        val rankFragment= RankFragment()
        val profileFragment=ProfileFragment()
        bottomNavigationView=findViewById(R.id.bottom_navbar)
        makeCurrentFragment(homeFragment)
          bottomNavigationView.setOnItemSelectedListener {
              when(it.itemId)
              {
                  R.id.main-> {
                      makeCurrentFragment(homeFragment)
                  }
                  R.id.rank->{makeCurrentFragment(rankFragment)
                  }
                  R.id.profile-> {
                      makeCurrentFragment(profileFragment)
                  }
              }

               true

          }

    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame,fragment)
            commit()
        }

    override fun onBackPressed() {

    }
}