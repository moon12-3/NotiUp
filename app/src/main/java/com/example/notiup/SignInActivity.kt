package com.example.notiup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.notiup.viewModel.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var db : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth
        db = Firebase.database.reference

        val currentUser = auth.currentUser
        val joinBtn = findViewById<Button>(R.id.join_btn)

        joinBtn.setOnClickListener {
            val email = findViewById<EditText>(R.id.user_id).text.toString()
            val password = findViewById<EditText>(R.id.user_pass).text.toString()
            val rePassword = findViewById<EditText>(R.id.re_pass).text.toString()
            val name = findViewById<EditText>(R.id.user_name).text.toString()
            Log.d("mytag", name);
//            joinBtn.isEnabled = false
            if(email!="" && password!="" && password==rePassword && name!="") {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, "회원가입 성공", Toast.LENGTH_SHORT).show()

                            val user = auth.currentUser?.uid!!
                            val userModel = UserModel(name, 0)
                            addUserToDatabase(userModel, user)
                            finish()
                            Log.d("mytag", "회원 가입(=유저 생성) 성공 ${user.toString()}")

                        } else {
                            Log.w("mytag", "회원 가입 실패 (사유 : ${task.exception}")
                            Toast.makeText(baseContext, "회원 가입 실패", Toast.LENGTH_SHORT).show()
                            joinBtn.isEnabled = true
                        }
                    }
            }
            else {
                Toast.makeText(baseContext, "공란이 있거나 재입력한 비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun addUserToDatabase(userModel : UserModel, uId : String) {
        db.child("users").child(uId).setValue(userModel)
    }
}