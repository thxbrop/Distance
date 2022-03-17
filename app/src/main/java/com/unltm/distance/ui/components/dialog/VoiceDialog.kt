package com.unltm.distance.components.dialog

import android.content.DialogInterface
import android.media.MediaRecorder
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateFormat
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unltm.distance.R
import com.unltm.distance.base.contracts.sound
import com.unltm.distance.base.file.FileType
import com.unltm.distance.databinding.DialogRecordBinding
import com.unltm.distance.ui.components.ceil.button.ShutterButton
import java.io.File
import java.util.*

class VoiceDialog : BottomSheetDialogFragment(), ShutterButton.ShutterButtonDelegate {
    private var filePath: String = ""
    private lateinit var binding: DialogRecordBinding
    private var mediaRecorder: MediaRecorder? = null

    private lateinit var callback: (String) -> Unit

    //private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheet)
        isCancelable = false

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogRecordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.delegate = this
    }


    fun show(manager: FragmentManager, tag: String?, callback: (String) -> Unit) {
        super.show(manager, tag)
        this.callback = callback
    }

    override fun onCancel(dialog: DialogInterface) {
        save()
        super.onCancel(dialog)
    }

    override fun dismiss() {
        save()
        super.dismiss()
    }

    private fun save() {
        context?.sound(R.raw.voicechat_leave, 16)
        try {
            mediaRecorder?.let {
                it.stop()
                it.release()
                callback.invoke(filePath)
            }
        } catch (_: Exception) {
        }
    }

    override fun shutterLongPressed(): Boolean {
        context?.sound(R.raw.voicechat_join, 16)
        binding.button.setState(ShutterButton.State.RECORDING, true)
        binding.button.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        mediaRecorder = MediaRecorder().apply {
            try {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                val fileName = "${
                    DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance())
                }.m4a"
                val parent =
                    this@VoiceDialog.context?.getExternalFilesDir(FileType.Music.environment)?.absolutePath + '/'
                File(parent).mkdirs()
                filePath = parent + fileName
                setOutputFile(filePath)
                prepare()
                start()
                binding.chronometer.start()
            } catch (e: Exception) {
                Log.e("TAG", "onViewCreated: ", e)
            }
        }

        return true
    }

    override fun shutterReleased() {
        binding.chronometer.stop()
        binding.button.setState(ShutterButton.State.DEFAULT, true)
        dismiss()
    }

    override fun shutterCancel() {
        //job?.cancel()
    }

    override fun onTranslationChanged(x: Float, y: Float): Boolean {
        return true
    }
}