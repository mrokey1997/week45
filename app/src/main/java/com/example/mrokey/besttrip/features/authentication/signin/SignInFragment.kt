package com.example.mrokey.besttrip.features.authentication.signin

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_sign_in.*
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.GoogleApiClient

/**
 * TOKEN: google API key
 */
class SignInFragment: Fragment(),SignInContract.View{
    private var presenter: SignInContract.Presenter? = null
    private val TOKEN = "AIzaSyD94xpi7A-bpeNtLuz5gZLNjuJ74LmNtVA"
    val GOOGLE_SIGN_IN_REQUEST_CODE = 1
    lateinit var mGoogleApiClient: GoogleApiClient
    private var mGoogleSignInClient: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SignInPresenter(this)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(context!!)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
        mGoogleApiClient.connect()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in,container, false)
        val btSignIn = view.findViewById(R.id.btSignIn) as Button
        val btEmail = view.findViewById(R.id.btn_email) as Button
        btSignIn.setOnClickListener {
            getAccount()
        }
        btEmail.setOnClickListener {
            initGoogleSignIn()
            signInWithGoogleSignIn()
        }
        return view
    }
    override fun setPresenter(presenter: SignInContract.Presenter) {
        this.presenter = presenter
    }
    override fun showLoading(isShow: Boolean) {
        loadSignIn.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    override fun getAccount() {
        val email = edt_email?.text.toString()
        val password = edt_password?.text.toString()
        presenter?.checkAccount(email,password)
    }
    // logic success
    override fun getSuccess(){
        val intent = Intent(context, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
    private fun initGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(TOKEN)
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(context!!).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()
        mGoogleApiClient.connect()
        mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso)
    }

    private fun signInWithGoogleSignIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount
                presenter?.authWithGoogle(account!!)
            }
        }
    }
}