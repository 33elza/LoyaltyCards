package com.example.el.loyaltycards.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.EXTRA_OUTPUT
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.el.loyaltycards.R
import com.example.el.loyaltycards.presentation.presenter.CardDetailsPresenter
import com.example.el.loyaltycards.presentation.view.CardDetailsView
import com.example.el.loyaltycards.ui.activity.BarcodeCaptureActivity
import kotlinx.android.synthetic.main.fragment_card_details.*
import kotlinx.android.synthetic.main.fragment_card_details.view.*
import java.io.File


class CardDetailsFragment : MvpAppCompatFragment(), CardDetailsView {

    @InjectPresenter(type = PresenterType.WEAK)
    lateinit var presenter: CardDetailsPresenter

    val REQUEST_IMAGE_FRONT = 1
    val REQUEST_IMAGE_BACK = 0
    val BARCODE_CAPTURE = 2

    val PERMISSION_REQUEST_CODE = 0

    private var imageUri: Uri? = null

    companion object {
        @JvmStatic
        fun newInstance() = CardDetailsFragment()
    }

    // LifeCycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CODE)
        }

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE)
        }

        val rootView = inflater.inflate(R.layout.fragment_card_details, container, false)

        rootView.cardNameEditText.addTextChangedListener(presenter.getCardNameTextWatcher())
        rootView.cardCodeEditText.addTextChangedListener(presenter.getCardCodeTextWatcher())
        rootView.cardNoteEditText.addTextChangedListener(presenter.getCardNoteTextWatcher())
        rootView.addFrontPhotoIcon.setOnClickListener { presenter.onAddPhotoClick(REQUEST_IMAGE_FRONT) }
        rootView.addFrontPhotoIcon.background.alpha = 200
        rootView.addBackPhotoIcon.setOnClickListener { presenter.onAddPhotoClick(REQUEST_IMAGE_BACK) }
        rootView.addBackPhotoIcon.background.alpha = 200
        rootView.saveCardButton.setOnClickListener { presenter.onSaveCardButtonClick() }
        rootView.readBarcodeButton.setOnClickListener { presenter.onBarcodeReadButtonClick() }
        rootView.cardFrontPhoto.setOnClickListener { presenter.onFrontPhotoImageViewClick() }
        rootView.cardBackPhoto.setOnClickListener { presenter.onBackPhotoImageViewClick() }

        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (imageUri != null) {
            outState.putString("imageUri", imageUri.toString())
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null && savedInstanceState.containsKey("imageUri")) {
            imageUri = Uri.parse(savedInstanceState.getString("imageUri"))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_IMAGE_FRONT -> {
                    if (imageUri != null) {
                        Glide.with(context!!).asBitmap().load(imageUri).into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                presenter.setFrontPhotoUri(imageUri!!, resource)
                            }
                        })
                        Glide.with(context!!).load(imageUri).into(cardFrontPhoto)
                        frontPhotoTextView.visibility = View.INVISIBLE
                    }
                }
                REQUEST_IMAGE_BACK -> {
                    if (imageUri != null) {
                        Glide.with(context!!).load(imageUri).into(cardBackPhoto)
                        backPhotoTextView.visibility = View.INVISIBLE
                        presenter.setBackPhotoUri(imageUri!!)
                    }
                }
                BARCODE_CAPTURE -> {
                    val barcode = data.extras.get("barcode")
                    cardCodeEditText.setText(barcode.toString())
                }
            }
        }
    }

    // CardDetailsView

    override fun setName(name: String) {
        cardNameEditText.setText(name)
    }

    override fun setCode(code: String) {
        cardCodeEditText.setText(code)
    }

    override fun setFrontPhoto(frontPhoto: Uri?) {
        if (frontPhoto != null) {
            Glide.with(context!!).load(frontPhoto).into(cardFrontPhoto)
            frontPhotoTextView.visibility = View.INVISIBLE
        } else {
            frontPhotoTextView.visibility = View.VISIBLE
        }
    }

    override fun setBackPhoto(backPhoto: Uri?) {
        if (backPhoto != null) {
            Glide.with(context!!).load(backPhoto).into(cardBackPhoto)
            backPhotoTextView.visibility = View.INVISIBLE
        } else {
            backPhotoTextView.visibility = View.VISIBLE
        }
    }

    override fun setNote(note: String) {
        cardNoteEditText.setText(note)
    }

    override fun openImageCapture(requestImage: Int) {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePicture.resolveActivity(activity!!.packageManager) != null) {

            val image = createImageFile()
            imageUri = FileProvider.getUriForFile(this.context!!, "com.example.el.loyaltycards.fileprovider", image)

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                takePicture.setClipData(ClipData.newUri(activity!!.contentResolver, "imageUri", imageUri))
                takePicture.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            takePicture.putExtra(EXTRA_OUTPUT, imageUri)

            startActivityForResult(takePicture, requestImage)
        }
    }

    private fun createImageFile(): File {
        val timeStamp = System.currentTimeMillis()
        val imageFileName = "JPEG_" + timeStamp + "_.jpg"
        val storageDir: File = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File(storageDir, imageFileName)

        return image
    }

    override fun openBarcodeCaptureActivity() {
        val intent = Intent(context, BarcodeCaptureActivity::class.java)
        startActivityForResult(intent, BARCODE_CAPTURE)
    }

    override fun showError(error: Int) {
        Toast.makeText(context, getString(error), Toast.LENGTH_LONG).show()
    }
}