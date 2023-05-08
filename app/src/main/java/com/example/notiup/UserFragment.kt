package com.example.notiup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.notiup.databinding.FragmentUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    lateinit var mainActivity : MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        val binding = FragmentUserBinding.bind(view)

        auth = Firebase.auth

        val currentUser = auth.currentUser

        binding.userInfo.text = """
            uid: ${currentUser?.uid}
            email: ${currentUser?.email}
            isEmailVerified: ${currentUser?.isEmailVerified}
            displayName: ${currentUser?.displayName}
        """.trimIndent()

        binding.logOut.setOnClickListener {
            auth.signOut()
            Toast.makeText(mainActivity, "로그아웃 되셨습니다.", Toast.LENGTH_SHORT).show()
            (activity as MainActivity).changeFragment(1)
        }

        return view
    }
}