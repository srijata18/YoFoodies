package com.example.registrationmodule.ui.registration

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.custompreferences.Constants
import com.example.custompreferences.views.afterTextChanged
import com.example.custompreferences.views.visible
import com.example.registrationmodule.R
import com.example.registrationmodule.data.RegistrationFormState
import com.example.registrationmodule.data.RegistrationResult
import com.example.registrationmodule.data.RegistrationUserView
import com.example.registrationmodule.model.RegistrationViewModel
import com.example.registrationmodule.model.RegistrationViewModelFactory
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class RegistrationActivity : AppCompatActivity() {

    private lateinit var registrationViewModel: RegistrationViewModel
    private var imageString: String? = null

    private var userName: EditText? = null
    private var userEmail: EditText? = null
    private var password: EditText? = null
    private var userPhoneNumber: EditText? = null
    private var addImg: ImageView? = null
    private var loading: ProgressBar? = null
    private var register: AppCompatButton? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        userName = findViewById(R.id.et_name)
        userEmail = findViewById(R.id.et_email)
        password = findViewById(R.id.et_password)
        userPhoneNumber = findViewById(R.id.et_phone)
        addImg = findViewById(R.id.iv_add_img)
        register = findViewById(R.id.btn_register)
        loading = findViewById(R.id.pb_loading_register)

        registrationViewModel =
            ViewModelProviders.of(this@RegistrationActivity, RegistrationViewModelFactory())
                .get(RegistrationViewModel::class.java)

        registrationViewModel.registrationFormState.observe(
            this,
            Observer {
                val registrationState = it ?: return@Observer
                updateWithRegistrtionState(registrationState)
            })

        registrationViewModel.registrationResult.observe(
            this@RegistrationActivity,
            Observer {
                val registrationResult = it ?: return@Observer
                updateWithRegistrationResult(registrationResult)
            })

        handleClickListeners()
    }

    private val CAMERA_REQUEST = 1888
    private val MY_CAMERA_PERMISSION_CODE = 100

    @RequiresApi(Build.VERSION_CODES.M)
    private fun selectImg() {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@RegistrationActivity)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                if (checkSelfPermission(Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA),
                        MY_CAMERA_PERMISSION_CODE
                    )
                } else {
                    val cameraIntent =
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA_REQUEST)
                }

            } else if (options[item] == "Choose from Gallery") {
                val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(intent, 2)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                Toast.makeText(this, "Please allow camera permission ", Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode === CAMERA_REQUEST && resultCode === Activity.RESULT_OK) {
                val photo = data!!.extras!!["data"] as Bitmap?
                loadImage(photo)
            } else if (requestCode == 2) {
                val selectedImage = data?.data
                val uri = selectedImage?.getPath()
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val c: Cursor? = selectedImage?.let {
                    contentResolver.query(it, filePath, null, null, null)
                }
                c?.moveToFirst()
                val columnIndex: Int? = c?.getColumnIndex(filePath[0])
                val picturePath: String? = columnIndex?.let { c.getString(it) }
                c?.close()
                val bitmap = BitmapFactory.decodeFile(picturePath)
                    ?: MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                Log.i(
                    "===imgPath===",
                    picturePath + bitmap.toString()
                )
//                iv_profile_img?.setImageURI(selectedImage)
                loadImage(bitmap)
            }
        }
    }

    private fun loadImage(bitmap: Bitmap?) {
        Glide.with(this)
            .load(bitmap)
            .error(R.drawable.ic_person)
            .circleCrop()
            .into(iv_profile_img)
        iv_profile_img?.background = null
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    private fun updateUiWithUser(model: RegistrationUserView) {
        Toast.makeText(
            applicationContext,
            "$model registration is Successful",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showRegistrationFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    fun updateWithRegistrtionState(registrationState: RegistrationFormState) {
        register?.apply {
            isClickable = registrationState.isDataValid
            isEnabled = registrationState.isDataValid
        }
        with(registrationState) {
            usernameError?.let { userName?.error = getString(it) }
            useremailError?.let { userEmail?.error = getString(it) }
            userphnoError?.let { userPhoneNumber?.error = getString(it) }
            passwordError?.let { password?.error = getString(it) }
        }
    }

    fun updateWithRegistrationResult(registrationResult: RegistrationResult) {
        loading?.visibility = View.GONE
        val userName =
            when {
                registrationResult.error != null -> {
                    showRegistrationFailed(registrationResult.error)
                    ""
                }
                registrationResult.success != null -> {
                    updateUiWithUser(registrationResult.success)
                    userName?.text?.toString()
                }
                else -> ""
            }
        val intent = Intent()
        intent.putExtra(Constants.USERDETAILSKEY, userName)
        setResult(Activity.RESULT_OK, intent)
        //Complete and destroy login activity once successful
        finish()
    }

    fun handleClickListeners() {
        userName?.afterTextChanged {
            checkWhenDataChanged()
        }
        userEmail?.afterTextChanged {
            checkWhenDataChanged()
        }
        userPhoneNumber?.afterTextChanged {
            checkWhenDataChanged()
        }

        password?.apply {
            afterTextChanged {
                checkWhenDataChanged()
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        performRegistration()
                }
                false
            }
        }

        register?.setOnClickListener {
            performRegistration()
        }

        addImg?.setOnClickListener {
            selectImg()
        }
    }

    fun checkWhenDataChanged() {
        registrationViewModel.registrationDataChanged(
            userName?.text.toString(),
            userEmail?.text.toString(),
            userPhoneNumber?.text.toString(),
            password?.text.toString(),
        )
    }

    fun performRegistration() {
        GlobalScope.launch(Dispatchers.Main) {
            loading?.visible()
            registrationViewModel.register(
                userName?.text.toString(),
                userEmail?.text.toString(),
                userPhoneNumber?.text.toString(),
                password?.text.toString(),
                imageString
            )
        }
    }
}