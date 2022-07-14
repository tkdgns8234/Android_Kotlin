package com.hoon.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hoon.instagramclone.databinding.ActivityLoginBinding

/*
TODO::
  1. activity 코드 정리
  2. 구글 로그인 코드 분석
  3. 페이스북까지 해보기
  4. 하울 강의 마저보기!
  5. registerForActivityResult 다시 공부하기

 */

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    // Firebase Authentication 관리 클래스
    lateinit var auth: FirebaseAuth

    // GoogleLogin 관리 클래스
    var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.emailLoginButton.setOnClickListener { createAndLoginEmail() }

        //구글 로그인 옵션
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //구글 로그인 클래스를 만듬
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleSignInButton.setOnClickListener { googleLogin() }
    }

    fun googleLogin() {
        var signInIntent = googleSignInClient?.signInIntent
        loginLauncher.launch(signInIntent)
    }

    /* Google Auth 로그인 결과 수신 */
    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    task.getResult(ApiException::class.java)?.let { account ->
                        firebaseAuthWithGoogle(account)
                    } ?: throw Exception()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //다음페이지 호출
                    moveMainPage()
                }
            }
    }

    private fun createAndLoginEmail() {
        auth.createUserWithEmailAndPassword(
            binding.emailEdittext.toString(),
            binding.passwordEdittext.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //아이디 생성이 성공했을 경우
                Toast.makeText(
                    this,
                    getString(R.string.signup_complete), Toast.LENGTH_SHORT
                ).show()

                //다음페이지 호출
                moveMainPage()
            } else {
                //아이디 생성도 안되고 에러도 발생되지 않았을 경우 로그인
                signinEmail()
            }
        }
    }

    private fun signinEmail() {
        auth.signInWithEmailAndPassword(
            binding.emailEdittext.toString(),
            binding.passwordEdittext.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                moveMainPage()
            } else {
                //로그인 실패
                Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveMainPage() {
        if (auth.currentUser != null) {
            Toast.makeText(this, getString(R.string.signin_complete), Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}