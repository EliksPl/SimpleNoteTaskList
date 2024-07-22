package com.simpleNotes.simplenotelist.screens.settingsFragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.simpleNotes.simplenotelist.MAIN
import com.simpleNotes.simplenotelist.R
import com.simpleNotes.simplenotelist.model.dbModel.NoteModel
import com.simpleNotes.simplenotelist.model.dbModel.TaskModel
import com.simpleNotes.simplenotelist.permissions.ProtectedAppsController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import kotlin.collections.ArrayList


class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var viewModel : SettingsViewModel

    private var listPreference: Preference? = null
    private var permissionPreference: Preference? = null
    private var backupPreference: Preference? = null
    private var getBackupPreference: Preference? = null
    val DIVIDER = "*%/div/%*"

    private val resultRead = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val uri = result.data?.data!!
                var stringBuilder = ""
                requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        var line: String? = reader.readLine()
                        var i =0
                        while (line != null) {
                            stringBuilder += line
                            println(line)
                            if (i == 0){
                                val arrayListType = object : TypeToken<ArrayList<NoteModel>>() {}.type
                                println(line.split(DIVIDER)[i])
                                val noteList: ArrayList<NoteModel> = Gson().fromJson(
                                    line.split(DIVIDER)[i],
                                    arrayListType
                                )

                                viewModel.addNotes(noteList)
                            }
                            ++i
                            if (i == 1){
                                val arrayListType = object : TypeToken<ArrayList<TaskModel>>() {}.type
                                println(line.split(DIVIDER)[i])
                                val taskList: ArrayList<TaskModel> = Gson().fromJson(
                                    line.split(DIVIDER)[i],
                                    arrayListType
                                )

                                viewModel.addTasks(taskList)
                            }


                            line = reader.readLine()
                        }

//                        val lineNotes =stringBuilder.split(DIVIDER).toTypedArray()[0]
//                        val lineTasks = stringBuilder.split(DIVIDER).toTypedArray()[1]
//
//                        Toast.makeText(requireContext(),lineNotes, Toast.LENGTH_LONG).show()
//                        Toast.makeText(requireContext(),lineTasks, Toast.LENGTH_LONG).show()











//                        val array = stringBuilder.split(DIVIDER) as ArrayList

//                        if (true) {
////                        Toast.makeText(requireContext(),array[0].toString(),Toast.LENGTH_LONG).show()
//                            val noteList = Gson().fromJson(
//                                stringBuilder.split(DIVIDER).toTypedArray()[0],
//                                ArrayList::class.java
//                            ) as ArrayList<NoteModel>
//
//
//                            viewModel.addNotes(noteList)
//
////                        Toast.makeText(requireContext(),array[1],Toast.LENGTH_LONG).show()
//                            val taskList = Gson().fromJson(
//                                stringBuilder.split(DIVIDER).toTypedArray()[1],
//                                ArrayList::class.java
//                            ) as ArrayList<TaskModel>
//
//
//                            viewModel.addTasks(taskList)
//                        }
                    }
                }

//            try {
//                result.data?.data?.let {
//                    requireContext().contentResolver.openInputStream(it)
//                }?.let {
//                    val r = BufferedReader(InputStreamReader(it))
//                    var i = 0
//                    while (true) {
//                        val line: String = r.readLine() ?: break
//                        if (i == 0){
//                            val backList  = Gson().fromJson(        // Back to another variable
//                                line, Array::class.java).toList() as ArrayList<NoteModel>
//                        }
//                        if (i == 1){
//                            val backList  = Gson().fromJson(        // Back to another variable
//                                line, Array::class.java).toList() as ArrayList<TaskModel>
//                        }
//
//                        Toast.makeText(requireContext(),line, Toast.LENGTH_LONG).show()
//                        ++i
//                    }
//                }
//
//            } catch (e: Exception) { // If the app failed to attempt to retrieve the error file, throw an error alert
//                Toast.makeText(
//                    requireContext(),
//                    "Sorry, but there was an error reading in the file",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            Toast.makeText(context, result.data?.data?.path, Toast.LENGTH_LONG).show()

        }
        else{
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }

    }

    private val resultWrite = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes

            lifecycleScope.launch(Dispatchers.Default){
                val noteList = viewModel.getAllNotes() as ArrayList<NoteModel>
                val taskList = viewModel.getAllTasks() as ArrayList<TaskModel>


                val noteString = Gson().toJson(noteList)
                val taskString = Gson().toJson(taskList)
                val completeString = noteString + DIVIDER + taskString



                try {
                    val uri = result.data?.data!!
                    requireContext().contentResolver.openFileDescriptor(uri, "w")?.use { it ->
                        FileOutputStream(it.fileDescriptor).use {
                            it.write(completeString.toByteArray())
                        }
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        }

    }


    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?) {

        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initToolbar()
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initToolbar() {
//        MAIN.setActionBarTitle(R.string.settingsFragment_title)
    }

    private fun init() {
        viewModel = ViewModelProvider(this).get<SettingsViewModel>()
        listPreference = preferenceManager.findPreference("wipe")
        listPreference?.setOnPreferenceClickListener {
            viewModel.deleteAllNotes {  }
            true
        }
        permissionPreference = preferenceManager.findPreference("autoStart")
        permissionPreference?.setOnPreferenceClickListener {
            val protectedAppsController = ProtectedAppsController()
            protectedAppsController.startPowerSaverIntent(MAIN)
            true
        }

        //createBackup
        backupPreference = preferenceManager.findPreference("createBackup")
        backupPreference?.setOnPreferenceClickListener {

            // Додавання даних до списків
            saveDataToFile()
            true
        }

        getBackupPreference = preferenceManager.findPreference("getBackup")
        getBackupPreference?.setOnPreferenceClickListener {
            loadDataFromFile()

            true
        }
    }

    private fun loadDataFromFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, MediaStore.Downloads.EXTERNAL_CONTENT_URI)
            }

            resultRead.launch(intent)
        }else{
            openFilePicker()
        }

    }

    private val REQUEST_CODE_OPEN_FILE = 124

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(intent, REQUEST_CODE_OPEN_FILE)
    }

    private fun readTextFromFile(uri: Uri) {
        var stringBuilder = ""
        requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                var i =0
                while (line != null) {
                    stringBuilder += line
                    println(line)
                    if (i == 0){
                        val arrayListType = object : TypeToken<ArrayList<NoteModel>>() {}.type
                        println(line.split(DIVIDER)[i])
                        val noteList: ArrayList<NoteModel> = Gson().fromJson(
                            line.split(DIVIDER)[i],
                            arrayListType
                        )

                        viewModel.addNotes(noteList)
                    }
                    ++i
                    if (i == 1){
                        val arrayListType = object : TypeToken<ArrayList<TaskModel>>() {}.type
                        println(line.split(DIVIDER)[i])
                        val taskList: ArrayList<TaskModel> = Gson().fromJson(
                            line.split(DIVIDER)[i],
                            arrayListType
                        )

                        viewModel.addTasks(taskList)
                    }


                    line = reader.readLine()
                }

            }
        }
        // Тепер ви можете використовувати fileContent з отриманим текстом з файлу
    }

//    private fun saveDataToFile() {
//        //TODO saveFile
//
//
//
//
//        saveFile()
////        Log.d("file", "saveDataToFile: "+path.absolutePath)
//
////        val sd_main = File(requireContext().getExternalFilesDir(null), "noteListBackup")
////        sd_main.mkdirs()
////        var success = true
////        if (!sd_main.exists())
////            success = sd_main.mkdir()
////
////        if (success) {
////            val sd = File(sd_main,"filename.txt")
////
////            if (!sd.exists()) {
////                success = sd.mkdir()
////                Log.d("failedFile", "wrong2")
////            }
////
////            if (success) {
////                // directory exists or already created
////                val dest = File(sd, "data")
////                try {
////                    // response is the data written to file
////                    PrintWriter(dest).use { out -> out.println("response") }
////                } catch (e: Exception) {
////                    Log.d("failedFile", "wrong1")
////                    // handle the exception
////                }
////            }
////        } else {
////            Log.d("failedFile", "wrong")
////            // directory creation is not successful
////        }
//    }

    private fun saveDataToFile() {

        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.Q) {
            val saveIntent = Intent(Intent.ACTION_CREATE_DOCUMENT, MediaStore.Downloads.EXTERNAL_CONTENT_URI).apply{
                setType("application/txt")
                putExtra(Intent.EXTRA_TITLE, "backup.txt")
            }

            resultWrite.launch(saveIntent)

//            val contentResolver = context!!.contentResolver
//
//            var values = ContentValues()
//            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "backup.txt")
//            values.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
//            values.put(MediaStore.MediaColumns.IS_PENDING, 1)
//
//            val mediaUri = contentResolver.insert(
//                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
//                values
//            )
//
//            contentResolver.openOutputStream(mediaUri!!).use { out ->
//                // Write your data here
//                out!!.write("test".toByteArray())
//            }
//
//            values = ContentValues()
//            values.put(MediaStore.MediaColumns.IS_PENDING, 0)
//
//            contentResolver.update(mediaUri!!, values, null, null)
//            result.launch(saveIntent)
//            context.startActivity(saveIntent).runCatching {


//            val storageManager = MAIN.getSystemService(Context.STORAGE_SERVICE) as StorageManager
//            var storageVolumeList = storageManager.storageVolumes
//            val storageVolume = storageVolumeList.get(0)
//            File(storageVolume.directory?.absolutePath + "/backupNotes.txt")
        } else {
//            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "backupNotes.txt")
//            val toast = Toast.makeText(MAIN,file.absolutePath, Toast.LENGTH_LONG)
//            toast.show()
            openDirectoryPicker()
        }
    }

    private val REQUEST_CODE_PICK_DIRECTORY = 123

    private fun openDirectoryPicker() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toUri()).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/txt"
            putExtra(Intent.EXTRA_TITLE, "backup.txt")
        }//(Intent.ACTION_CREATE_DOCUMENT, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
        startActivityForResult(intent, REQUEST_CODE_PICK_DIRECTORY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_DIRECTORY && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                lifecycleScope.launch(Dispatchers.IO){
                    val noteList = viewModel.getAllNotes() as ArrayList<NoteModel>
                    val taskList = viewModel.getAllTasks() as ArrayList<TaskModel>


                    val noteString = Gson().toJson(noteList)
                    val taskString = Gson().toJson(taskList)
                    val completeString = noteString + DIVIDER + taskString



                    try {
                        requireContext().contentResolver.openFileDescriptor(uri, "w")?.use { it ->
                            FileOutputStream(it.fileDescriptor).use {
                                it.write(completeString.toByteArray())
                            }
                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        if (requestCode == REQUEST_CODE_OPEN_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                readTextFromFile(uri)
            }
        }
    }


    //--------------------STORAGE PERM
    // Код дозволу, який ви можете використати для ідентифікації запиту
    private val STORAGE_PERMISSION_CODE = 1

    // Функція для перевірки дозволу
    private fun isStoragePermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            MAIN,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Функція для запиту дозволу
    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            MAIN,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    // Обробник результату запиту дозволу
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Дозвіл на роботу з файлами було надано
            } else {
                // Дозвіл на роботу з файлами було відхилено
            }
        }
    }

    // Функція, яка може бути викликана для перевірки та запиту дозволу
    private fun checkAndRequestStoragePermission() {
        if (!isStoragePermissionGranted()) {
            requestStoragePermission()
        }
    }


}