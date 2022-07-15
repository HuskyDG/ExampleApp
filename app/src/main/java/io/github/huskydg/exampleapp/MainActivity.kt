package io.github.huskydg.exampleapp

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jaredrummler.ktsh.Shell


class MainActivity : AppCompatActivity() {
    companion object {
        val shell = Shell("/system/bin/sh")
        var sushell = Shell("/system/bin/sh")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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