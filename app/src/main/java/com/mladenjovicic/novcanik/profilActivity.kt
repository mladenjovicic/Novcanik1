package com.mladenjovicic.novcanik

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivities
import androidx.core.app.ActivityCompat.startActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor

import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.typeOf


private val money = arrayListOf<Double>()
private val valuta = arrayListOf<String>()
private val categoria = arrayListOf<String>()
private val dateTime2 = arrayListOf<String>()
private val idActivitis = arrayListOf<Int>()
private val profilAktivnost = arrayListOf<String>()
private val idUserActivitiy = arrayListOf<Int>()
private val idUserPrimarValut = arrayListOf<String>()
private val cursValut = arrayListOf<Double>()
private val valueConvert = arrayListOf<Double>()
private val dateCurrencyConvert = arrayListOf<String>()
var test:Int= 0
var profilStatus:Int=0

class profilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        arrayClear()
        work()
        profilStatus = intent.getIntExtra("profilStatus", 0)
    }
    override fun onRestart() {
        arrayClear()
        work()
        super.onRestart()
    }
    override fun onResume() {

        arrayClear()
        work()
        super.onResume()
    }

    fun arrayClear(){
        idUserPrimarValut.clear()
        cursValut.clear()
        valueConvert.clear()
        dateCurrencyConvert.clear()
        money.clear()
        valuta.clear()
        categoria.clear()
        dateTime2.clear()
        idActivitis.clear()
        profilAktivnost.clear()
    }

    fun  work() {
        val nameUser = intent.getStringExtra("nameUser")
        val lastNameUser = intent.getStringExtra("lastnameUser")
        val emailUser = intent.getStringExtra("emailUser")
        val idUser = intent.getIntExtra("idUser",0)
        val userValute = intent.getStringExtra("userValute")
        val context = this
        val db = DataBaseHandler(context)
        val readActions = db.readActions()

        for (i in 0 until readActions.size) {
            if (idUser == readActions[i].idUser) {
                if (profilStatus == 1 && readActions[i].profil == "Novcanik") {
                    money.add(readActions[i].money)
                    valuta.add(readActions[i].idValuta)
                    categoria.add(readActions[i].category)
                    dateTime2.add(readActions[i].timeDate)
                    idActivitis.add(readActions[i].id)
                    profilAktivnost.add(readActions[i].profil)
                    idUserActivitiy.add(readActions[i].idUser)
                    idUserPrimarValut.add(readActions[i].idUserPrimarValut)
                    cursValut.add(readActions[i].cursValut)
                    valueConvert.add(readActions[i].valueConvert)
                    dateCurrencyConvert.add(readActions[i].dateCurrencyConvert)


                } else if (profilStatus == 2 && readActions[i].profil == "Devize") {
                    money.add(readActions[i].money)
                    valuta.add(readActions[i].idValuta)
                    categoria.add(readActions[i].category)
                    dateTime2.add(readActions[i].timeDate)
                    idActivitis.add(readActions[i].id)
                    profilAktivnost.add(readActions[i].profil)
                    idUserActivitiy.add(readActions[i].idUser)
                    idUserPrimarValut.add(readActions[i].idUserPrimarValut)
                    cursValut.add(readActions[i].cursValut)
                    valueConvert.add(readActions[i].valueConvert)
                    dateCurrencyConvert.add(readActions[i].dateCurrencyConvert)


                } else if (profilStatus == 3 && readActions[i].profil == "Banka") {
                    money.add(readActions[i].money)
                    valuta.add(readActions[i].idValuta)
                    categoria.add(readActions[i].category)
                    dateTime2.add(readActions[i].timeDate)
                    idActivitis.add(readActions[i].id)
                    profilAktivnost.add(readActions[i].profil)
                    idUserActivitiy.add(readActions[i].idUser)
                    idUserPrimarValut.add(readActions[i].idUserPrimarValut)
                    cursValut.add(readActions[i].cursValut)
                    valueConvert.add(readActions[i].valueConvert)
                    dateCurrencyConvert.add(readActions[i].dateCurrencyConvert)


                } else if (profilStatus == 4) {
                    money.add(readActions[i].money)
                    valuta.add(readActions[i].idValuta)
                    categoria.add(readActions[i].category)
                    dateTime2.add(readActions[i].timeDate)
                    idActivitis.add(readActions[i].id)
                    profilAktivnost.add(readActions[i].profil)
                    idUserActivitiy.add(readActions[i].idUser)
                    idUserPrimarValut.add(readActions[i].idUserPrimarValut)
                    cursValut.add(readActions[i].cursValut)
                    valueConvert.add(readActions[i].valueConvert)
                    dateCurrencyConvert.add(readActions[i].dateCurrencyConvert)


                } else {


                }
            }
        }
        val profilStatus = intent.getIntExtra("profilStatus", 0)

        val listView = findViewById<ListView>(R.id.listViewProfil)
        listView.adapter = profilAdapter(this)
    }


    private class profilAdapter(context: Context) : BaseAdapter() {
        private val mContext: Context

        init {
            mContext = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var PosNeg = 1
            var moneyInsert = 0.0
            val layoutInflater = LayoutInflater.from(mContext)
            val rowProfil = layoutInflater.inflate(R.layout.profilticet, parent, false)
            val moneys = rowProfil.findViewById<TextView>(R.id.textViewVrijednostTransakcije)
            val kategorija = rowProfil.findViewById<TextView>(R.id.textViewCategorija)
            val datum = rowProfil.findViewById<TextView>(R.id.textViewDatum)
            var btnDelete = rowProfil.findViewById<ImageView>(R.id.ImageViewDelete)
            val slikaPrfil = rowProfil.findViewById<ImageView>(R.id.imageViewProfil)
            var btnEdit = rowProfil.findViewById<ImageView>(R.id.imageViewEdit)
            val layoutProfil = rowProfil.findViewById<LinearLayout>(R.id.linearLayoutProfil)
            val boxEdit = rowProfil.findViewById<LinearLayout>(R.id.linearLayoutBoxEdit)
            val boxBtnEdit = rowProfil.findViewById<LinearLayout>(R.id.linearLayoutBtnEdit)
            val boxSeen =rowProfil.findViewById<LinearLayout>(R.id.LinearLayoutInfo)
            val boxSeenBtn = rowProfil.findViewById<LinearLayout>(R.id.LinearLayoutButtons)
            val btnSaveEdit = rowProfil.findViewById<ImageView>(R.id.ImageViewSave)
            val btnBackEdit = rowProfil.findViewById<ImageView>(R.id.ImageViewBack)
            val statusMoneyEdit = rowProfil.findViewById<TextView>(R.id.textViewPosNeg)
            val enterEditMoney = rowProfil.findViewById<EditText>(R.id.editTextEnterValueMoney)
            val textViewDlt = rowProfil.findViewById<TextView>(R.id.textViewDlt)

            btnDelete.setOnClickListener{
               var deleting =  idActivitis.get(position) .toString()
                val context = this
                val db = DataBaseHandler(mContext)
                val deleteActions = db.deleteActions(deleting)
                boxEdit.visibility = View.GONE
                boxBtnEdit.visibility = View.GONE
                boxSeen.visibility = View.GONE
                boxSeenBtn.visibility = View.GONE
                textViewDlt.visibility = View.VISIBLE
                slikaPrfil.visibility = View.GONE

                textViewDlt.text = "Izbrisana je akcija"


            }
            btnEdit.setOnClickListener {
                boxEdit.visibility = View.VISIBLE
                boxBtnEdit.visibility = View.VISIBLE
                boxSeen.visibility = View.GONE
                boxSeenBtn.visibility = View.GONE
            }
            btnBackEdit.setOnClickListener {
                boxEdit.visibility = View.GONE
                boxBtnEdit.visibility = View.GONE
                boxSeen.visibility = View.VISIBLE
                boxSeenBtn.visibility = View.VISIBLE
            }
            statusMoneyEdit.setOnClickListener {
                if(PosNeg == 1){
                    statusMoneyEdit.text="-"
                    PosNeg = 0
                }else{
                    statusMoneyEdit.text="+"
                    PosNeg = 1
                }
            }
            btnSaveEdit.setOnClickListener {

                if(enterEditMoney.text.isEmpty()){
                    Toast.makeText(mContext, "Polje mora biti popunjeno", Toast.LENGTH_SHORT).show()
                }else{
                    if(statusMoneyEdit.text == "-"){
                        moneyInsert = enterEditMoney.text.toString().toDouble()* (-1.0)
                    }else{
                        moneyInsert = enterEditMoney.text.toString().toDouble()
                    }
                    val test = rowProfil.findViewById<TextView>(R.id.textViewVrijednostTransakcije)

                var id =  idActivitis.get(position) .toString()
                var idUser = idUserActivitiy.get(position)
                var valuta = valuta.get(position)
                var moneys = moneyInsert
                var datetime = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")).toString()
                var category = categoria.get(position)
                var profil = profilAktivnost.get(position)
                var userPrimarValut= idUserPrimarValut.get(position)
                    var cursVal = cursValut.get(position)
                    var cursDate = dateCurrencyConvert.get(position)
                val context = this
                val db = DataBaseHandler(mContext)
                val updateActions = db.updateData(id,idUser,valuta, moneys,datetime,category,profil,userPrimarValut,cursVal,
                    (moneys*cursVal), cursDate )
                enterEditMoney.text.clear()
                boxEdit.visibility = View.GONE
                boxBtnEdit.visibility = View.GONE
                boxSeen.visibility = View.VISIBLE
                boxSeenBtn.visibility = View.VISIBLE
                    if (moneys < 0.0) {
                        test.setTextColor(getColor(mContext, R.color.colorRed))
                    }else{
                        test.setTextColor(getColor(mContext, R.color.colorWhite))
                    }
                    test.text = "Novca: " + moneys + " " + com.mladenjovicic.novcanik.valuta.get(position)
                    datum.text = "Datum: " + datetime
                }
            }
            if (position % 2 == 0) {
                rowProfil.setBackgroundColor(getColor(mContext,R.color.colorDarkGreen))
            } else {
                rowProfil.setBackgroundColor(getColor(mContext, R.color.colorLightGreen))
            }
            if (money.get(position) < 0.0) {
                moneys.setTextColor(getColor(mContext, R.color.colorRed))
            }
            if (profilAktivnost.get(position) == "Novcanik") {
                slikaPrfil.setImageResource(R.drawable.wallethome)
            } else if (profilAktivnost.get(position) == "Banka") {
                slikaPrfil.setImageResource(R.drawable.wallermoneyicone)
            } else if (profilAktivnost.get(position) == "Devize") {
                slikaPrfil.setImageResource(R.drawable.walletgoldbar)
            } else if (profilAktivnost.get(position)== "Ostalo"){
                slikaPrfil.setImageResource(R.drawable.clockprofil)
            }
            moneys.text = "Novca: " + money.get(position) + " " + valuta.get(position)
            kategorija.text = "Kategorija: " + categoria.get(position)
            datum.text = "Datum: " + dateTime2.get(position)
            return rowProfil
        }
        override fun getCount(): Int {
            return money.size
        }
        override fun getItem(position: Int): Any {
            return "testString"
        }
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
    }
}
