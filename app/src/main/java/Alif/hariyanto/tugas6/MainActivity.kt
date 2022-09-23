package Alif.hariyanto.tugas6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import kotlinx.android.synthetic.main.activity_main.*
import java.lang. Exception

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var bundle : Bundle? =null
    var topik = "tugas6"
    var type = 0
    private lateinit var db: FirebaseFirestore
    lateinit var userdata:HashMap<String,String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Firebase.firestore
        btnSave.setOnClickListener(this)
        Firebase.messaging.subscribeToTopic(topik).addOnCompleteListener {
            var msg = "Subcribe to $topik"
            if (!it.isSuccessful) msg = "Can't subscribe to topic"
            Toast.makeText(this, "$msg", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(!it.isSuccessful) return@addOnCompleteListener
            edToken.setText(it.result!!.toString())
        }
        try {
            bundle = getIntent().getExtras()!!
        }catch (e : Exception){
            Log.e("BUNDLE","bundle is null")
        }
        if (bundle !=null){
            type = bundle!!.getInt("type")
            when(type){
                0 ->{
                    edPromoId.setText(bundle!!.getString("promoId"))
                    edPromo.setText(bundle!!.getString("promo"))
                    edPromoUntil.setText(bundle!!.getString("promoUntil"))
                    userdata= hashMapOf(
                        "type" to "0",
                        "promoId" to edPromoId.text.toString(),
                        "promo" to edPromo.text.toString(),
                        "promoUntil" to edPromoUntil.text.toString(),
                    )
                }
                1 ->{
                    edTitle.setText(bundle!!.getString("title"))
                    edBody.setText(bundle!!.getString("body"))
                    userdata= hashMapOf(
                        "type" to "1",
                        "title" to edTitle.text.toString(),
                        "body" to edBody.text.toString(),
                    )
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnSave ->{
                if (userdata["type"]=="1"){
                    val DocumentReference = db.collection("notification").document(userdata["title"].toString())
                    DocumentReference.set(userdata)
                }
                else{
                    val DocumentReference = db.collection("data").document(userdata["promoId"].toString())
                    DocumentReference.set(userdata)
                }
            }
        }
    }
}