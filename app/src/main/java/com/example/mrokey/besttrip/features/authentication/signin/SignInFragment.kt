package com.example.mrokey.besttrip.features.authentication.signin

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.mrokey.besttrip.R
import com.example.mrokey.besttrip.home.HomeActivity
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.fragment_sign_in.*
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import java.util.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.firebase.auth.FirebaseAuth

class SignInFragment: Fragment(), SignInContract.View, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private var presenter: SignInContract.Presenter? = null
    private val GOOGLE_SIGN_IN_REQUEST_CODE = 1
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mFirebaseAuth: FirebaseAuth? = null

    //Facebook
    private lateinit var mCallbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Facebook
        mCallbackManager = CallbackManager.Factory.create()
        presenter = SignInPresenter(this, mCallbackManager)
        presenter?.currentAccount()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in,container, false)
        val btSignIn = view.findViewById(R.id.btSignIn) as Button
        val btEmail = view.findViewById(R.id.btn_email) as Button
        val btFacebook = view.findViewById(R.id.btn_facebook) as Button
        initGoogleSignIn()
        btSignIn.setOnClickListener {
            getAccount()
        }
        btEmail.setOnClickListener {
            signInWithGoogleSignIn()
        }
        btFacebook.setOnClickListener {
            presenter?.authWithFacebook()
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
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
    //sign in with email and password
    override fun getAccount() {
        val email = edt_email?.text.toString()
        val password = edt_password?.text.toString()
        presenter?.checkAccount(email, password)
    }
    // logic success
    override fun getSuccess(){
        val intent = Intent(context, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
    //Sign in with google
    private fun initGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this@SignInFragment.requireContext())
                .addConnectionCallbacks(this)
                .enableAutoManage(this@SignInFragment.requireActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
        mFirebaseAuth = FirebaseAuth.getInstance()
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
                if (account != null) {
                    presenter?.authWithGoogle(account)
                }
            }
        }
        //Facebook
        else mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }
    override fun onSuccessLoginFacebook(result: LoginResult?) {
        Toast.makeText(context, "Login Success", Toast.LENGTH_LONG).show()
        startActivity(Intent(activity, HomeActivity::class.java))
    }
    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(context, "Google Play Services error.", Toast.LENGTH_SHORT).show()
    }
    override fun onConnected(p0: Bundle?) {
        Log.d("ddd","connected")
        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
    }
    override fun onConnectionSuspended(p0: Int) {
    }
}