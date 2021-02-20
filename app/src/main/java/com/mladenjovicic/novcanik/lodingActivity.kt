package com.mladenjovicic.novcanik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_loding.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.thread

class lodingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loding)
        Glide.with(this).load(R.drawable.loding_anim).into(imageViewLodingSih)

        var task1 = false
        var task2 = false
        val nameUser = intent.getStringExtra("nameUser")
        val lastNameUser = intent.getStringExtra("lastnameUser")
        val emailUser = intent.getStringExtra("emailUser")
        val idUser = intent.getStringExtra("idUser")
        val userValute = intent.getStringExtra("userValute")
        val db = DataBaseHandler(this)
        val readActions = db.readActions()
        val intent = Intent(this, HomeActivity::class.java )

        val lenghBase = db.readActions().size
        val ref= FirebaseDatabase.getInstance().getReference("/userActions/$idUser")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val userActionFirebase = it.getValue(userActionFirebase::class.java)
                    println("akcijeee"+ it.toString())

                    if(userActionFirebase?.idUser==idUser){
                        if(readActions.size==0){
                            val userActvities = UserActvities(
                                userActionFirebase?.idUser!!,
                                userActionFirebase?.idValuta!!,
                                userActionFirebase?.money!!,
                                userActionFirebase?.category!!,
                                userActionFirebase?.profil!!,
                                userActionFirebase?.timeDate!!,
                                userActionFirebase?.idUserPrimarValut!!,
                                userActionFirebase?.cursValut!!,
                                userActionFirebase?.valueConvert!!,
                                userActionFirebase?.dateCurrencyConvert!!,
                                2
                            )
                            db.insertActions(userActvities)
                        }
                    }

                }
                task1 = true
            }
        })
        for (i in 0 until readActions.size){

            if(readActions[i].idStatus==1&&readActions[i].idUser==idUser){
                val ref1 = FirebaseDatabase.getInstance().getReference("/userActions/$idUser"+"/"+readActions[i].id)
                ref1.setValue(userActionFirebase(readActions[i].id, readActions[i].idUser,readActions[i].idValuta,readActions[i].money,readActions[i].category,
                    readActions[i].profil,readActions[i].timeDate,readActions[i].idUserPrimarValut,readActions[i].cursValut,readActions[i].valueConvert,readActions[i].dateCurrencyConvert))
                val updateActions = db.updateData(readActions[i].id.toString(),readActions[i].idUser,readActions[i].idValuta,readActions[i].money,readActions[i].timeDate,readActions[i].category,readActions[i].profil,readActions[i].idUserPrimarValut,readActions[i].cursValut,readActions[i].valueConvert,readActions[i].dateCurrencyConvert, 2)

            }
            task2=true
        }
        val r = Runnable {

            intent.putExtra("idUser", idUser)
            intent.putExtra("emailUser", emailUser)
            intent.putExtra("nameUser", nameUser)
            intent.putExtra("lastnameUser", lastNameUser)
            intent.putExtra("userValute", userValute)
            intent.putExtra("userLang", lastNameUser)
            startActivity(intent)
        }
        val h = Handler()

        h.postDelayed(r, 2500)


    }


}