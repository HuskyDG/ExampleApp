package io.github.huskydg.exampleapp

import android.content.pm.PackageInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jaredrummler.ktsh.Shell


class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var shell: Shell
        lateinit var sushell: Shell
        val packagename = BuildConfig.APPLICATION_ID
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ainfo = this.applicationContext.packageManager.getApplicationInfo(
            "$packagename",
            PackageManager.GET_SHARED_LIBRARY_FILES
        )
        val libpath = "${ainfo.nativeLibraryDir}"
        val bb = "$libpath/libbusybox.so"

        shell = Shell("$bb sh")
        sushell = Shell("$bb sh")

        val supath = shell.run("which su")

        val result = findViewById<TextView>(R.id.resultView)
        val bgresult = findViewById<FrameLayout>(R.id.frameLayout)
        var hasMagisk = false
        var MagiskVersion: String = ""

        if (supath.isSuccess && shell.run(supath.stdout() + " -c true").isSuccess){
            sushell = Shell(supath.stdout())

            if (sushell.isAlive()){
                // we have root access, check for magisk
                val runMagisk = sushell.run("magisk -v")
                hasMagisk = runMagisk.isSuccess // check if we has Magisk
                if (hasMagisk){
                    MagiskVersion = runMagisk.stdout()
                }
            }
        }

        if (!sushell.isAlive()) {
            result.setText("Root access not found")
        } else if (!hasMagisk) {
            result.setText("Magisk not found")
        } else {
            result.setText("Magisk: " + MagiskVersion)
            bgresult.setBackgroundResource(R.drawable.circle_bg_green)
        }
    }


}