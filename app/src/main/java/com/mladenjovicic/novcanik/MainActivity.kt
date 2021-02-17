package com.mladenjovicic.novcanik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registre.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.thread

var login = false
var autoLogin = false
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val context = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Glide.with(this).load(R.drawable.loding).into(imageViewLoding)
        val db = DataBaseHandler(context)
        var datum = LocalDate.now()

        val readValute = db.readValute()
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
            val intent = Intent(this, RegistreActivity::class.java )
            startActivity(intent)
        }

        if(login == true){
            val intent = Intent(this, RegistreActivity::class.java )
            startActivity(intent)
        }else{

        }

        imageViewLoding.visibility = View.GONE
        text_home.visibility = View.GONE
        loginBox.visibility = View.VISIBLE



            btnLoginUser.setOnClickListener {
                //Provjera da li je editText za email i šifru prazni
                if(editTextUserEmailLogin.text.toString().isNotEmpty()&& editTextUserPasswordLogin.text.toString().isNotEmpty()){
                    //provjera podataka u bazi podataka
                    val userData = db.readData()
                    for (i in 0 until userData.size) {
                    var userId = userData[i].id.toInt()
                    var userEmail = userData[i].email.toString()
                    var userPassword = userData[i].password.toString()
                    var userName = userData[i].name.toString()
                    var userLastname = userData[i].lastName.toString()
                    var userValute=userData[i].primaryMoney.toString()
                    var userLang = userData[i].userLang.toString()
                    if(userEmail == editTextUserEmailLogin.text.toString()&& userPassword == editTextUserPasswordLogin.text.toString()){
                        val intent = Intent(this, HomeActivity::class.java )
                        intent.putExtra("idUser", userId)
                        intent.putExtra("emailUser", userEmail)
                        intent.putExtra("nameUser", userName)
                        intent.putExtra("lastnameUser", userLastname)
                        intent.putExtra("userValute", userValute)
                        intent.putExtra("userLang", userLang)
                        startActivity(intent)
                        break
                    }else{
                        Toast.makeText(context, greskaToasts, Toast.LENGTH_SHORT).show()
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
}
