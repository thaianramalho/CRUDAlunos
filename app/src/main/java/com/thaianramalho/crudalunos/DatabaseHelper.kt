package com.thaianramalho.crudalunos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "escola.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "alunos"
        const val COLUMN_ID = "id"
        const val COLUMN_NOME = "nome"
        const val COLUMN_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NOME TEXT, $COLUMN_EMAIL TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertAluno(aluno: Aluno): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOME, aluno.nome)
        values.put(COLUMN_EMAIL, aluno.email)
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllAlunos(): List<Aluno> {
        val listaAlunos = mutableListOf<Aluno>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
                listaAlunos.add(Aluno(id, nome, email))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaAlunos
    }

    fun updateAluno(aluno: Aluno): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOME, aluno.nome)
        values.put(COLUMN_EMAIL, aluno.email)
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(aluno.id.toString()))
    }

    fun deleteAluno(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}
