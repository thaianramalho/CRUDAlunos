package com.thaianramalho.crudalunos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlunoAdapter(
    private val alunos: MutableList<Aluno>,
    private val onEdit: (Aluno) -> Unit,
    private val onDelete: (Aluno) -> Unit
) : RecyclerView.Adapter<AlunoAdapter.AlunoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlunoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_aluno, parent, false)
        return AlunoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlunoViewHolder, position: Int) {
        val aluno = alunos[position]
        holder.bind(aluno)
    }

    override fun getItemCount(): Int = alunos.size

    inner class AlunoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewNome: TextView = itemView.findViewById(R.id.textViewNome)
        private val textViewEmail: TextView = itemView.findViewById(R.id.textViewEmail)
        private val buttonEdit: Button = itemView.findViewById(R.id.buttonEdit)
        private val buttonDelete: Button = itemView.findViewById(R.id.buttonDelete)

        fun bind(aluno: Aluno) {
            textViewNome.text = aluno.nome
            textViewEmail.text = aluno.email

            buttonEdit.setOnClickListener { onEdit(aluno) }
            buttonDelete.setOnClickListener { onDelete(aluno) }
        }
    }
}
