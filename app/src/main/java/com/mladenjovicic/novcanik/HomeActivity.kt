package com.mladenjovicic.novcanik

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.engine.Resource
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import java.util.*


import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_registre.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        work()


    }
    override fun onStart() {
        super.onStart()
        work()
    }
    override fun onResume() {
        super.onResume()
        work()
    }


    fun work(){
        val context = this
        val db = DataBaseHandler(context)
        val readActions = db.readActions()

        var sumMoneyNovcanik = 0.0
        var sumMoneyDevize = 0.0
        var sumMoneyBanka = 0.0
        var sumSum = 0.0
        var sumMOneyDevizeNEgativ = 0.0
        var sumMOneyNovcanikaNEgativ = 0.0
        var sumMOneyBankNEgativ = 0.0
        val res: Resources = resources

        val pieChart = findViewById<PieChart>(R.id.pieChart)
        val pieChartNegativ = findViewById<PieChart>(R.id.pieChart1)
        val arrayBank = ArrayList<PieEntry>()
        val arrayBankNegativ = ArrayList<PieEntry>()

        var listCategorys = res.getStringArray(R.array.category)
        var listWalletCategory  = ArrayList<Double>()
        var listDeviseCategory  = ArrayList<Double>()
        var listBankCategory  = ArrayList<Double>()
        var listALLCategory  = ArrayList<Double>()
        var listDeviseCategoryNegativ = ArrayList<Double>()
        var listWalletCategoryNeg = ArrayList<Double>()
        var listBankCategoryNeg = ArrayList<Double>()
        var listAllCategoryNeg = ArrayList<Double>()
        for (j in 0 until listCategorys.size) {
            listWalletCategory.add(0.0)
            listBankCategory.add(0.0)
            listDeviseCategory.add(0.0)
            listALLCategory.add(0.0)
            listDeviseCategoryNegativ.add(0.0)
            listWalletCategoryNeg.add(0.0)
            listBankCategoryNeg.add(0.0)
            listAllCategoryNeg.add(0.0)
        }

        val nameUser = intent.getStringExtra("nameUser")
        val lastNameUser = intent.getStringExtra("lastnameUser")
        val emailUser = intent.getStringExtra("emailUser")
        val idUser = intent.getIntExtra("idUser", 0)
        val userValute = intent.getStringExtra("userValute")


        var spinnerPlan= findViewById<Spinner>(R.id.spinnerPlan)
        val adapter = ArrayAdapter.createFromResource(this, R.array.plan, R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerPlan.adapter = adapter
        //spinnerPlan.onItemSelectedListener = this

        var spinnerPlanKategorija= findViewById<Spinner>(R.id.spinnerPlanCategoria)
        val adapterCat = ArrayAdapter.createFromResource(this, R.array.category, R.layout.spinner_item)
        adapterCat.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerPlanKategorija.adapter = adapterCat

        var spinnerPlanProfil= findViewById<Spinner>(R.id.spinnerProfilPlan)
        val adapterProfil = ArrayAdapter.createFromResource(this, R.array.profil, R.layout.spinner_item)
        adapterProfil.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerPlanProfil.adapter = adapterProfil


        val context1 = this
        val db1 = DataBaseHandler(context1)
        val userPlan = db1.readPlan()


        for (i in 0 until userPlan.size) {
            /*textViewTestAC.append(
                userPlan[i].idUser.toString() +" test  "+  userPlan[i].nextPayment + userPlan[i].moneyRata.toString()
            )*/
            var datum = LocalDate.now()
            if(userPlan[i].idUser==idUser){
                if(userPlan[i].nextPayment == datum.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")).toString()){
                    if(userPlan[i].otherMoney>0.0){
                        when(userPlan[i].plan) {
                            0 -> {
                                datum= LocalDate.now().plusDays(1)
                            }
                            1 -> {
                                datum = LocalDate.now().plusDays(7)
                            }
                            2 -> {
                                datum= LocalDate.now().plusDays(30)
                            }

                        }
                        var getMoney = 0.0
                        var ostatakNovca= userPlan[i].otherMoney-userPlan[i].moneyRata
                        if(ostatakNovca<0){
                            ostatakNovca = 0.0
                        }
                        if(userPlan[i].otherMoney >= userPlan[i].moneyRata){
                            getMoney= userPlan[i].moneyRata
                        }else if(userPlan[i].otherMoney < userPlan[i].moneyRata){
                            getMoney = userPlan[i].otherMoney
                        }

                        val updatePlan = db1.updatePlan(userPlan[i].id.toString(),userPlan[i].idUser, userPlan[i].plan, userPlan[i].moneyPlan, ostatakNovca,
                            userPlan[i].moneyRata,userPlan[i].categoryPlan, datum.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")).toString(),userPlan[i].profilPlan)

                        val userActivity = UserActvities(idUser, userValute.toString(), (getMoney)*-1,userPlan[i].categoryPlan, userPlan[i].profilPlan,
                            LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")).toString())
                        db.insertActions(userActivity)
                    }
                }

            }
        }


        for (i in 0 until readActions.size) {
            if(readActions[i].idUser==idUser&& readActions[i].profil== "Novcanik" ){
                sumMOneyNovcanikaNEgativ = sumMOneyNovcanikaNEgativ + readActions[i].money
                sumMoneyNovcanik = sumMoneyNovcanik + readActions[i].money

                if (readActions[i].category=="Hrana i piće"){
                    var hipN = listWalletCategory[0] + readActions[i].money
                    listWalletCategory.set(0, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listWalletCategoryNeg[0]+readActions[i].money
                        listWalletCategoryNeg.set(0, hipNN)
                        var hipNNA = listAllCategoryNeg[0]+readActions[i].money
                        listAllCategoryNeg.set(0,hipNNA)
                    }
                }
                if (readActions[i].category=="Šoping"){
                    var hipN = listWalletCategory[1] + readActions[i].money
                    listWalletCategory.set(1, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listWalletCategoryNeg[1]+readActions[i].money
                        listWalletCategoryNeg.set(1, hipNN)
                        var hipNNA = listAllCategoryNeg[1]+readActions[i].money
                        listAllCategoryNeg.set(1,hipNNA)
                    }
                }
                if (readActions[i].category=="Transport"){
                    var hipN = listWalletCategory[2] + readActions[i].money
                    listWalletCategory.set(2, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listWalletCategoryNeg[2]+readActions[i].money
                        listWalletCategoryNeg.set(2, hipNN)
                        var hipNNA = listAllCategoryNeg[2]+readActions[i].money
                        listAllCategoryNeg.set(2,hipNNA)
                    }
                }
                if (readActions[i].category=="Režije"){
                    var hipN = listWalletCategory[3] + readActions[i].money
                    listWalletCategory.set(3, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listWalletCategoryNeg[3]+readActions[i].money
                        listWalletCategoryNeg.set(3, hipNN)
                        var hipNNA = listAllCategoryNeg[3]+readActions[i].money
                        listAllCategoryNeg.set(3,hipNNA)
                    }
                }
                if (readActions[i].category=="Automobil"){
                    var hipN = listWalletCategory[4] + readActions[i].money
                    listWalletCategory.set(4, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listWalletCategoryNeg[4]+readActions[i].money
                        listWalletCategoryNeg.set(4, hipNN)
                        var hipNNA = listAllCategoryNeg[4]+readActions[i].money
                        listAllCategoryNeg.set(4,hipNNA)
                    }
                }
                if (readActions[i].category=="Život i zabava"){
                    var hipN = listWalletCategory[5] + readActions[i].money
                    listWalletCategory.set(5, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listWalletCategoryNeg[5]+readActions[i].money
                        listWalletCategoryNeg.set(5, hipNN)
                        var hipNNA = listAllCategoryNeg[5]+readActions[i].money
                        listAllCategoryNeg.set(5,hipNNA)
                    }
                }
                if (readActions[i].category=="Investicije"){
                    var hipN = listWalletCategory[6] + readActions[i].money
                    listWalletCategory.set(6, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listWalletCategoryNeg[6]+readActions[i].money
                        listWalletCategoryNeg.set(6, hipNN)
                        var hipNNA = listAllCategoryNeg[6]+readActions[i].money
                        listAllCategoryNeg.set(6,hipNNA)
                    }
                }
                if (readActions[i].category=="Putovanja"){
                    var hipN = listWalletCategory[7] + readActions[i].money
                    listWalletCategory.set(7, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listWalletCategoryNeg[7]+readActions[i].money
                        listWalletCategoryNeg.set(7, hipNN)
                        var hipNNA = listAllCategoryNeg[7]+readActions[i].money
                        listAllCategoryNeg.set(7,hipNNA)
                    }
                }
                if (readActions[i].category=="Doktor i lijekovi"){
                    var hipN = listWalletCategory[8] + readActions[i].money
                    listWalletCategory.set(8, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listWalletCategoryNeg[8]+readActions[i].money
                        listWalletCategoryNeg.set(8, hipNN)
                        var hipNNA = listAllCategoryNeg[8]+readActions[i].money
                        listAllCategoryNeg.set(8,hipNNA)
                    }
                }
                if (readActions[i].category=="Edukacija"){
                    var hipN = listWalletCategory[9] + readActions[i].money
                    listWalletCategory.set(9, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listWalletCategoryNeg[9]+readActions[i].money
                        listWalletCategoryNeg.set(9, hipNN)
                        var hipNNA = listAllCategoryNeg[9]+readActions[i].money
                        listAllCategoryNeg.set(9,hipNNA)
                    }
                }
                if (readActions[i].category=="Ostalo"){
                    var hipN = listWalletCategory[10] + readActions[i].money
                    listWalletCategory.set(10, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listWalletCategoryNeg[10]+readActions[i].money
                        listWalletCategoryNeg.set(10, hipNN)
                        var hipNNA = listAllCategoryNeg[10]+readActions[i].money
                        listAllCategoryNeg.set(10,hipNNA)
                    }
                }

            }
            if(readActions[i].idUser==idUser&& readActions[i].profil== "Devize" ){
                sumMOneyDevizeNEgativ = sumMOneyDevizeNEgativ + readActions[i].money
                sumMoneyDevize = sumMoneyDevize + readActions[i].money
                if (readActions[i].category=="Hrana i piće"){
                    var hipN = listDeviseCategory[0] + readActions[i].money
                    listDeviseCategory.set(0, hipN )

                    if(readActions[i].money< 0){
                    var hipNN = listDeviseCategoryNegativ[0]+readActions[i].money
                        listDeviseCategoryNegativ.set(0, hipNN)
                        var hipNNA = listAllCategoryNeg[0]+readActions[i].money
                        listAllCategoryNeg.set(0,hipNNA)
                    }
                }
                if (readActions[i].category=="Šoping"){
                    var hipN = listDeviseCategory[1] + readActions[i].money
                    listDeviseCategory.set(1, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listDeviseCategoryNegativ[1]+readActions[i].money
                        listDeviseCategoryNegativ.set(1, hipNN)
                        var hipNNA = listAllCategoryNeg[1]+readActions[i].money
                        listAllCategoryNeg.set(1,hipNNA)
                    }
                }
                if (readActions[i].category=="Transport"){
                    var hipN = listDeviseCategory[2] + readActions[i].money
                    listDeviseCategory.set(2, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listDeviseCategoryNegativ[2]+readActions[i].money
                        listDeviseCategoryNegativ.set(2, hipNN)
                        var hipNNA = listAllCategoryNeg[2]+readActions[i].money
                        listAllCategoryNeg.set(2,hipNNA)
                    }
                }
                if (readActions[i].category=="Režije"){
                    var hipN = listDeviseCategory[3] + readActions[i].money
                    listDeviseCategory.set(3, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listDeviseCategoryNegativ[3]+readActions[i].money
                        listDeviseCategoryNegativ.set(3, hipNN)
                        var hipNNA = listAllCategoryNeg[3]+readActions[i].money
                        listAllCategoryNeg.set(3,hipNNA)
                    }
                }
                if (readActions[i].category=="Automobil"){
                    var hipN = listDeviseCategory[4] + readActions[i].money
                    listDeviseCategory.set(4, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listDeviseCategoryNegativ[4]+readActions[i].money
                        listDeviseCategoryNegativ.set(4, hipNN)
                        var hipNNA = listAllCategoryNeg[4]+readActions[i].money
                        listAllCategoryNeg.set(4,hipNNA)
                    }
                }
                if (readActions[i].category=="Život i zabava"){
                    var hipN = listDeviseCategory[5] + readActions[i].money
                    listDeviseCategory.set(5, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listDeviseCategoryNegativ[5]+readActions[i].money
                        listDeviseCategoryNegativ.set(5, hipNN)
                        var hipNNA = listAllCategoryNeg[5]+readActions[i].money
                        listAllCategoryNeg.set(5,hipNNA)
                    }
                }
                if (readActions[i].category=="Investicije"){
                    var hipN = listDeviseCategory[6] + readActions[i].money
                    listDeviseCategory.set(6, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listDeviseCategoryNegativ[6]+readActions[i].money
                        listDeviseCategoryNegativ.set(6, hipNN)
                        var hipNNA = listAllCategoryNeg[6]+readActions[i].money
                        listAllCategoryNeg.set(6,hipNNA)
                    }
                }
                if (readActions[i].category=="Putovanja"){
                    var hipN = listDeviseCategory[7] + readActions[i].money
                    listDeviseCategory.set(7, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listDeviseCategoryNegativ[7]+readActions[i].money
                        listDeviseCategoryNegativ.set(7, hipNN)
                        var hipNNA = listAllCategoryNeg[7]+readActions[i].money
                        listAllCategoryNeg.set(7,hipNNA)
                    }
                }
                if (readActions[i].category=="Doktor i lijekovi"){
                    var hipN = listDeviseCategory[8] + readActions[i].money
                    listDeviseCategory.set(8, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listDeviseCategoryNegativ[8]+readActions[i].money
                        listDeviseCategoryNegativ.set(8, hipNN)
                        var hipNNA = listAllCategoryNeg[8]+readActions[i].money
                        listAllCategoryNeg.set(8,hipNNA)
                    }
                }
                if (readActions[i].category=="Edukacija"){
                    var hipN = listDeviseCategory[9] + readActions[i].money
                    listDeviseCategory.set(9, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listDeviseCategoryNegativ[9]+readActions[i].money
                        listDeviseCategoryNegativ.set(9, hipNN)
                        var hipNNA = listAllCategoryNeg[9]+readActions[i].money
                        listAllCategoryNeg.set(9,hipNNA)
                    }
                }
                if (readActions[i].category=="Ostalo"){
                    var hipN = listDeviseCategory[10] + readActions[i].money
                    listDeviseCategory.set(10, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listDeviseCategoryNegativ[10]+readActions[i].money
                        listDeviseCategoryNegativ.set(10, hipNN)
                        var hipNNA = listAllCategoryNeg[10]+readActions[i].money
                        listAllCategoryNeg.set(10,hipNNA)
                    }
                }


            }
            if(readActions[i].idUser==idUser&& readActions[i].profil== "Banka" ){
                sumMoneyBanka = sumMoneyBanka + readActions[i].money
                sumMOneyBankNEgativ = sumMOneyBankNEgativ + readActions[i].money
                if (readActions[i].category=="Hrana i piće"){
                    var hipN = listBankCategory[0] + readActions[i].money
                    listBankCategory.set(0, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listBankCategoryNeg[0]+readActions[i].money
                        listBankCategoryNeg.set(0, hipNN)

                        var hipNNA = listAllCategoryNeg[0]+readActions[i].money
                        listAllCategoryNeg.set(0,hipNNA)
                    }

                }
                if (readActions[i].category=="Šoping"){
                    var hipN = listBankCategory[1] + readActions[i].money
                    listBankCategory.set(1, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listBankCategoryNeg[1]+readActions[i].money
                        listBankCategoryNeg.set(1, hipNN)
                        var hipNNA = listAllCategoryNeg[1]+readActions[i].money
                        listAllCategoryNeg.set(1,hipNNA)
                    }
                }
                if (readActions[i].category=="Transport"){
                    var hipN = listBankCategory[2] + readActions[i].money
                    listBankCategory.set(2, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listBankCategoryNeg[2]+readActions[i].money
                        listBankCategoryNeg.set(2, hipNN)
                        var hipNNA = listAllCategoryNeg[2]+readActions[i].money
                        listAllCategoryNeg.set(2,hipNNA)
                    }
                }
                if (readActions[i].category=="Režije"){
                    var hipN = listBankCategory[3] + readActions[i].money
                    listBankCategory.set(3, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listBankCategoryNeg[3]+readActions[i].money
                        listBankCategoryNeg.set(3, hipNN)
                        var hipNNA = listAllCategoryNeg[3]+readActions[i].money
                        listAllCategoryNeg.set(3,hipNNA)
                    }
                }
                if (readActions[i].category=="Automobil"){
                    var hipN = listBankCategory[4] + readActions[i].money
                    listBankCategory.set(4, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listBankCategoryNeg[4]+readActions[i].money
                        listBankCategoryNeg.set(4, hipNN)
                        var hipNNA = listAllCategoryNeg[4]+readActions[i].money
                        listAllCategoryNeg.set(4,hipNNA)
                    }
                }
                if (readActions[i].category=="Život i zabava"){
                    var hipN = listBankCategory[5] + readActions[i].money
                    listBankCategory.set(5, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listBankCategoryNeg[5]+readActions[i].money
                        listBankCategoryNeg.set(5, hipNN)
                        var hipNNA = listAllCategoryNeg[5]+readActions[i].money
                        listAllCategoryNeg.set(5,hipNNA)
                    }
                }
                if (readActions[i].category=="Investicije"){
                    var hipN = listBankCategory[6] + readActions[i].money
                    listBankCategory.set(6, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listBankCategoryNeg[6]+readActions[i].money
                        listBankCategoryNeg.set(6, hipNN)
                        var hipNNA = listAllCategoryNeg[6]+readActions[i].money
                        listAllCategoryNeg.set(6,hipNNA)
                    }
                }
                if (readActions[i].category=="Putovanja"){
                    var hipN = listBankCategory[7] + readActions[i].money
                    listBankCategory.set(7, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listBankCategoryNeg[7]+readActions[i].money
                        listBankCategoryNeg.set(7, hipNN)
                        var hipNNA = listAllCategoryNeg[7]+readActions[i].money
                        listAllCategoryNeg.set(7,hipNNA)
                    }
                }
                if (readActions[i].category=="Doktor i lijekovi"){
                    var hipN = listBankCategory[8] + readActions[i].money
                    listBankCategory.set(8, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listBankCategoryNeg[8]+readActions[i].money
                        listBankCategoryNeg.set(8, hipNN)
                        var hipNNA = listAllCategoryNeg[8]+readActions[i].money
                        listAllCategoryNeg.set(8,hipNNA)
                    }
                }
                if (readActions[i].category=="Edukacija"){
                    var hipN = listBankCategory[9] + readActions[i].money
                    listBankCategory.set(9, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listBankCategoryNeg[9]+readActions[i].money
                        listBankCategoryNeg.set(9, hipNN)
                        var hipNNA = listAllCategoryNeg[9]+readActions[i].money
                        listAllCategoryNeg.set(9,hipNNA)
                    }
                }
                if (readActions[i].category=="Ostalo"){
                    var hipN = listBankCategory[10] + readActions[i].money
                    listBankCategory.set(10, hipN )
                    if(readActions[i].money< 0){
                        var hipNN = listBankCategoryNeg[10]+readActions[i].money
                        listBankCategoryNeg.set(10, hipNN)
                        var hipNNA = listAllCategoryNeg[10]+readActions[i].money
                        listAllCategoryNeg.set(10,hipNNA)
                    }
                }
            }
            if(readActions[i].idUser==idUser){
                sumSum = sumSum + readActions[i].money
            }
        }

        btnNovcanik.text ="Novčanik: \n $sumMoneyNovcanik"
        btnBanka.text = "Banka: \n $sumMoneyBanka"
        btnDevise.text = "Devize: \n $sumMoneyDevize"
        btnAllMoney.text = "Sav novac: \n $sumSum"

        for (j in 0 until listCategorys.size) {
            var numv = listBankCategory[j]+listDeviseCategory[j]+listWalletCategory[j]
            listALLCategory.set(j, numv)
            if(listAllCategoryNeg[j]<0.0){



                var upis = listAllCategoryNeg[j]*(-1)

                arrayBankNegativ.add(PieEntry(upis.toFloat(), listCategorys[j]))
            }
        }


        val dataSetNegativ = PieDataSet(arrayBankNegativ, "")
        dataSetNegativ.setDrawIcons(false)

        dataSetNegativ.sliceSpace = 3f
        dataSetNegativ.iconsOffset = MPPointF(0F, 40F)
        dataSetNegativ.selectionShift = 5f
        dataSetNegativ.setColors(*ColorTemplate.JOYFUL_COLORS, *ColorTemplate.COLORFUL_COLORS, *ColorTemplate.MATERIAL_COLORS  )
        //dataSet.setValueFormatter(new MyValueFormatter(mFormat, pieChart))


        val dataNegativ = PieData(dataSetNegativ)
        dataNegativ.setValueTextSize(5f)
        dataNegativ.setValueTextColor(Color.parseColor("#FFFFFF"))
        pieChartNegativ.data = dataNegativ
        pieChartNegativ.highlightValues(null)
        pieChartNegativ.setBackgroundColor(getColor(R.color.colorDarkGreen))
        pieChartNegativ.invalidate()
        pieChartNegativ.setHoleColor(Color.parseColor("#FFFFFF"))
        pieChartNegativ.setUsePercentValues(true)
        pieChartNegativ.animateXY(5000, 5000)
        pieChartNegativ.description.text = " "




        if (sumMoneyBanka > 0.0){
            arrayBank.add(PieEntry(sumMoneyBanka.toFloat(), "Banka"))
        }
        if(sumMoneyDevize > 0.0){
            arrayBank.add(PieEntry(sumMoneyDevize.toFloat(), "Devize"))
        }
        if(sumMoneyNovcanik> 0.0){
            arrayBank.add(PieEntry(sumMoneyNovcanik.toFloat(), "Novcanik"))
        }






        val dataSet = PieDataSet(arrayBank, "")

        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.selectionShift = 5f
        dataSet.setColors(*ColorTemplate.JOYFUL_COLORS )

        val data = PieData(dataSet)
        data.setValueTextSize(11f)
        data.setValueTextColor(getColor(R.color.colorDarkGreen))
        pieChart.data = data
        pieChart.highlightValues(null)
        pieChart.invalidate()
        pieChart.animateXY(5000, 5000)
        pieChart.description.text = " "







        btnDevise.setOnLongClickListener()  {
            val intent = Intent(this, profilActivity::class.java)
            intent.putExtra("profilStatus", 2)
            intent.putExtra("idUser", idUser)
            intent.putExtra("emailUser", emailUser)
            intent.putExtra("nameUser", nameUser)
            intent.putExtra("lastnameUser", lastNameUser)
            intent.putExtra("userValute", userValute)
            //intent.putExtra("userLang", userLang)
            startActivity(intent )

            return@setOnLongClickListener true
        }
        btnNovcanik.setOnLongClickListener() {
            val intent = Intent(this, profilActivity::class.java)
            intent.putExtra("profilStatus", 1)
            intent.putExtra("idUser", idUser)
            intent.putExtra("emailUser", emailUser)
            intent.putExtra("nameUser", nameUser)
            intent.putExtra("lastnameUser", lastNameUser)
            intent.putExtra("userValute", userValute)
            //intent.putExtra("userLang", userLang)
            startActivity(intent )
            return@setOnLongClickListener true

        }
        btnBanka.setOnLongClickListener() {
            val intent = Intent(this, profilActivity::class.java)
            intent.putExtra("profilStatus", 3)
            intent.putExtra("idUser", idUser)
            intent.putExtra("emailUser", emailUser)
            intent.putExtra("nameUser", nameUser)
            intent.putExtra("lastnameUser", lastNameUser)
            intent.putExtra("userValute", userValute)
            //intent.putExtra("userLang", userLang)
            startActivity(intent )
            return@setOnLongClickListener true
        }
        btnAllMoney.setOnLongClickListener { val intent = Intent(this, profilActivity::class.java)
            intent.putExtra("profilStatus", 4)
            intent.putExtra("idUser", idUser)
            intent.putExtra("emailUser", emailUser)
            intent.putExtra("nameUser", nameUser)
            intent.putExtra("lastnameUser", lastNameUser)
            intent.putExtra("userValute", userValute)
            //intent.putExtra("userLang", userLang)
            startActivity(intent )
            return@setOnLongClickListener true }

        btnNovcanik.setOnClickListener {
            btnDevise.setBackgroundColor(getColor(R.color.colorLightGreen))
            btnBanka.setBackgroundColor(getColor(R.color.colorLightGreen))
            btnAllMoney.setBackgroundColor(getColor(R.color.colorLightGreen))
            btnNovcanik.setBackgroundColor(getColor(R.color.colorWhite))
            btnDevise.setTextColor(getResources().getColor(R.color.colorWhite))
            btnBanka.setTextColor(getResources().getColor(R.color.colorWhite))
            btnAllMoney.setTextColor(getResources().getColor(R.color.colorWhite))
            btnNovcanik.setTextColor(getResources().getColor(R.color.colorLightGreen))
            textViewHome.text = "Grafički prikaz prihoda u novčaniku"
            textViewHome1.text = "Grafički prikaz rashoda u novčaniku"
            arrayBank.clear()
            arrayBankNegativ.clear()
            for (i in 0 until listCategorys.size) {

                if(listWalletCategory[i]>0.0){
                    arrayBank.add(PieEntry(listWalletCategory[i].toFloat(), listCategorys[i]))

                }
                if(listWalletCategoryNeg[i]<0.0){



                    var upis = listWalletCategoryNeg[i]*(-1)

                    var upis1 = upis / (listWalletCategoryNeg.sum()*-1)
                    upis1 = upis1 * 100
                    arrayBankNegativ.add(PieEntry(upis1.toFloat(), listCategorys[i]))
                }
            }
            val dataSet = PieDataSet(arrayBank, "")

            dataSet.setDrawIcons(false)
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0F, 40F)
            dataSet.selectionShift = 5f
            dataSet.setColors(*ColorTemplate.JOYFUL_COLORS, *ColorTemplate.COLORFUL_COLORS, *ColorTemplate.MATERIAL_COLORS  )

            val data = PieData(dataSet)
            data.setValueTextSize(11f)
            data.setValueTextColor(getColor(R.color.colorWhite))
            pieChart.data = data
            pieChart.highlightValues(null)
            pieChart.setBackgroundColor(getColor(R.color.colorDarkGreen))
            pieChart.invalidate()
            pieChart.animateXY(5000, 5000)
            pieChart.description.text = " "

            val pieChartNegativ = findViewById<PieChart>(R.id.pieChart1)
            val dataSetNegativ = PieDataSet(arrayBankNegativ, "")
            dataSetNegativ.setDrawIcons(false)

            dataSetNegativ.sliceSpace = 3f
            dataSetNegativ.iconsOffset = MPPointF(0F, 40F)
            dataSetNegativ.selectionShift = 5f
            dataSetNegativ.setColors(*ColorTemplate.JOYFUL_COLORS, *ColorTemplate.COLORFUL_COLORS, *ColorTemplate.MATERIAL_COLORS  )
            //dataSet.setValueFormatter(new MyValueFormatter(mFormat, pieChart))


            val dataNegativ = PieData(dataSetNegativ)
            dataNegativ.setValueTextSize(5f)
            dataNegativ.setValueTextColor(Color.parseColor("#FFFFFF"))
            pieChartNegativ.data = dataNegativ
            pieChartNegativ.highlightValues(null)
            pieChartNegativ.setBackgroundColor(getColor(R.color.colorDarkGreen))
            pieChartNegativ.invalidate()
            pieChartNegativ.setHoleColor(Color.parseColor("#FFFFFF"))
            pieChartNegativ.setUsePercentValues(true)
            pieChartNegativ.animateXY(5000, 5000)
            pieChartNegativ.description.text = " "

        }
        btnDevise.setOnClickListener {
            btnDevise.setBackgroundColor(getColor(R.color.colorWhite))
            btnBanka.setBackgroundColor(getColor(R.color.colorLightGreen))
            btnAllMoney.setBackgroundColor(getColor(R.color.colorLightGreen))
            btnNovcanik.setBackgroundColor(getColor(R.color.colorLightGreen))
            btnDevise.setTextColor(getResources().getColor(R.color.colorLightGreen))
            btnBanka.setTextColor(getResources().getColor(R.color.colorWhite))
            btnAllMoney.setTextColor(getResources().getColor(R.color.colorWhite))
            btnNovcanik.setTextColor(getResources().getColor(R.color.colorWhite))

            textViewHome.text = "Grafički prikaz prihoda u deviznom računu"
            textViewHome1.text = "Grafički prikaz rashoda u deviznom računu"
            var listCategorys = res.getStringArray(R.array.category)
            arrayBank.clear()
            arrayBankNegativ.clear()

            for (i in 0 until listCategorys.size) {
                if(listDeviseCategory[i]>0.0){
                    arrayBank.add(PieEntry(listDeviseCategory[i].toFloat(), listCategorys[i]))
                }
                if(listDeviseCategoryNegativ[i]<0.0){


                    var upis = listDeviseCategoryNegativ[i]*(-1)
                    var upis1 = upis / (listDeviseCategoryNegativ.sum()*-1)
                    upis1 = upis1 * 100
                    arrayBankNegativ.add(PieEntry(upis1.toFloat(), listCategorys[i]))
                }
            }
            val pieChart = findViewById<PieChart>(R.id.pieChart)
            val dataSet = PieDataSet(arrayBank, "")
            dataSet.setDrawIcons(false)
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0F, 40F)
            dataSet.selectionShift = 5f
            dataSet.setColors(*ColorTemplate.JOYFUL_COLORS, *ColorTemplate.COLORFUL_COLORS, *ColorTemplate.MATERIAL_COLORS  )

            val data = PieData(dataSet)
            data.setValueTextSize(8f)
            data.setValueTextColor(Color.parseColor("#FFFFFF"))
            pieChart.data = data
            pieChart.highlightValues(null)
            pieChart.setBackgroundColor(getColor(R.color.colorDarkGreen))
            pieChart.invalidate()
            pieChart.animateXY(5000, 5000)
            pieChart.description.text = " "


            val pieChartNegativ = findViewById<PieChart>(R.id.pieChart1)
            val dataSetNegativ = PieDataSet(arrayBankNegativ, "")
            dataSetNegativ.setDrawIcons(false)
            dataSetNegativ.sliceSpace = 3f
            dataSetNegativ.iconsOffset = MPPointF(0F, 40F)
            dataSetNegativ.selectionShift = 5f
            dataSetNegativ.setColors(*ColorTemplate.JOYFUL_COLORS, *ColorTemplate.COLORFUL_COLORS, *ColorTemplate.MATERIAL_COLORS  )

            val dataNegativ = PieData(dataSetNegativ)
            dataNegativ.setValueTextSize(8f)
            dataNegativ.setValueTextColor(Color.parseColor("#FFFFFF"))
            pieChartNegativ.data = dataNegativ
            pieChartNegativ.highlightValues(null)
            pieChartNegativ.setBackgroundColor(getColor(R.color.colorDarkGreen))
            pieChartNegativ.invalidate()
            pieChartNegativ.animateXY(5000, 5000)
            pieChartNegativ.description.text = " "
        }
        btnBanka.setOnClickListener {
            btnDevise.setBackgroundColor(getColor(R.color.colorLightGreen))
            btnBanka.setBackgroundColor(getColor(R.color.colorWhite))
            btnAllMoney.setBackgroundColor(getColor(R.color.colorLightGreen))
            btnNovcanik.setBackgroundColor(getColor(R.color.colorLightGreen))
            btnDevise.setTextColor(getResources().getColor(R.color.colorWhite))
            btnBanka.setTextColor(getResources().getColor(R.color.colorLightGreen))
            btnAllMoney.setTextColor(getResources().getColor(R.color.colorWhite))
            btnNovcanik.setTextColor(getResources().getColor(R.color.colorWhite))


            textViewHome.text = "Grafički prikaz prihoda u banci"
            textViewHome1.text = "Grafički prikaz rashoda u banci"
            arrayBank.clear()
            arrayBankNegativ.clear()
            for (i in 0 until listCategorys.size) {
                if(listBankCategory[i]>0.0){
                arrayBank.add(PieEntry(listBankCategory[i].toFloat(), listCategorys[i]))
                }
                if(listBankCategoryNeg[i]<0.0){


                    var upis = listBankCategoryNeg[i]*(-1)
                    var upis1 = upis / (listBankCategoryNeg.sum()*-1)
                    upis1 = upis1 * 100
                    arrayBankNegativ.add(PieEntry(upis1.toFloat(), listCategorys[i]))
                }
            }

            val dataSet = PieDataSet(arrayBank, "")

            dataSet.setDrawIcons(false)
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0F, 40F)
            dataSet.selectionShift = 5f
            dataSet.setColors(*ColorTemplate.JOYFUL_COLORS, *ColorTemplate.COLORFUL_COLORS, *ColorTemplate.MATERIAL_COLORS  )

            val data = PieData(dataSet)
            data.setValueTextSize(11f)
            data.setValueTextColor(Color.parseColor("#FFFFFF"))
            pieChart.data = data
            pieChart.highlightValues(null)
            pieChart.setBackgroundColor(getColor(R.color.colorDarkGreen))
            pieChart.invalidate()
            pieChart.animateXY(5000, 5000)
            pieChart.description.text = " "

            val pieChartNegativ = findViewById<PieChart>(R.id.pieChart1)
            val dataSetNegativ = PieDataSet(arrayBankNegativ, "")
            dataSetNegativ.setDrawIcons(false)
            dataSetNegativ.sliceSpace = 3f
            dataSetNegativ.iconsOffset = MPPointF(0F, 40F)
            dataSetNegativ.selectionShift = 5f
            dataSetNegativ.setColors(*ColorTemplate.JOYFUL_COLORS, *ColorTemplate.COLORFUL_COLORS, *ColorTemplate.MATERIAL_COLORS  )

            val dataNegativ = PieData(dataSetNegativ)
            dataNegativ.setValueTextSize(8f)
            dataNegativ.setValueTextColor(Color.parseColor("#FFFFFF"))
            pieChartNegativ.data = dataNegativ
            pieChartNegativ.highlightValues(null)
            pieChartNegativ.setBackgroundColor(getColor(R.color.colorDarkGreen))
            pieChartNegativ.invalidate()
            pieChartNegativ.animateXY(5000, 5000)
            pieChartNegativ.description.text = " "

        }
        btnAllMoney.setOnClickListener {
            btnDevise.setBackgroundColor(getColor(R.color.colorLightGreen))
            btnBanka.setBackgroundColor(getColor(R.color.colorLightGreen))
            btnAllMoney.setBackgroundColor(getColor(R.color.colorWhite))
            btnNovcanik.setBackgroundColor(getColor(R.color.colorLightGreen))
            textViewHome.text = "Grafički prikaz svih prihoda"
            textViewHome1.text = "Grafički prikaz svih rashoda"
            btnDevise.setTextColor(getResources().getColor(R.color.colorWhite))
            btnBanka.setTextColor(getResources().getColor(R.color.colorWhite))
            btnNovcanik.setTextColor(getResources().getColor(R.color.colorWhite))
            btnAllMoney.setTextColor(getResources().getColor(R.color.colorLightGreen))

            arrayBank.clear()
            arrayBankNegativ.clear()
            for (i in 0 until listCategorys.size) {
                if(listALLCategory[i]>0.0){
                arrayBank.add(PieEntry(listALLCategory[i].toFloat(), listCategorys[i]))}
                if(listAllCategoryNeg[i]<0.0){


                    var upis = listAllCategoryNeg[i]*(-1)
                    var upis1 = upis / (listAllCategoryNeg.sum()*-1)
                    upis1 = upis1 * 100
                    arrayBankNegativ.add(PieEntry(upis1.toFloat(), listCategorys[i]))
                }
            }
            val dataSet = PieDataSet(arrayBank, "")
            dataSet.setDrawIcons(false)
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0F, 40F)
            dataSet.selectionShift = 5f
            dataSet.setColors(*ColorTemplate.JOYFUL_COLORS, *ColorTemplate.COLORFUL_COLORS, *ColorTemplate.MATERIAL_COLORS  )

            val data = PieData(dataSet)
            data.setValueTextSize(11f)
            data.setValueTextColor(Color.parseColor("#FFFFFF"))
            pieChart.data = data
            pieChart.highlightValues(null)
            pieChart.setBackgroundColor(getColor(R.color.colorDarkGreen))
            pieChart.invalidate()
            pieChart.animateXY(5000, 5000)
            pieChart.description.text = " "

            val pieChartNegativ = findViewById<PieChart>(R.id.pieChart1)
            val dataSetNegativ = PieDataSet(arrayBankNegativ, "")
            dataSetNegativ.setDrawIcons(false)
            dataSetNegativ.sliceSpace = 3f
            dataSetNegativ.iconsOffset = MPPointF(0F, 40F)
            dataSetNegativ.selectionShift = 5f
            dataSetNegativ.setColors(*ColorTemplate.JOYFUL_COLORS, *ColorTemplate.COLORFUL_COLORS, *ColorTemplate.MATERIAL_COLORS  )

            val dataNegativ = PieData(dataSetNegativ)
            dataNegativ.setValueTextSize(8f)
            dataNegativ.setValueTextColor(Color.parseColor("#FFFFFF"))
            pieChartNegativ.data = dataNegativ
            pieChartNegativ.highlightValues(null)
            pieChartNegativ.setBackgroundColor(getColor(R.color.colorDarkGreen))
            pieChartNegativ.invalidate()
            pieChartNegativ.animateXY(5000, 5000)
            pieChartNegativ.description.text = " "
        }

        textViewShowMore.setOnClickListener{


            if(textViewShowMore.text == "Prikaži više"){

                textViewShowMore.text = "Prikaži  manje"
                boxPT.visibility= View.VISIBLE


            }else{
                textViewShowMore.text = "Prikaži više"
                boxPT.visibility = View.GONE
            }

            val context1 = this
            val db1 = DataBaseHandler(context1)

            btnAcceptPlans.setOnClickListener {
                if(editTextValuePlan.text.isNotEmpty()&&editTextIzdvajanje.text.isNotEmpty()){
                    if(editTextValuePlan.text.toString().toDouble()>0.0 && editTextIzdvajanje.text.toString().toDouble()>0.0){
                        if(editTextValuePlan.text.toString().toDouble()> editTextIzdvajanje.text.toString().toDouble()){
                        var datum = LocalDate.now()
                        when(spinnerPlan.selectedItemPosition){
                            0 ->{datum = LocalDate.now().plusDays(1)
                                Toast.makeText(this, "$datum" ,Toast.LENGTH_LONG).show()}
                            1 ->{datum = LocalDate.now().plusDays(7)
                                Toast.makeText(this, "$datum" ,Toast.LENGTH_LONG).show()}
                            2 ->{datum = LocalDate.now().plusDays(30)
                                Toast.makeText(this, "$datum" ,Toast.LENGTH_LONG).show()}
                        }
                        val userPlan = PlanUser(idUser,spinnerPlan.selectedItemPosition.toString().toInt(), editTextValuePlan.text.toString().toDouble(), editTextValuePlan.text.toString().toDouble(),
                            editTextIzdvajanje.text.toString().toDouble(), spinnerPlanKategorija.selectedItem.toString(), datum.format(
                                DateTimeFormatter.ofPattern("dd-MMM-yyyy")).toString(), spinnerPlanProfil.selectedItem.toString())
                        db1.insertPlan(userPlan)
                        editTextIzdvajanje.text.clear()
                        editTextValuePlan.text.clear()
                        }


                    }else{
                        Toast.makeText(this, "Vrijednosti moraju biti iznad 0" ,Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "Sva polja moraju biti popunjena" ,Toast.LENGTH_LONG).show()
                    //toast greska prayno polje
                }
            }
        }




        addNewActions.setOnClickListener { view ->
            var intentAddNewActivity = Intent(this, addNewActivity::class.java)
            intentAddNewActivity.putExtra("userValute", userValute)
            intentAddNewActivity.putExtra("idUser", idUser)
            startActivity(intentAddNewActivity)
        }
    }





}
