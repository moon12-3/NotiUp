package com.example.notiup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth
        db = Firebase.firestore

        val currentUser = auth.currentUser
        val joinBtn = findViewById<Button>(R.id.join_btn)

        joinBtn.setOnClickListener {
            val email = findViewById<EditText>(R.id.user_id).text.toString()
            val password = findViewById<EditText>(R.id.user_pass).text.toString()
            val name = findViewById<EditText>(R.id.user_name).text.toString()

            joinBtn.isEnabled = false

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(baseContext, "회원가입 성공", Toast.LENGTH_SHORT).show()

                        val user = auth.currentUser!!
                        db.collection("users").document(user.uid).set(name)
                            .addOnSuccessListener {
                                Log.d("mytag", "UserDB successfully written!")
                                finish()
                            }
                            .addOnFailureListener {
                                    e -> Log.d("mytag", "Error writing document", e)
                            }
                        Log.d("mytag", "회원 가입(=유저 생성) 성공 ${user.toString()}")

                    } else {
                        Log.w("mytag", "회원 가입 실패 (사유 : ${task.exception}")
                        Toast.makeText(baseContext, "회원 가입 실패", Toast.LENGTH_SHORT).show()
                        joinBtn.isEnabled = true
                    }
                }
        }
    }
}