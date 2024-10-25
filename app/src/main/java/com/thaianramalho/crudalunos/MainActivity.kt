package com.thaianramalho.crudalunos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: AlunoAdapter
    private val alunos = mutableListOf<Aluno>()

    private var alunoAtual: Aluno? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        val editTextNome = findViewById<EditText>(R.id.editTextNome)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val buttonSalvar = findViewById<Button>(R.id.buttonSalvar)

        val recyclerViewAlunos = findViewById<RecyclerView>(R.id.recyclerViewAlunos)
        recyclerViewAlunos.layoutManager = LinearLayoutManager(this)
        adapter = AlunoAdapter(alunos, ::iniciarEdicaoAluno, ::deleteAluno)
        recyclerViewAlunos.adapter = adapter

        loadAlunos()

        buttonSalvar.setOnClickListener {
            val nome = editTextNome.text.toString()
            val email = editTextEmail.text.toString()

            if (nome.isNotEmpty() && email.isNotEmpty()) {
                if (alunoAtual == null) {
                    val aluno = Aluno(nome = nome, email = email)
                    val id = dbHelper.insertAluno(aluno)

                    if (id != -1L) {
                        Toast.makeText(this, "Aluno inserido com sucesso!", Toast.LENGTH_SHORT).show()
                        editTextNome.text.clear()
                        editTextEmail.text.clear()
                        loadAlunos()
                    } else {
                        Toast.makeText(this, "Erro ao inserir o aluno.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    alunoAtual?.nome = nome
                    alunoAtual?.email = email
                    alunoAtual?.let {
                        dbHelper.updateAluno(it)
                        Toast.makeText(this, "Aluno atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                        editTextNome.text.clear()
                        editTextEmail.text.clear()
                        alunoAtual = null
                        loadAlunos()
                    }
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadAlunos() {
        alunos.clear()
        alunos.addAll(dbHelper.getAllAlunos())
        adapter.notifyDataSetChanged()
    }

    private fun iniciarEdicaoAluno(aluno: Aluno) {
        val editTextNome = findViewById<EditText>(R.id.editTextNome)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)

        editTextNome.setText(aluno.nome)
        editTextEmail.setText(aluno.email)
        alunoAtual = aluno
    }

    private fun deleteAluno(aluno: Aluno) {
        dbHelper.deleteAluno(aluno.id)
        loadAlunos()
        Toast.makeText(this, "Aluno excluído!", Toast.LENGTH_SHORT).show()
    }
}
