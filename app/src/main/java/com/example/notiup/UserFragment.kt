package com.example.notiup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
        var view : View
        auth = Firebase.auth

        val currentUser = auth.currentUser

        if(currentUser == null) {   // 로그인 전
            view = inflater.inflate(R.layout.fragment_go_login, container, false)

            view.findViewById<TextView>(R.id.login_btn).setOnClickListener {
                activity?.let {
                    val intent = Intent(it, LoginActivity::class.java)
                    it.startActivity(intent)
                }
            }
        }
        else{   // 로그인 후
            view = inflater.inflate(R.layout.fragment_user, container, false)
            val binding = FragmentUserBinding.bind(view)

            binding.logOut.setOnClickListener {
                auth.signOut()
                Toast.makeText(mainActivity, "로그아웃 되셨습니다.", Toast.LENGTH_SHORT).show()
                (activity as MainActivity).changeFragment(1)
            }
        }

//        binding.userInfo.text = """
//            uid: ${currentUser?.uid}
//            email: ${currentUser?.email}
//            isEmailVerified: ${currentUser?.isEmailVerified}
//            displayName: ${currentUser?.displayName}
//        """.trimIndent()



        return view
    }
}