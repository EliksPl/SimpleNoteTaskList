package com.simpleNotes.simplenotelist.permissions

import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AlertDialog
import com.simpleNotes.simplenotelist.R

class ProtectedAppsController {
    private var POWER_MANAGER_INTENTS = mutableListOf(
        Intent().setComponent(ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
        Intent().setComponent(ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
        Intent().setComponent(ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
        Intent().setComponent(ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
        Intent().setComponent(ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
        Intent().setComponent(ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
        Intent().setComponent(ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
        Intent().setComponent(ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
        Intent().setComponent(ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
        Intent().setComponent(ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity")).setData(
            Uri.parse("mobilemanager://function/entry/AutoStart"))
    )

    fun startPowerSaverIntentIfFirsTime(context: Context){
        val settings = context.getSharedPreferences("ProtectedApps", Context.MODE_PRIVATE)
        val skipMessage = settings.getBoolean("skipProtectedAppCheck", false)
        if (!skipMessage){
            startPowerSaverIntent(context)
        }

    }

    fun startPowerSaverIntent(context: Context) {
        val settings = context.getSharedPreferences("ProtectedApps", Context.MODE_PRIVATE)

        val editor = settings.edit()
        var foundCorrectIntent = false
        for (intent in POWER_MANAGER_INTENTS) {
            if (isCallable(context, intent)) {
                foundCorrectIntent = true
//                   val dontShowAgain = AppCompatCheckBox(context)
//                   dontShowAgain.text = "Do not show again"
//                   dontShowAgain.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
                editor.putBoolean("skipProtectedAppCheck", true)
//                       editor.putInt("permissionRequestedCount",)
                editor.apply()
//                   }
                AlertDialog.Builder(context)
                    .setTitle(Build.MANUFACTURER + " Protected Apps")
                    .setMessage(String.format("%s requires to be enabled in 'Protected Apps' to function properly. You can set this permission later in settings%n", context.getString(
                        R.string.app_name)))
//                       .setView(dontShowAgain)
                    .setPositiveButton("Go to settings") { _: DialogInterface?, _: Int -> context.startActivity(intent) }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
                break
            }
        }
        if (!foundCorrectIntent) {
            editor.putBoolean("skipProtectedAppCheck", true)
            editor.apply()
        }

    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun isCallable(context: Context, intent: Intent): Boolean {
        val list: MutableList<ResolveInfo>
//        list = context.packageManager.queryIntentActivities(
//            intent,
//            PackageManager.MATCH_DEFAULT_ONLY
//        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            list = context.packageManager.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            )
        } else {
            list = context.packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
        }

        return list.size > 0
    }
}