package com.example.android.manager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.android.manager.database.UserModel
import com.example.android.manager.database.saveData
import com.example.android.manager.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*


@Suppress("DEPRECATION")
class SignInActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    var user = mAuth.currentUser
    val RC_SIGN_IN = 100
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        if (user != null){
            updateUI(user)
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        signIn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    user = mAuth.currentUser
                    if (user?.phoneNumber.toString() == "") {
                        val userData = UserModel(user!!.displayName!!, user?.email.toString(), user?.photoUrl.toString(), null)
                        saveData().addUser(userData)
                    }else{
                        val userData = UserModel(user!!.displayName!!, user?.email.toString(), user?.photoUrl.toString(), user?.phoneNumber.toString())
                        saveData().addUser(userData)
                    }
                        updateUI(user)
                } else {
                    Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_LONG).show()
                    updateUI(null)
                }
            }
    }

    fun updateUI(user : FirebaseUser?){
        if (user == null){
            val intent = Intent(this , SignInActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent(this , MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}