package org.first.mnkotlin

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import org.first.mnkotlin.databinding.FragmentLoginBinding
import org.json.JSONObject
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {
    private lateinit var accessToken: AccessToken
    private lateinit var result: FacebookCallback<LoginResult>
    private var isLoggedInWithFacebook: Boolean = false
    private lateinit var userEmail: String
    private lateinit var userName: String
    private lateinit var jobj1: JSONObject
    private lateinit var jobj2: JSONObject
    private lateinit var userPicture: String
    private var callbackManager: CallbackManager? = null
    private lateinit var binding: FragmentLoginBinding
    private var callback: SessionCallback = SessionCallback()
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private val RC_SIGN_IN = 20
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        loginSetting()

        binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        binding.facebookloginbutton.setPermissions("email", "public_profile")
        binding.facebookloginbutton.setFragment(this)
        binding.facebookloginbutton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.e("TAG", "facebook:onSuccess:")

                    accessToken = loginResult.accessToken
                    isLoggedInWithFacebook = accessToken != null && !accessToken.isExpired
                    handleFacebookAccessToken(accessToken)
                    requestMe(accessToken)
                }

                override fun onCancel() {

                    Log.e("TAG", "facebook:onCancel:")
                }

                override fun onError(error: FacebookException?) {

                    Log.e("TAG", "facebook:onError:")
                }

            })
        binding.googleloginbutton.setOnClickListener {
            val signInClient = googleSignInClient.signInIntent
            startActivityForResult(signInClient, RC_SIGN_IN)
        }

        return binding.root
    }

    fun loginSetting(){
        auth = FirebaseAuth.getInstance()
        Session.getCurrentSession().addCallback(callback)
        // 카카오톡 로그인 되어있는지 체크
        Session.getCurrentSession().checkAndImplicitOpen()
        callbackManager = CallbackManager.Factory.create()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity!!, gso)
    }

    fun requestMe(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(accessToken) { `object`, response ->
            try {
                //here is the data that you want

                userEmail = `object`.getString("email")
                Log.e("TAGG", userEmail)
                userName = `object`.getString("name")
                Log.e("TAGG", userName)
                jobj1 = `object`.optJSONObject("picture")
                Log.e("TAGG", jobj1.toString())
                jobj2 = jobj1.optJSONObject("data")
                Log.e("TAGG", jobj2.toString())
                userPicture = jobj2.getString("url")
                Log.e("TAGG", userPicture)


            } catch (e: Exception) {
                e.printStackTrace()
            }

            goToSelectFragment()
        }

        val parameters = Bundle()
        parameters.putString("fields", "name,email,picture")
        request.parameters = parameters
        request.executeAsync()

    }


    fun goToSelectFragment() {
        var bundle = bundleOf("isLoggedInWithFacebook" to isLoggedInWithFacebook)
        findNavController().navigate(R.id.action_loginFragment2_to_selectPictureFragment, bundle)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            }catch (e: ApiException){
                Log.e("TT", "", e)
            }
        }
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(activity)
        if (account != null) {
            findNavController().navigate(R.id.action_loginFragment2_to_selectPictureFragment)
        }
        if (AccessToken.getCurrentAccessToken() != null) {
            accessToken = AccessToken.getCurrentAccessToken()
            isLoggedInWithFacebook = accessToken != null && !accessToken.isExpired
        }
        if (isLoggedInWithFacebook) {
            requestMe(accessToken)
        } else {

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
        Log.e("TEST1", "onDestroy")
    }

    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpenFailed(exception: KakaoException?) {
            Log.e("TEST2", "onSessionOpenFailed")
            exception?.printStackTrace()
        }

        override fun onSessionOpened() {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {

                override fun onFailure(errorResult: ErrorResult?) {
                    Log.e("TEST3", "onFailure")
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Log.e("TEST4", "onSessionClosed")
                }

                override fun onSuccess(result: MeV2Response?) {
                    findNavController().navigate(R.id.action_loginFragment2_to_selectPictureFragment)
                    Log.e("TEST5", "onSuccess")
                }

            })
        }

    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)

                }

                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!){ task ->
                if(task.isSuccessful){
                    findNavController().navigate(R.id.action_loginFragment2_to_selectPictureFragment)
                }
                else{
                    Toast.makeText(context,"Firebase 오류 발생",Toast.LENGTH_LONG)
                }
            }
    }

}


