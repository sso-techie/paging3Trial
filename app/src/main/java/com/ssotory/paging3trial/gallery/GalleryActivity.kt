package com.ssotory.paging3trial.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssotory.paging3trial.R
import com.ssotory.paging3trial.gallery.adapter.GalleryAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GalleryActivity : AppCompatActivity() {

    private val galleryViewModel: GalleryViewModel by lazy {
        ViewModelProvider(
            this,
            GalleryViewModel.Factory(contentResolver)
        ).get(GalleryViewModel::class.java)
    }

    private val adapter = GalleryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        checkEssentialPermission()
    }

    private fun checkEssentialPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                initUi()
            } else {
                showNeedPermissionDialog()
            }
        }

        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                initUi()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showNeedPermissionDialog()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun showNeedPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.title_need_permission)
            .setMessage(R.string.message_need_permission)
            .setPositiveButton(android.R.string.ok) { _, _ -> finish() }
            .create()
            .show()
    }

    private fun initUi() {
        findViewById<RecyclerView>(R.id.recycler_view).let {
            it.adapter = adapter
            it.layoutManager = GridLayoutManager(this, 5)
        }

        lifecycleScope.launch {
            galleryViewModel.flow.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}