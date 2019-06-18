package com.sujeendra.builder_india.phone

import android.Manifest.permission.CALL_PHONE
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telecom.TelecomManager
import android.telecom.TelecomManager.ACTION_CHANGE_DEFAULT_DIALER
import android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_dialer.*
import timber.log.Timber


class DialerActivity : AppCompatActivity() {
    var gotIt=""
    var repeat=false
    var count=0
    fun reset(view: View) {
        val sharedPref = this.getSharedPreferences("com.sujee.storage_app", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("boolean",false).apply()
        sharedPref.edit().putString("index","0").apply()
        Toast.makeText(
            this,
            "Now press Enter in keyboard to start calling from first number in the list",
            Toast.LENGTH_LONG
        ).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialer)
        val mysql = this.openOrCreateDatabase("TELEPHONE NUMBERS", Context.MODE_PRIVATE, null)
        mysql.execSQL("CREATE TABLE IF NOT EXISTS telephone_numbers(SlNo INT(2),telephone_no VARCHAR)")
        mysql.execSQL("INSERT INTO telephone_numbers(SlNo,telephone_no) VALUES(1,'7026335428')")
        mysql.execSQL("INSERT INTO telephone_numbers(SlNo,telephone_no) VALUES(2,'2222222722')")
        mysql.execSQL("INSERT INTO telephone_numbers(SlNo,telephone_no) VALUES(3,'1234567890')")
        //phoneNumberInput.setText(intent?.data?.schemeSpecificPart)
       // val textView = findViewById<TextView>(R.id.textView)
        val variablecount = this.getSharedPreferences("com.sujee.storage_app", Context.MODE_PRIVATE)

        variablecount.edit().putInt("c",++count).apply()
        count=variablecount.getInt("c",0)
        if (count==1) {
            Toast.makeText(
                this,
                "Press Enter No Need of typing number\n Make sure you have added numbers in \nsql database by editing DialerActivity.kt",
                Toast.LENGTH_LONG
            ).show()
        }
        phoneNumberInput.setText("1234567890")

    }

    override fun onStart() {
        super.onStart()
        val mysql = this.openOrCreateDatabase("TELEPHONE NUMBERS", Context.MODE_PRIVATE, null)
        val c = mysql.rawQuery("SELECT * FROM telephone_numbers", null)
        val index = c!!.getColumnIndex("telephone_no")
        c.moveToFirst()
        val sharedPreferences = this.getSharedPreferences("com.sujee.storage_app", Context.MODE_PRIVATE)
        var i=0
        i=Integer.parseInt(sharedPreferences.getString("index", "0"))
        var j=i

        try {
            while (j>0){
                c.moveToNext()
                j--
            }
            gotIt=c.getString(index)
            Timber.w(gotIt)
        }
        catch (e:Exception){
            sharedPreferences.edit().putBoolean("boolean",false).apply()
            Toast.makeText(
                this,
                "Done reading the database/n enter a new database in DialerActivity.kt",
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
        i+=1
        sharedPreferences.edit().putString("index", Integer.toString(i)).apply()
        repeat=sharedPreferences.getBoolean("boolean",false)
        offerReplacingDefaultDialer()
        if(repeat){
            makeCall()
        }
        //textView.text = gotIt
        phoneNumberInput.setOnEditorActionListener { _, _, _ ->
        makeCall()
            sharedPreferences.edit().putBoolean("boolean",true).apply()
            true
        }
    }

    private fun makeCall() {
        if (checkSelfPermission(this, CALL_PHONE) == PERMISSION_GRANTED) {
            val uri = "tel:${gotIt}".toUri()
            startActivity(Intent(Intent.ACTION_CALL, uri))
        } else {
            requestPermissions(this, arrayOf(CALL_PHONE), REQUEST_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION && PERMISSION_GRANTED in grantResults) {
            makeCall()
        }
    }

    private fun offerReplacingDefaultDialer() {
        if (getSystemService(TelecomManager::class.java).defaultDialerPackage != packageName) {
            Intent(ACTION_CHANGE_DEFAULT_DIALER)
                .putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
                .let(::startActivity)
        }
    }

    companion object {
        const val REQUEST_PERMISSION = 0
    }
}
