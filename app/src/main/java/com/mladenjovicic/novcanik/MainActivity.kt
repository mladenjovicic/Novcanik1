package com.mladenjovicic.novcanik

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.TelecomManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

var login = false
var autoLogin = false
class MainActivity : AppCompatActivity() {

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {

        val context = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Glide.with(this).load(R.drawable.loding).into(imageViewLoding)
        val db = DataBaseHandler(context)
        val intent = Intent(this, HomeActivity::class.java )
        val intent2 = Intent(this,  lodingActivity::class.java)
        var datum = LocalDate.now()

        val readValute = db.readValute()
        val userData = db.readData()
        if(readValute.count()==0){
            val currencyValue = currencyValue("2021-01-27", 1.610665, 97.4, 6.2187, 1.0, 0.822132, 0.72775, 0.886654, 103.607, 1.289998,1.269385,75.1062,6.4652)
            db.insertValute(currencyValue)
            currencyValue1()
        }else{
            if (readValute[0].dateCurrency !=datum.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()){
                currencyValue1()
            }

        }

        var jezici = Locale.getDefault().language
        var registruj_se = 0
        var prijavi_se = 0
        var unesi_email = 0
        var unesi_sifru = 0
        var greskaToasts = 0
        var prazna_polja= 0

        if(jezici=="bs" || jezici =="hr"|| jezici == "sr"){
            registruj_se = R.string.srp_registruj_se
            prijavi_se = R.string.srp_prijavi_se
            unesi_email = R.string.srp_unesite_svoj_email
            unesi_sifru = R.string.srp_unesite_svoju_sifru
            greskaToasts = R.string.srp_no_macth_profil
            prazna_polja = R.string.srp_greska_popuniti_sva_polja
        }else{
            prijavi_se = R.string.eng_prijavi_se
            registruj_se = R.string.eng_registruj_se
            unesi_email = R.string.eng_unesite_svoj_email
            greskaToasts = R.string.eng_no_macth_profil
            unesi_sifru = R.string.eng_unesite_svoju_sifru
            prazna_polja = R.string.eng_greska_popuniti_sva_polja
        }

        btnRegisterAct.setText(registruj_se)
        btnLoginUser.setText(prijavi_se)
        editTextUserEmailLogin.setHint(unesi_email)
        editTextUserPasswordLogin.setHint(unesi_sifru)


        btnRegisterAct.setOnClickListener {
            val intent1 = Intent(this, RegistreActivity::class.java )
            startActivity(intent1)
        }

        if(login == true){
            val intent2 = Intent(this, RegistreActivity::class.java )
            startActivity(intent2)
        }else{

        }

        imageViewLoding.visibility = View.GONE
        text_home.visibility = View.GONE
        loginBox.visibility = View.VISIBLE



            btnLoginUser.setOnClickListener {
                //Provjera da li je editText za email i šifru prazni
                if(editTextUserEmailLogin.text.toString().isNotEmpty()&& editTextUserPasswordLogin.text.toString().isNotEmpty()){

                    if(isOnline(context)== false){

                        //provjera podataka u bazi podataka
                        for (i in 0 until userData.size) {
                            var idBase = userData[i].id
                            var userEmail = userData[i].email
                            var userPassword = userData[i].password
                            var userId=userData[i].uidUser
                            var nameUser = userData[i].name
                            var lastName = userData[i].lastName
                            var userValute = userData[i].primaryMoney
                            var userLang = userData[i].userLang


                        if(userEmail == editTextUserEmailLogin.text.toString()&& userPassword == editTextUserPasswordLogin.text.toString()){
                            intent.putExtra("idUser", userId)
                            intent.putExtra("emailUser", userEmail)
                            intent.putExtra("nameUser", nameUser)
                            intent.putExtra("lastnameUser", lastName)
                            intent.putExtra("userValute", userValute)
                            intent.putExtra("userLang", userLang)
                        startActivity(intent)
                        break
                         }else{
                        Toast.makeText(context, greskaToasts, Toast.LENGTH_SHORT).show()
                            }
                        }

                    }else {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                            editTextUserEmailLogin.text.toString(),
                            editTextUserPasswordLogin.text.toString()
                        ).addOnCompleteListener {
                            if (!it.isSuccessful) {

                                //Toast.makeText(this, "Greska 258", Toast.LENGTH_SHORT).show()
                            } else {

                                val uid = FirebaseAuth.getInstance().uid
                                val ref = FirebaseDatabase.getInstance().getReference("/users")

                                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(error: DatabaseError) {

                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        p0.children.forEach {
                                            Log.d("usersss", it.toString())
                                            val userFire = it.getValue(userFirebase::class.java)
                                            if (userFire?.email == editTextUserEmailLogin.text.toString()){
                                            if (userData.count() == 0) {
                                                val user = User(
                                                    userFire?.name!!,
                                                    userFire?.lastName!!,
                                                    userFire?.email!!,
                                                    userFire?.password!!,
                                                    userFire?.primaryMoney!!,
                                                    userFire?.userLang!!,
                                                    uid!!
                                                )
                                                db.insertData(user)}
                                                for (i in 0 until userData.size) {
                                                    var userEmail = userData[i].email
                                                    if(userEmail == editTextUserEmailLogin.text.toString()){
                                                    }else{
                                                        val user = User(
                                                            userFire?.name!!,
                                                            userFire?.lastName!!,
                                                            userFire?.email!!,
                                                            userFire?.password!!,
                                                            userFire?.primaryMoney!!,
                                                            userFire?.userLang!!,
                                                            uid!!
                                                        )
                                                        db.insertData(user)
                                                        break
                                                    }
                                                }
                                                intent2.putExtra("emailUser", userFire?.email)
                                                intent2.putExtra("nameUser", userFire?.name)
                                                intent2.putExtra("lastnameUser", userFire?.lastName)
                                                intent2.putExtra("userValute", userFire?.primaryMoney)
                                                intent2.putExtra("userLang", userFire?.userLang)
                                                intent2.putExtra("idUser", uid)
                                                startActivity(intent2)
                                                }

                                        }
                                    }
                                })

                            }
                        }.addOnFailureListener {
                            println("greskica +${it.message}")
                            if(it.message=="The password is invalid or the user does not have a password."){
                                Toast.makeText(this,"Šifra nije ispravna",Toast.LENGTH_LONG).show()
                            }
                            if (it.message=="There is no user record corresponding to this identifier. The user may have been deleted."){
                                Toast.makeText(this,"Nepoznat email",Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                if (checkBoxSave.isChecked){
                    autoLogin = true

                }else{
                    autoLogin = false
                }
            }else{
                    Toast.makeText(this, prazna_polja, Toast.LENGTH_SHORT).show()
                }

        }


    }
    fun currencyValue1(){
        val url = "https://api.currencyfreaks.com/latest?apikey=04b0aa8e72bc4d03a61f742175c58d95&symbols=BAM,RSD,HRK,USD,EUR,GBP,CHF,JPY,AUD,CAD,RUB,CNY"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object:Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()
                println("test123"+ body)
                val jsonObj = JSONObject(body)
                val rates= jsonObj.getJSONObject("rates")
                val valueDate = jsonObj.getString("date")
                var BAM = rates.getString("BAM")
                var RSD = rates.getString("RSD")
                var HRK = rates.getString("HRK")
                var USD = rates.getString("USD")
                var EUR = rates.getString("EUR")
                var GBP = rates.getString("GBP")
                var CHF = rates.getString("CHF")
                var JPY = rates.getString("JPY")
                var AUD = rates.getString("AUD")
                var CAD = rates.getString("CAD")
                var RUB = rates.getString("RUB")
                var CNY = rates.getString("CNY")
                val db = DataBaseHandler(this@MainActivity)
                val currencyValue = currencyValue(valueDate.take(10), BAM.toString().toDouble(), RSD.toString().toDouble(), HRK.toString().toDouble(), USD.toString().toDouble(), EUR.toString().toDouble(), GBP.toString().toDouble(), CHF.toString().toDouble(), JPY.toString().toDouble(), AUD.toString().toDouble(),CAD.toString().toDouble(),RUB.toString().toDouble(),CNY.toString().toDouble())
                db.insertValute(currencyValue)
                println("BAM"+valueDate)

            }
            override fun onFailure(call: Call, e: IOException) {
                println("greska sa serverom")
            }

        })

    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        Log.i("Internet", "nema neta")
        return false
    }
}
