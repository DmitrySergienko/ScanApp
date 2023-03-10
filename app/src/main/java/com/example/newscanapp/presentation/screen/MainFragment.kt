package com.example.newscanapp.presentation.screen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.newscanapp.R
import com.example.newscanapp.data.HistoryDatabase
import com.example.newscanapp.data.TestDB
import com.example.newscanapp.databinding.FragmentMainBinding
import com.example.newscanapp.presentation.utils.BaseFragment
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val MY_TAG = "VVV"
const val NAME_NOT_SCAN = "Name not detected"
const val ID_NOT_SCAN = "id not detected"

class MainFragment :
    BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val MY_PERMISSIONS_REQUEST_CAMERA: Int = 101
    private lateinit var mCameraSource: CameraSource
    private lateinit var textRecognizer: TextRecognizer
    private var client_name = ""
    private var id_number = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToLocalStorage()

        requestForPermission()

        //  Create text Recognizer
        textRecognizer = TextRecognizer.Builder(requireContext()).build()
        if (!textRecognizer.isOperational) {
            Toast.makeText(
                requireContext(),
                "Dependencies are not loaded yet...please try after few moment!!",
                Toast.LENGTH_SHORT
            )
                .show()
            Log.e(MY_TAG, "Dependencies are downloading....try after few moment")
            return
        }

        //  Init camera source to use high resolution and auto focus
        mCameraSource = CameraSource.Builder(requireContext(), textRecognizer)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(1280, 1024)
            .setAutoFocusEnabled(true)
            .setRequestedFps(2.0f)
            .build()

        binding.surfaceCameraPreview.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceDestroyed(p0: SurfaceHolder) {
                mCameraSource.stop()
            }

            @SuppressLint("MissingPermission")
            override fun surfaceCreated(p0: SurfaceHolder) {
                try {
                    if (isCameraPermissionGranted()) {
                        mCameraSource.start(binding.surfaceCameraPreview.holder)
                    } else {
                        requestForPermission()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()

                }
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

            }
        })

        textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
                val items = detections.detectedItems

                if (items.size() <= 0) {
                    return
                }

                binding.tvResult.post {
                    val stringBuilder = StringBuilder()
                    for (i in 0 until items.size()) {
                        val item = items.valueAt(i)
                        stringBuilder.append(item.value)
                        stringBuilder.append("\n")
                    }
                    if(binding.tvResult.text != null){
                        binding.tvResult.text = stringBuilder.toString()
                    }else
                    {return@post}

                }
            }
        })
        cleanRoom()

        //button click
        binding.btCalculate.setOnClickListener {
            val result = binding.tvResult.text

            Log.d(MY_TAG, "result:  ${getID(result)}")
            val tc_id = binding.tvId
            val tc_name = binding.tvName
            tc_id.text = getID(result)
            tc_name.text = getName(result)

            scan(tc_id, tc_name)
        }
    }

    private fun attachItemToRoom(name:String,id:String) {
            val db =
                Room.databaseBuilder(requireContext(), HistoryDatabase::class.java, "new_db2")
                    .build()
            val dao = db.historyDao()
            val newItem = TestDB(0, name, id)

            val scope = CoroutineScope(Dispatchers.IO)

            scope.launch {
                dao.insertItem(newItem)
            }
    }

    private fun navigateToLocalStorage() {

        binding.storageButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToHistory(client_name,id_number)
            findNavController()
                .navigate(action)
        }
    }

    private fun getID(scan: CharSequence): String {
        var id = ID_NOT_SCAN
        val pattern = Regex("\\d{3}-\\d{4}-\\d{7}-\\d{1}")
        val matches = pattern.findAll(scan)
        for (match in matches) {
            id = match.value
        }
        return id
    }

    private fun getName(scan: CharSequence): String {
        var result = NAME_NOT_SCAN
        val input = scan
        val regex = Regex("Name: (.*)")
        val matchResult = regex.find(input)
        val name = matchResult?.groupValues?.getOrNull(1)
        if (name != null) {
            result = name
        }
        return result
    }

    private fun scan(viewID: TextView, viewName: TextView) {
        if ((viewID.text == "id not detected") || (viewName.text == "Name not detected")) {
            return
        } else {
            //alert dialog confirm scan result
            val addInfoDialog = AlertDialog.Builder(requireContext())
                .setMessage("Name: ${viewName.text.toString()}\nID: ${viewID.text.toString()}")
                .setPositiveButton(R.string.accept) { _, _ ->

                    //save scan result in bd
                    client_name = viewName.text.toString()
                    id_number =viewID.text.toString()
                    attachItemToRoom(client_name,id_number)
                }
                .setNegativeButton(R.string.scan_again) { _, _ ->
                    //stay on same fragment
                }.create()

            addInfoDialog.show()
        }
    }

    private fun cleanRoom(){
        binding.cleanButton.setOnClickListener {
            val db = Room.databaseBuilder(requireContext(), HistoryDatabase::class.java, "new_db2")
                .build()
            val dao = db.historyDao()
            val scope = CoroutineScope(Dispatchers.IO)

            scope.launch {
                dao.deleteAll()
            }
        }
    }

    private fun requestForPermission() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireContext() as Activity,
                    Manifest.permission.CAMERA
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    requireContext() as Activity,
                    arrayOf(
                        Manifest.permission.CAMERA
                    ),
                    MY_PERMISSIONS_REQUEST_CAMERA
                )

                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    private fun isCameraPermissionGranted(): Boolean {

        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    }

    //for handling permissions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    requestForPermission()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

}