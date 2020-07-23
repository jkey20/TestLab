package org.first.mnkotlin

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kakao.auth.Session
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import org.first.mnkotlin.databinding.FragmentSelectPictureBinding

/**
 * A simple [Fragment] subclass.
 */
class SelectPictureFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var facebookLoginCheck = arguments?.getBoolean("isLoggedInWithFacebook")
    private lateinit var viewmodel: SelectPictureViewModel
    private var GET_GALLERY_IMAGE = 312;
    private var GET_CAMERA_IMAGE = 313;
    private lateinit var binding: FragmentSelectPictureBinding
    private var isMachineLearningComplete = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()
        // 권한요청
        if (!(ContextCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1
            )
        }

        // 바인딩
        binding = DataBindingUtil.inflate<FragmentSelectPictureBinding>(
            inflater,
            R.layout.fragment_select_picture,
            container,
            false
        )
        // 뷰 모델
        Log.i("GameFragment", "Called ViewModelProviders.of")
        viewmodel = ViewModelProviders.of(this).get(SelectPictureViewModel::class.java)

        // 로그인
        binding.logincheckButton.setOnClickListener { view: View ->
            var currentUser = firebaseAuth.currentUser
            if (facebookLoginCheck != null) {
                facebookSignOut()
            } else if (Session.getCurrentSession().isOpened) {
                kakaoSignOut()
            } else if (currentUser != null) {
                googleSignOut()
            }
        }

        // 갤러리
        binding.galleryButton.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, GET_GALLERY_IMAGE)
        }

        binding.cameraButton.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, GET_CAMERA_IMAGE)
        }
        return binding.root
    }

    fun facebookSignOut() {
        LoginManager.getInstance().logOut()
        findNavController()
            .navigate(R.id.action_selectPictureFragment_to_loginFragment2)
    }

    fun kakaoSignOut() {
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                findNavController()
                    .navigate(R.id.action_selectPictureFragment_to_loginFragment2)
                Log.e("TEST!", "onCompleteLogout")
            }
        })
    }

    fun googleSignOut() {

        FirebaseAuth.getInstance().signOut()

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity!!, gso)
        googleSignInClient.signOut().addOnCompleteListener {
            findNavController().navigate(R.id.action_selectPictureFragment_to_loginFragment2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null) {
            binding.galleryImageview.setImageURI(viewmodel.changeImageInGallery(data))
            runMachineLearning()
        } else if (requestCode == GET_CAMERA_IMAGE && resultCode == RESULT_OK && data != null) {
            binding.galleryImageview.setImageBitmap(viewmodel.changeImageInCamera(data))
            runMachineLearning()
        }
    }

    fun runMachineLearning() {
        if (isMachineLearningComplete) {
            // 다음 fragment로 이동
            var intent = Intent(activity, GlideViewPagerActivity::class.java)
            startActivity(intent)
        } else {
            showRetryDialog()
        }
    }

    fun showRetryDialog() {
        var alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle("머신러닝 결과")
        alertDialogBuilder.setMessage("음식 인식에 실패했습니다.\n다시 시도하시겠습니까?")
            .setCancelable(false)
            .setPositiveButton("예", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    isMachineLearningComplete = viewmodel.changeMachineLearningResult()
                    runMachineLearning()
                }

            })
            .setNegativeButton("아니오", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog!!.cancel()
                }
            })
        var alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
