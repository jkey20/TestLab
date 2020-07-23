package org.first.mnkotlin

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel

class SelectPictureViewModel : ViewModel() {
    private var GET_GALLERY_IMAGE = 200;
    private var isMachineLearningComplete = false
    init {
        Log.i("GameViewModel", "GameViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }

    fun changeImageInGallery(data: Intent): Uri? {

        val selectedImageUri = data.data
        if (selectedImageUri == null) {
            return null
        } else {
            return selectedImageUri
        }
    }

    fun changeImageInCamera(data: Intent): Bitmap?{
        var bitmap = data.extras?.get("data")
        if(bitmap != null){
            return bitmap as Bitmap
        }
        else{
            return null
        }
    }
    fun changeMachineLearningResult() : Boolean{
        isMachineLearningComplete = true
        return isMachineLearningComplete
    }
}
