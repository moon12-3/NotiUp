package com.example.notiup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if(currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.login_btn).setOnClickListener {
            val email = findViewById<EditText>(R.id.user_id).text.toString()
            val password = findViewById<EditText>(R.id.user_pass).text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        val user = auth.currentUser
                        Log.d("mytag", "로그인 성공 ${user.toString()}")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    else {
                        Log.d("mytag", "로그인 실패 (사유 : ${task.exception}")
                        Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        findViewById<TextView>(R.id.join_btn).setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}