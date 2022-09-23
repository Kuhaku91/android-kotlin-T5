package Alif.hariyanto.tugas6

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AppxofFirebaseMessagingService : FirebaseMessagingService() {

    var promoId = ""
    var promo = ""
    var promoUntil= ""
    var body = ""
    var title = ""
    val RC_INTENT = 100
    val CHANNEL_ID = "tugas6"

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage){
        super.onMessageReceived(p0)

        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        if(p0.data.isNotEmpty()){
//            Log.d("data-notif",p0.data.getValue("promoId").toString() )
            promoId = p0.data.getValue("promoId").toString()
            promo = p0.data.getValue("promo").toString()
            promoUntil = p0.data.getValue("promoUntil").toString()

            intent.putExtra("promoId",promoId)
            intent.putExtra("promo",promo)
            intent.putExtra("promoUntil",promoUntil)
            intent.putExtra("type",0)

            sendNotif("Today Promo!!!","$promo $promoUntil",intent)
        }
        if(p0.notification !=null){
            body =p0.notification!!.body!!
            title =p0.notification!!.title!!

            intent.putExtra("title",title)
            intent.putExtra("body",body)
            intent.putExtra("type",1)

            sendNotif(title, body, intent)
        }
    }

    fun sendNotif(title: String, body: String, intent: Intent) {
        val pendingIntent = PendingIntent.getActivity(this, RC_INTENT, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val audioAttributes = AudioAttributes.Builder().
        setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).
        setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            val mChannel = NotificationChannel(CHANNEL_ID, "tugas6",
                NotificationManager.IMPORTANCE_HIGH)
            mChannel.description = "tugas6"
            mChannel.setSound(ringtoneUri,audioAttributes)
            notificationManager.createNotificationChannel(mChannel)

        }
        val notificationBuilder = NotificationCompat.Builder(baseContext,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.ic_launcher_foreground))
            .setContentTitle(title)
            .setContentText(body)
            .setSound(ringtoneUri)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true).build()
        notificationManager.notify(RC_INTENT,notificationBuilder)
    }
}