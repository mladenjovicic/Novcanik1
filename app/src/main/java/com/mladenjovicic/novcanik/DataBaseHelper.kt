package com.mladenjovicic.novcanik

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract
import android.widget.Toast

val DATABASENAME = "MYDATABASE5"
//tabela za korsinika
val TABLENAME = "Users"
val COL_ID = "id"
val COL_NAME = "name"
val COL_LASTNAME = "lastName"
val COL_EMAIL = "email"
val COL_PASSWORD = "password"
val COL_PRIMARY_MONEY = "primaryMoney"
val COL_USER_LANG = "userLang"
//tabela za Akcije korisnika
val TABLENAMEACTIONS = "Actions"
val COL_ID_ACT = "id"
val COL_ID_USER = "idUser"
val COL_CURRENCY = "idValuta"
val COL_MONEY = "money"
val COL_TIME_DATE = "timeDate"
val COL_CATEGORY = "category"
val COL_PROFIL = "profil"
//Tabela za planirane troskove
val TABLENAMEPLAN = "PlanUser"
val COL_ID_PLA = "id"
val COL_ID_USER_PLA = "idUserPlan"
val COL_PLAN = "plan"
val COL_PLAN_MONEY = "moneyPlan"
val COL_OTHER_MONEY = "otherMoney"
val COL_MONEY_RATA= "moneyRate"
val COL_NEXT_PAYMENT = "nextPayment"
val COL_CATEGORY_PLAN = "categoryPlan"
val COL_PROFIL_PLAN = "profilPlan"


class DataBaseHandler(var context: Context) : SQLiteOpenHelper(
    context, DATABASENAME, null,
    1
) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLENAME + " " +
                "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_NAME + " VARCHAR(256)," + COL_LASTNAME + " VARCHAR(256)," + COL_EMAIL + " VARCHAR(256)," + COL_PASSWORD + " VARCHAR(256)," + COL_PRIMARY_MONEY + " VARCHAR(256)," + COL_USER_LANG + " VARCHAR(256))"
        db?.execSQL(createTable)

        val createTableActions = "CREATE TABLE " + TABLENAMEACTIONS + " " +
                "(" + COL_ID_ACT + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_ID_USER + " INTEGER," + COL_CURRENCY + " VARCHAR(256)," + COL_MONEY + " DOUBLE," + COL_CATEGORY + " VARCHAR(256)," + COL_PROFIL + " VARCHAR(256)," + COL_TIME_DATE + " VARCHAR(256))"
        db?.execSQL(createTableActions)

        val createTablePlan = "CREATE TABLE "+ TABLENAMEPLAN +" "+
                "("+ COL_ID_PLA+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ COL_ID_USER_PLA + " INTEGER,"+COL_PLAN+ " INTEGER," + COL_PLAN_MONEY + " DOUBLE," + COL_OTHER_MONEY+ " DOUBLE,"+ COL_MONEY_RATA + " DOUBLE," + COL_CATEGORY_PLAN  + " VARCHAR(256)," + COL_NEXT_PAYMENT + " VARCHAR(256)," + COL_PROFIL_PLAN + " VARCHAR(256))"
        db?.execSQL(createTablePlan)

    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {


    }

    fun insertData(user: User) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, user.name)
        contentValues.put(COL_LASTNAME, user.lastName)
        contentValues.put(COL_EMAIL, user.email)
        contentValues.put(COL_PASSWORD, user.password)
        contentValues.put(COL_PRIMARY_MONEY, user.primaryMoney)
        contentValues.put(COL_USER_LANG, user.userLang)
        val result = database.insert(TABLENAME, null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Neuspjesno ste se registrovali", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Uspjesno ste se registrovali", Toast.LENGTH_SHORT).show()
        }
    }

    fun readData(): MutableList<User> {
        val list: MutableList<User> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val user = User()
                user.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                user.name = result.getString(result.getColumnIndex(COL_NAME))
                user.lastName = result.getString(result.getColumnIndex(COL_LASTNAME))
                user.email = result.getString(result.getColumnIndex(COL_EMAIL))
                user.password = result.getString(result.getColumnIndex(COL_PASSWORD))
                user.primaryMoney = result.getString(result.getColumnIndex(COL_PRIMARY_MONEY))
                user.userLang = result.getString(result.getColumnIndex(COL_USER_LANG))
                list.add(user)
            } while (result.moveToNext())
        }
        return list
    }
    fun insertActions(userActvities: UserActvities) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID_USER, userActvities.idUser)
        contentValues.put(COL_CURRENCY, userActvities.idValuta)
        contentValues.put(COL_MONEY, userActvities.money)
        contentValues.put(COL_TIME_DATE, userActvities.timeDate)
        contentValues.put(COL_CATEGORY, userActvities.category)
        contentValues.put(COL_PROFIL, userActvities.profil)
        contentValues.put(COL_TIME_DATE, userActvities.timeDate)
        val result = database.insert(TABLENAMEACTIONS, null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Neuspjesno ste se dodali akciju", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Uspjesno ste se dodali akciju", Toast.LENGTH_SHORT).show()
        }

    }
    fun insertPlan(planUser: PlanUser){
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID_USER_PLA, planUser.idUser)
        contentValues.put(COL_PLAN, planUser.plan)
        contentValues.put(COL_PLAN_MONEY, planUser.moneyPlan)
        contentValues.put(COL_OTHER_MONEY,planUser.otherMoney)
        contentValues.put(COL_MONEY_RATA, planUser.moneyRata)
        contentValues.put(COL_NEXT_PAYMENT,planUser.nextPayment)
        contentValues.put(COL_CATEGORY_PLAN,planUser.categoryPlan)
        contentValues.put(COL_PROFIL_PLAN, planUser.profilPlan)
        val result = database.insert(TABLENAMEPLAN, null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Neuspjesno ste se dodali akciju", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Uspjesno ste se dodali akciju", Toast.LENGTH_SHORT).show()
        }
    }

    fun readPlan(): MutableList<PlanUser>{
        var list: MutableList<PlanUser> = arrayListOf()
        val db = this.readableDatabase
        val query = "select * from $TABLENAMEPLAN "
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do{
                val planUser = PlanUser()
                planUser.id= result.getString(result.getColumnIndex(COL_ID_PLA)).toInt()
                planUser.idUser= result.getString(result.getColumnIndex(COL_ID_USER_PLA)).toInt()
                planUser.categoryPlan=result.getString(result.getColumnIndex(COL_CATEGORY_PLAN))
                planUser.moneyPlan=result.getString(result.getColumnIndex(COL_PLAN_MONEY)).toDouble()
                planUser.moneyRata=result.getString(result.getColumnIndex(COL_MONEY_RATA)).toDouble()
                planUser.nextPayment=result.getString(result.getColumnIndex(COL_NEXT_PAYMENT))
                planUser.otherMoney=result.getString(result.getColumnIndex(COL_OTHER_MONEY)).toDouble()
                planUser.plan=result.getString(result.getColumnIndex(COL_PLAN)).toInt()
                planUser.profilPlan = result.getString(result.getColumnIndex(COL_PROFIL_PLAN))
                list.add(planUser)
            }while (result.moveToNext())

        }

        return list
    }

    fun readActions(): MutableList<UserActvities> {
        var list: MutableList<UserActvities> = arrayListOf()
        val db = this.readableDatabase
        val query = "select * from $TABLENAMEACTIONS"
        val result = db.rawQuery(query, null)
        if (result.moveToLast()) {
            do {
                val userActvities = UserActvities()
                userActvities.id = result.getString(result.getColumnIndex(COL_ID_ACT)).toInt()
                userActvities.idUser = result.getString(result.getColumnIndex(COL_ID_USER)).toInt()
                userActvities.idValuta = result.getString(result.getColumnIndex(COL_CURRENCY))
                userActvities.money = result.getString(result.getColumnIndex(COL_MONEY)).toDouble()
                userActvities.category = result.getString(result.getColumnIndex(COL_CATEGORY))
                userActvities.profil = result.getString(result.getColumnIndex(COL_PROFIL))
                userActvities.timeDate = result.getString(result.getColumnIndex(COL_TIME_DATE))
                list.add(userActvities)
            } while (result.moveToPrevious())
        }
        return list
    }

    fun deleteActions(id: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLENAMEACTIONS, "id = ?", arrayOf(id))
    }

    fun updateData(
        id: String,
        idUser: Int,
        idValuta: String,
        moneys: Double,
        timeDate: String,
        category: String,
        profil: String
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID_ACT, id)
        contentValues.put(COL_ID_USER, idUser)
        contentValues.put(COL_CURRENCY, idValuta)
        contentValues.put(COL_MONEY, moneys)
        contentValues.put(COL_TIME_DATE, timeDate)
        contentValues.put(COL_CATEGORY, category)
        contentValues.put(COL_PROFIL, profil)
        db.update(TABLENAMEACTIONS, contentValues, "id = ?", arrayOf(id))
        return true
    }
    fun updatePlan(id: String,
               idUser:Int,
               plan:Int,
               moneyPlan:Double,
               otherMoney:Double,
               moneyRata:Double,
               categoryPlan:String,
               nextPayment:String,
               profilPlan:String):Boolean{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID_PLA, id)
        contentValues.put(COL_ID_USER_PLA,idUser)
        contentValues.put(COL_PLAN, plan)
        contentValues.put(COL_PLAN_MONEY, moneyPlan)
        contentValues.put(COL_OTHER_MONEY, otherMoney)
        contentValues.put(COL_MONEY_RATA, moneyRata)
        contentValues.put(COL_CATEGORY_PLAN,categoryPlan)
        contentValues.put(COL_NEXT_PAYMENT,nextPayment)
        contentValues.put(COL_PROFIL_PLAN, profilPlan)

        db.update(TABLENAMEPLAN, contentValues, "id = ?", arrayOf(id))
        return true
    }


}