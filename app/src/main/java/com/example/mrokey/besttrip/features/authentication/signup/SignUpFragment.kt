package com.example.mrokey.besttrip.features.authentication.signup

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mrokey.besttrip.R
import android.widget.Toast
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment: Fragment(),SignUpContract.View {

    private var presenter: SignUpContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SignUpPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val btSignUp = view.findViewById(R.id.btSignUp) as Button
        btSignUp.setOnClickListener {
            getAccount()
        }
        return view
    }

    override fun getAccount() {
        val name: String = edt_username?.text.toString()
        val email: String = edt_email?.text.toString()
        val password: String = edt_password?.text.toString()
        presenter?.checkAccount(name, email, password)
    }

    override fun setPresenter(presenter: SignUpContract.Presenter) {
        this.presenter = presenter
    }

    override fun showLoading(isShow: Boolean) {
        loadSignUp.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun showNotification(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}